/*-
 * ============LICENSE_START=======================================================
 *  Copyright (C) 2020 Nordix Foundation.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 * ============LICENSE_END=========================================================
 */

package org.onap.aaf.certservice.cmpv2client.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateParsingException;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.PKIXCertPathValidatorResult;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.bouncycastle.asn1.cmp.CMPCertificate;
import org.bouncycastle.asn1.cmp.CertRepMessage;
import org.bouncycastle.asn1.cmp.ErrorMsgContent;
import org.bouncycastle.asn1.cmp.PKIBody;
import org.bouncycastle.asn1.cmp.PKIMessage;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.onap.aaf.certservice.cmpv2client.exceptions.CmpClientException;
import org.onap.aaf.certservice.cmpv2client.exceptions.PkiErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CmpResponseHelper {

  private static final Logger LOG = LoggerFactory.getLogger(CmpMessageHelper.class);

  public static void checkIfCmpResponseContainsError(PKIMessage respPkiMessage)
      throws CmpClientException {
    if (respPkiMessage.getBody().getType() == PKIBody.TYPE_ERROR) {
      final ErrorMsgContent errorMsgContent =
          (ErrorMsgContent) respPkiMessage.getBody().getContent();
      PkiErrorException pkiErrorException =
          new PkiErrorException(
              errorMsgContent.getPKIStatusInfo().getStatusString().getStringAt(0).getString());
      CmpClientException cmpClientException =
          new CmpClientException("Error in the PkiMessage response", pkiErrorException);
      LOG.error("Error in the PkiMessage response: {} ", pkiErrorException.getMessage());
      throw cmpClientException;
    }
  }

  /**
   * @param cert byte array that contains certificate
   * @param returnType the type of Certificate to be returned, for example X509Certificate.class.
   *     Certificate.class can be used if certificate type is unknown.
   * @throws CertificateParsingException if the byte array does not contain a proper certificate.
   */
  public static <T extends Certificate> Optional<X509Certificate> getCertfromByteArray(
      byte[] cert, Class<T> returnType) throws CertificateParsingException, CmpClientException {
    LOG.info("getting certificate from byte array");
    return getCertfromByteArray(cert, BouncyCastleProvider.PROVIDER_NAME, returnType);
  }

  /**
   * @param cert byte array that contains certificate
   * @param provider provider used to generate certificate from bytes
   * @param returnType the type of Certificate to be returned, for example X509Certificate.class.
   *     Certificate.class can be used if certificate type is unknown.
   * @throws CertificateParsingException if the byte array does not contain a proper certificate.
   */
  public static <T extends Certificate> Optional<X509Certificate> getCertfromByteArray(
      byte[] cert, String provider, Class<T> returnType)
      throws CertificateParsingException, CmpClientException {
    String prov = provider;
    if (provider == null) {
      prov = BouncyCastleProvider.PROVIDER_NAME;
    }

    if (returnType.equals(X509Certificate.class)) {
      return parseX509Certificate(prov, cert);
    }
    return Optional.empty();
  }

  /**
   * Check the certificate with CA certificate.
   *
   * @param caCertChain Collection of X509Certificates. May not be null, an empty list or a
   *     Collection with null entries.
   * @throws CmpClientException if verification failed
   */
  public static void verify(List<X509Certificate> caCertChain) throws CmpClientException {
    int iterator = 1;
    while (iterator < caCertChain.size()) {
      verify(caCertChain.get(iterator - 1), caCertChain.get(iterator), null);
      iterator += 1;
    }
  }

  /**
   * Check the certificate with CA certificate.
   *
   * @param certificate X.509 certificate to verify. May not be null.
   * @param caCertChain Collection of X509Certificates. May not be null, an empty list or a
   *     Collection with null entries.
   * @param date Date to verify at, or null to use current time.
   * @param pkixCertPathCheckers optional PKIXCertPathChecker implementations to use during cert
   *     path validation
   * @throws CmpClientException if certificate could not be validated
   */
  public static void verify(
      X509Certificate certificate,
      X509Certificate caCertChain,
      Date date,
      PKIXCertPathChecker... pkixCertPathCheckers)
      throws CmpClientException {
    try {
      verifyCertificates(certificate, caCertChain, date, pkixCertPathCheckers);
    } catch (CertPathValidatorException cpve) {
      CmpClientException cmpClientException =
          new CmpClientException(
              "Invalid certificate or certificate not issued by specified CA: ", cpve);
      LOG.error("Invalid certificate or certificate not issued by specified CA: ", cpve);
      throw cmpClientException;
    } catch (CertificateException ce) {
      CmpClientException cmpClientException =
          new CmpClientException("Something was wrong with the supplied certificate", ce);
      LOG.error("Something was wrong with the supplied certificate", ce);
      throw cmpClientException;
    } catch (NoSuchProviderException nspe) {
      CmpClientException cmpClientException =
          new CmpClientException("BouncyCastle provider not found.", nspe);
      LOG.error("BouncyCastle provider not found.", nspe);
      throw cmpClientException;
    } catch (NoSuchAlgorithmException nsae) {
      CmpClientException cmpClientException =
          new CmpClientException("Algorithm PKIX was not found.", nsae);
      LOG.error("Algorithm PKIX was not found.", nsae);
      throw cmpClientException;
    } catch (InvalidAlgorithmParameterException iape) {
      CmpClientException cmpClientException =
          new CmpClientException(
              "Either ca certificate chain was empty,"
                  + " or the certificate was on an inappropriate type for a PKIX path checker.",
              iape);
      LOG.error(
          "Either ca certificate chain was empty, "
              + "or the certificate was on an inappropriate type for a PKIX path checker.",
          iape);
      throw cmpClientException;
    }
  }

  public static void verifyCertificates(
      X509Certificate certificate,
      X509Certificate caCertChain,
      Date date,
      PKIXCertPathChecker[] pkixCertPathCheckers)
      throws CertificateException, NoSuchProviderException, InvalidAlgorithmParameterException,
          NoSuchAlgorithmException, CertPathValidatorException {
    LOG.info("verifying certificates in cert chain");
    CertPath cp = getCertPath(certificate);
    PKIXParameters params = getPkixParameters(caCertChain, date, pkixCertPathCheckers);
    CertPathValidator cpv =
        CertPathValidator.getInstance("PKIX", BouncyCastleProvider.PROVIDER_NAME);
    PKIXCertPathValidatorResult result = (PKIXCertPathValidatorResult) cpv.validate(cp, params);
    if (LOG.isDebugEnabled()) {
      LOG.debug("Certificate verify result:{} ", result.toString());
    }
  }

  public static PKIXParameters getPkixParameters(
      X509Certificate caCertChain, Date date, PKIXCertPathChecker[] pkixCertPathCheckers)
      throws InvalidAlgorithmParameterException {
    TrustAnchor anchor = new TrustAnchor(caCertChain, null);
    PKIXParameters params = new PKIXParameters(Collections.singleton(anchor));
    for (final PKIXCertPathChecker pkixCertPathChecker : pkixCertPathCheckers) {
      params.addCertPathChecker(pkixCertPathChecker);
    }
    params.setRevocationEnabled(false);
    params.setDate(date);
    return params;
  }

  public static CertPath getCertPath(X509Certificate certificate)
      throws CertificateException, NoSuchProviderException {
    ArrayList<X509Certificate> certlist = new ArrayList<>();
    certlist.add(certificate);
    return CertificateFactory.getInstance("X.509", BouncyCastleProvider.PROVIDER_NAME)
        .generateCertPath(certlist);
  }

  /**
   * Parse a X509Certificate from an array of bytes
   *
   * @param provider a provider name
   * @param cert a byte array containing an encoded certificate
   * @return a decoded X509Certificate
   * @throws CertificateParsingException if the byte array wasn't valid, or contained a certificate
   *     other than an X509 Certificate.
   */
  public static Optional<X509Certificate> parseX509Certificate(String provider, byte[] cert)
      throws CertificateParsingException, CmpClientException {
    LOG.info("parsing X509Certificate");
    final CertificateFactory cf = getCertificateFactory(provider);
    X509Certificate result;
    try {
      result =
          (X509Certificate)
              Objects.requireNonNull(cf).generateCertificate(new ByteArrayInputStream(cert));
    } catch (CertificateException ce) {
      throw new CertificateParsingException("Could not parse byte array as X509Certificate ", ce);
    }
    if (result != null) {
      return Optional.of(result);
    } else {
      throw new CertificateParsingException("Could not parse byte array as X509Certificate.");
    }
  }

  /**
   * Returns a CertificateFactory that can be used to create certificates from byte arrays and such.
   *
   * @param provider Security provider that should be used to create certificates, default BC is
   *     null is passed.
   * @return CertificateFactory for creating certificate
   */
  public static CertificateFactory getCertificateFactory(final String provider)
      throws CmpClientException {
    LOG.info("getting certificate Factory");
    final String prov;
    prov = Objects.requireNonNullElse(provider, BouncyCastleProvider.PROVIDER_NAME);
    try {
      return CertificateFactory.getInstance("X.509", prov);
    } catch (NoSuchProviderException nspe) {
      CmpClientException cmpClientException = new CmpClientException("NoSuchProvider: ", nspe);
      LOG.error("NoSuchProvider: ", nspe);
      throw cmpClientException;
    } catch (CertificateException ce) {
      CmpClientException cmpClientException = new CmpClientException("CertificateException: ", ce);
      LOG.error("CertificateException: ", ce);
      throw cmpClientException;
    }
  }

  /**
   * puts together certChain and Trust store and verifies the certChain
   *
   * @param respPkiMessage PKIMessage that may contain extra certs used for certchain
   * @param certRepMessage CertRepMessage that should contain rootCA for certchain
   * @param leafCertificate certificate returned from our original Cert Request
   * @param certChain Array of certificates to be used for KeyStore
   * @param trustStore Array of certificates to be used for TrustStore
   * @return list of two lists, CertChain and TrustStore
   * @throws CertificateParsingException thrown if error occurs while parsing certificate
   * @throws IOException thrown if IOException occurs while parsing certificate
   * @throws CmpClientException thrown if error occurs during the verification of the certChain
   */
  public static List<List<X509Certificate>> verifyAndReturnCertChainAndTrustSTore(
      PKIMessage respPkiMessage,
      CertRepMessage certRepMessage,
      X509Certificate leafCertificate,
      ArrayList<X509Certificate> certChain,
      ArrayList<X509Certificate> trustStore)
      throws CertificateParsingException, IOException, CmpClientException {
    LOG.info("verifying the certificates in the cert chain.");
    certChain.add(leafCertificate);
    addExtraCertsToChain(respPkiMessage, certRepMessage, certChain);
    verify(certChain);
    List<List<X509Certificate>> listOfArray = new ArrayList<>();
    listOfArray.add(certChain);
    listOfArray.add(trustStore);
    return listOfArray;
  }

  /**
   * checks whether PKIMessage contains extracerts to create certchain, if not creates from caPubs
   *
   * @param respPkiMessage PKIMessage that may contain extra certs used for certchain
   * @param certRepMessage CertRepMessage that should contain rootCA for certchain
   * @param certChain Array of certificates to be used for KeyStore
   * @throws CertificateParsingException thrown if error occurs while parsing certificate
   * @throws IOException thrown if IOException occurs while parsing certificate
   * @throws CmpClientException thrown if there are errors creating CertificateFactory
   */
  public static void addExtraCertsToChain(
      PKIMessage respPkiMessage,
      CertRepMessage certRepMessage,
      ArrayList<X509Certificate> certChain)
      throws CertificateParsingException, IOException, CmpClientException {
    LOG.info("adding certificates to chain");
    if (respPkiMessage.getExtraCerts() != null) {
      final CMPCertificate[] extraCerts = respPkiMessage.getExtraCerts();
      for (CMPCertificate cmpCert : extraCerts) {
        Optional<X509Certificate> cert =
            getCertfromByteArray(cmpCert.getEncoded(), X509Certificate.class);
        certChain.add(cert.get());
      }
    } else {
      final CMPCertificate respCmpCaCert = getRootCa(certRepMessage);
      Optional<X509Certificate> cert =
          getCertfromByteArray(respCmpCaCert.getEncoded(), X509Certificate.class);
      certChain.add(cert.get());
    }
  }

  private static CMPCertificate getRootCa(CertRepMessage certRepMessage) {
    return certRepMessage.getCaPubs()[0];
  }
}
