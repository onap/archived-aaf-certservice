/*
 * Copyright (C) 2019 Ericsson Software Technology AB. All rights reserved.
 *
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
 * limitations under the License
 */

package org.onap.aaf.certservice.cmpv2client.impl;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Optional;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.bouncycastle.asn1.cmp.PKIMessage;
import org.onap.aaf.certservice.cmpv2client.api.CmpClient;
import org.onap.aaf.certservice.cmpv2client.external.CSRMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the CmpClient Interface conforming to RFC4210 (Certificate Management Protocol
 * (CMP)) and RFC4211 (Certificate Request Message Format (CRMF)) standards.
 */
public class CmpClientImpl implements CmpClient {

  private final Logger LOG = LoggerFactory.getLogger(CmpClientImpl.class);

  private static final String DEFAULT_PROFILE = "RA";
  private static final String DEFAULT_CA_NAME = "Certification Authority";

  private String CA_Name;
  private String CA_Profile;

  /**
   * Validate inputs for Certificate Creation.
   *
   * @param csrMeta CSRMeta Object containing variables for creating a Certificate Request.
   * @param cert Certificate object needed to validate response from CA server.
   * @param caName Date specifying certificate is not valid before this date.
   * @param profile Date specifying certificate is not valid after this date.
   * @throws IllegalArgumentException if Before Date is set after the After Date.
   */
  private void validate(
      final CSRMeta csrMeta,
      final X509Certificate cert,
      final String caName,
      final String profile,
      final Optional<Date> notBefore,
      final Optional<Date> notAfter)
      throws IllegalArgumentException {

    CA_Name = CmpUtil.isNullOrEmpty(caName) ? caName : DEFAULT_CA_NAME;
    CA_Profile = CmpUtil.isNullOrEmpty(profile) ? profile : DEFAULT_PROFILE;
    LOG.info(
        "Validate before creating Certificate Request for CA :{} in Mode {} ", CA_Name, CA_Profile);

    CmpUtil.notNull(csrMeta, "CSRMeta Instance");
    CmpUtil.notNull(csrMeta.x500Name(), "Subject DN");
    CmpUtil.notNull(csrMeta.issuerx500Name(), "Issuer DN");
    CmpUtil.notNull(csrMeta.password(), "IAK/RV Password");
    CmpUtil.notNull(cert, "Certificate Signing Request (CSR)");
    CmpUtil.notNull(csrMeta.caUrl(), "External CA URL");
    CmpUtil.notNull(csrMeta.keypair(), "Subject KeyPair");

    if (notBefore.isPresent()
        && notAfter.isPresent()
        && notBefore.get().compareTo(notAfter.get()) > 0) {
      throw new IllegalArgumentException("Before Date is set after the After Date");
    }
  }

  @Override
  public X509Certificate createCertRequest(
      String caName,
      String profile,
      CSRMeta csrMeta,
      X509Certificate cert,
      Optional<Date> notBefore,
      Optional<Date> notAfter)
      throws CmpClientException, PKIErrorException, IOException {
    // Validate inputs for Certificate Request
    validate(csrMeta, cert, caName, profile, notBefore, notAfter);

    final CreateCertRequest certRequest =
        CmpMessageBuilder.of(CreateCertRequest::new)
            .with(CreateCertRequest::setIssuerDn, csrMeta.issuerx500Name())
            .with(CreateCertRequest::setSubjectDn, csrMeta.x500Name())
            .with(CreateCertRequest::setSansList, csrMeta.sans())
            .with(CreateCertRequest::setSubjectKeyPair, csrMeta.keyPair())
            .with(CreateCertRequest::setNotBefore, notBefore)
            .with(CreateCertRequest::setNotAfter, notAfter)
            .with(CreateCertRequest::setInitAuthPassword, csrMeta.password())
            .build();

    final PKIMessage pkiMessage = certRequest.generateCertReq();
    final byte[] respBytes =
        HttpClient.postRequest(pkiMessage, csrMeta.caUrl(), caName, createCloseableHttpClient());
    CmpUtil.notNull(respBytes.length, "Response body empty, Server response");
    final PKIMessage respPkiMessage = PKIMessage.getInstance(respBytes);
    return null;
  }

  public CloseableHttpClient createCloseableHttpClient() {
    return HttpClients.createDefault();
  }

  @Override
  public X509Certificate revokeCertRequest(
      String caName, X509Certificate cert, int reason, Date invalidityTime)
      throws CmpClientException, PKIErrorException, IOException {
    // TODO Auto-generated method stub
    return null;
  }
}
