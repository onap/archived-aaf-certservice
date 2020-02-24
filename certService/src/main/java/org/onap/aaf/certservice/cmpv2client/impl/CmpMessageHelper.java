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

import static org.onap.aaf.certservice.cmpv2client.impl.CmpUtil.generateProtectedBytes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Signature;
import java.security.SignatureException;
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
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DEROutputStream;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.cmp.CMPCertificate;
import org.bouncycastle.asn1.cmp.CertRepMessage;
import org.bouncycastle.asn1.cmp.ErrorMsgContent;
import org.bouncycastle.asn1.cmp.PBMParameter;
import org.bouncycastle.asn1.cmp.PKIBody;
import org.bouncycastle.asn1.cmp.PKIHeader;
import org.bouncycastle.asn1.cmp.PKIMessage;
import org.bouncycastle.asn1.crmf.CertRequest;
import org.bouncycastle.asn1.crmf.OptionalValidity;
import org.bouncycastle.asn1.crmf.POPOSigningKey;
import org.bouncycastle.asn1.crmf.ProofOfPossession;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.ExtensionsGenerator;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.Time;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.onap.aaf.certservice.cmpv2client.exceptions.CmpClientException;
import org.onap.aaf.certservice.cmpv2client.exceptions.PkiErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CmpMessageHelper {

  private static final Logger LOG = LoggerFactory.getLogger(CmpMessageHelper.class);
  private static final AlgorithmIdentifier OWF_ALGORITHM =
      new AlgorithmIdentifier(new ASN1ObjectIdentifier("1.3.14.3.2.26"));
  private static final AlgorithmIdentifier MAC_ALGORITHM =
      new AlgorithmIdentifier(new ASN1ObjectIdentifier("1.2.840.113549.2.9"));
  private static final ASN1ObjectIdentifier PASSWORD_BASED_MAC =
      new ASN1ObjectIdentifier("1.2.840.113533.7.66.13");

  private CmpMessageHelper() {}

  /**
   * Creates an Optional Validity, which is used to specify how long the returned cert should be
   * valid for.
   *
   * @param notBefore Date specifying certificate is not valid before this date.
   * @param notAfter Date specifying certificate is not valid after this date.
   * @return {@link OptionalValidity} that can be set for certificate on external CA.
   */
  public static OptionalValidity generateOptionalValidity(
      final Date notBefore, final Date notAfter) {
    LOG.info("Generating Optional Validity from Date objects");
    ASN1EncodableVector optionalValidityV = new ASN1EncodableVector();
    if (notBefore != null) {
      Time nb = new Time(notBefore);
      optionalValidityV.add(new DERTaggedObject(true, 0, nb));
    }
    if (notAfter != null) {
      Time na = new Time(notAfter);
      optionalValidityV.add(new DERTaggedObject(true, 1, na));
    }
    return OptionalValidity.getInstance(new DERSequence(optionalValidityV));
  }

  /**
   * Create Extensions from Subject Alternative Names.
   *
   * @return {@link Extensions}.
   */
  public static Extensions generateExtension(final List<String> sansList)
      throws CmpClientException {
    LOG.info("Generating Extensions from Subject Alternative Names");
    final ExtensionsGenerator extGenerator = new ExtensionsGenerator();
    final GeneralName[] sansGeneralNames = getGeneralNames(sansList);
    // KeyUsage
    try {
      final KeyUsage keyUsage =
          new KeyUsage(
              KeyUsage.digitalSignature | KeyUsage.keyEncipherment | KeyUsage.nonRepudiation);
      extGenerator.addExtension(Extension.keyUsage, false, new DERBitString(keyUsage));
      extGenerator.addExtension(
          Extension.subjectAlternativeName, false, new GeneralNames(sansGeneralNames));
    } catch (IOException ioe) {
      CmpClientException cmpClientException =
          new CmpClientException(
              "Exception occurred while creating extensions for PKIMessage", ioe);
      LOG.error("Exception occurred while creating extensions for PKIMessage");
      throw cmpClientException;
    }
    return extGenerator.generate();
  }

  public static GeneralName[] getGeneralNames(List<String> sansList) {
    final List<GeneralName> nameList = new ArrayList<>();
    for (String san : sansList) {
      nameList.add(new GeneralName(GeneralName.dNSName, san));
    }
    final GeneralName[] sansGeneralNames = new GeneralName[nameList.size()];
    nameList.toArray(sansGeneralNames);
    return sansGeneralNames;
  }

  /**
   * Method generates Proof-of-Possession (POP) of Private Key. To allow a CA/RA to properly
   * validity binding between an End Entity and a Key Pair, the PKI Operations specified here make
   * it possible for an End Entity to prove that it has possession of the Private Key corresponding
   * to the Public Key for which a Certificate is requested.
   *
   * @param certRequest Certificate request that requires proof of possession
   * @param keypair keypair associated with the subject sending the certificate request
   * @return {@link ProofOfPossession}.
   * @throws CmpClientException A general-purpose Cmp client exception.
   */
  public static ProofOfPossession generateProofOfPossession(
      final CertRequest certRequest, final KeyPair keypair) throws CmpClientException {
    ProofOfPossession proofOfPossession;
    try (final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
      final DEROutputStream derOutputStream = new DEROutputStream(byteArrayOutputStream);
      derOutputStream.writeObject(certRequest);

      byte[] popoProtectionBytes = byteArrayOutputStream.toByteArray();
      final String sigalg = PKCSObjectIdentifiers.sha256WithRSAEncryption.getId();
      final Signature signature = Signature.getInstance(sigalg, BouncyCastleProvider.PROVIDER_NAME);
      signature.initSign(keypair.getPrivate());
      signature.update(popoProtectionBytes);
      DERBitString bs = new DERBitString(signature.sign());

      proofOfPossession =
          new ProofOfPossession(
              new POPOSigningKey(
                  null, new AlgorithmIdentifier(new ASN1ObjectIdentifier(sigalg)), bs));
    } catch (IOException
        | NoSuchProviderException
        | NoSuchAlgorithmException
        | InvalidKeyException
        | SignatureException ex) {
      CmpClientException cmpClientException =
          new CmpClientException(
              "Exception occurred while creating proof of possession for PKIMessage", ex);
      LOG.error("Exception occurred while creating proof of possession for PKIMessage");
      throw cmpClientException;
    }
    return proofOfPossession;
  }

  /**
   * Generic code to create Algorithm Identifier for protection of PKIMessage.
   *
   * @return Algorithm Identifier
   */
  public static AlgorithmIdentifier protectionAlgoIdentifier(int iterations, byte[] salt) {
    ASN1Integer iteration = new ASN1Integer(iterations);
    DEROctetString derSalt = new DEROctetString(salt);

    PBMParameter pp = new PBMParameter(derSalt, OWF_ALGORITHM, iteration, MAC_ALGORITHM);
    return new AlgorithmIdentifier(PASSWORD_BASED_MAC, pp);
  }

  /**
   * Adds protection to the PKIMessage via a specified protection algorithm.
   *
   * @param password password used to authenticate PkiMessage with external CA
   * @param pkiHeader Header of PKIMessage containing generic details for any PKIMessage
   * @param pkiBody Body of PKIMessage containing specific details for certificate request
   * @return Protected Pki Message
   * @throws CmpClientException Wraps several exceptions into one general-purpose exception.
   */
  public static PKIMessage protectPkiMessage(
      PKIHeader pkiHeader, PKIBody pkiBody, String password, int iterations, byte[] salt)
      throws CmpClientException {

    byte[] raSecret = password.getBytes();
    byte[] basekey = new byte[raSecret.length + salt.length];
    System.arraycopy(raSecret, 0, basekey, 0, raSecret.length);
    System.arraycopy(salt, 0, basekey, raSecret.length, salt.length);
    byte[] out;
    try {
      MessageDigest dig =
          MessageDigest.getInstance(
              OWF_ALGORITHM.getAlgorithm().getId(), BouncyCastleProvider.PROVIDER_NAME);
      for (int i = 0; i < iterations; i++) {
        basekey = dig.digest(basekey);
        dig.reset();
      }
      byte[] protectedBytes = generateProtectedBytes(pkiHeader, pkiBody);
      Mac mac =
          Mac.getInstance(MAC_ALGORITHM.getAlgorithm().getId(), BouncyCastleProvider.PROVIDER_NAME);
      SecretKey key = new SecretKeySpec(basekey, MAC_ALGORITHM.getAlgorithm().getId());
      mac.init(key);
      mac.reset();
      mac.update(protectedBytes, 0, protectedBytes.length);
      out = mac.doFinal();
    } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException ex) {
      CmpClientException cmpClientException =
          new CmpClientException(
              "Exception occurred while generating proof of possession for PKIMessage", ex);
      LOG.error("Exception occured while generating the proof of possession for PKIMessage");
      throw cmpClientException;
    }
    DERBitString bs = new DERBitString(out);

    return new PKIMessage(pkiHeader, pkiBody, bs);
  }

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
  public static <T extends Certificate> T getCertfromByteArray(byte[] cert, Class<T> returnType)
      throws CertificateParsingException, CmpClientException {
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
  public static <T extends Certificate> T getCertfromByteArray(
      byte[] cert, String provider, Class<T> returnType)
      throws CertificateParsingException, CmpClientException {
    String prov = provider;
    if (provider == null) {
      prov = BouncyCastleProvider.PROVIDER_NAME;
    }

    if (returnType.equals(X509Certificate.class)) {
      return returnType.cast(parseX509Certificate(prov, cert));
    }
    return null;
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
      LOG.info("verifying certificates in cert chain");
      ArrayList<X509Certificate> certlist = new ArrayList<>();
      certlist.add(certificate);
      CertPath cp =
          CertificateFactory.getInstance("X.509", BouncyCastleProvider.PROVIDER_NAME)
              .generateCertPath(certlist);
      TrustAnchor anchor = new TrustAnchor(caCertChain, null);
      PKIXParameters params = new PKIXParameters(Collections.singleton(anchor));
      for (final PKIXCertPathChecker pkixCertPathChecker : pkixCertPathCheckers) {
        params.addCertPathChecker(pkixCertPathChecker);
      }
      params.setRevocationEnabled(false);
      params.setDate(date);
      CertPathValidator cpv =
          CertPathValidator.getInstance("PKIX", BouncyCastleProvider.PROVIDER_NAME);
      PKIXCertPathValidatorResult result = (PKIXCertPathValidatorResult) cpv.validate(cp, params);
      if (LOG.isDebugEnabled()) {
        LOG.debug("Certificate verify result:{} ", result.toString());
      }
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

  /**
   * Parse a X509Certificate from an array of bytes
   *
   * @param provider a provider name
   * @param cert a byte array containing an encoded certificate
   * @return a decoded X509Certificate
   * @throws CertificateParsingException if the byte array wasn't valid, or contained a certificate
   *     other than an X509 Certificate.
   */
  public static X509Certificate parseX509Certificate(String provider, byte[] cert)
      throws CertificateParsingException, CmpClientException {
    LOG.info("parsing X509Certificate");
    final CertificateFactory cf = getCertificateFactory(provider);
    X509Certificate result;
    try {
      result =
          (X509Certificate)
              Objects.requireNonNull(cf).generateCertificate(new ByteArrayInputStream(cert));
    } catch (CertificateException ce) {
      throw new CertificateParsingException(
          "Could not parse byte array as X509Certificate ", ce);
    }
    if (result != null) {
      return result;
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
  public static List<ArrayList<X509Certificate>> verifyAndReturnCertChainAndTrustSTore(
      PKIMessage respPkiMessage,
      CertRepMessage certRepMessage,
      X509Certificate leafCertificate,
      ArrayList<X509Certificate> certChain,
      ArrayList<X509Certificate> trustStore)
      throws CertificateParsingException, IOException, CmpClientException {
    LOG.info("verifying the certificates in the cert chain.");
    certChain.add(leafCertificate);
    addExtraCertsToChain(respPkiMessage, certRepMessage, certChain);
    CmpMessageHelper.verify(certChain);
    ArrayList<ArrayList<X509Certificate>> listOfArray = new ArrayList<>();
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
        certChain.add(
            CmpMessageHelper.getCertfromByteArray(cmpCert.getEncoded(), X509Certificate.class));
      }
    } else {
      final CMPCertificate respCmpCaCert = certRepMessage.getCaPubs()[0];
      certChain.add(
          CmpMessageHelper.getCertfromByteArray(respCmpCaCert.getEncoded(), X509Certificate.class));
    }
  }
}
