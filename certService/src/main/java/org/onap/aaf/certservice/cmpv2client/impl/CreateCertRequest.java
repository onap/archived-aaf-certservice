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

import static org.onap.aaf.certservice.cmpv2client.impl.CmpUtil.generatePkiHeader;

import java.io.IOException;
import java.security.KeyPair;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.asn1.cmp.PKIBody;
import org.bouncycastle.asn1.cmp.PKIHeader;
import org.bouncycastle.asn1.cmp.PKIMessage;
import org.bouncycastle.asn1.crmf.AttributeTypeAndValue;
import org.bouncycastle.asn1.crmf.CRMFObjectIdentifiers;
import org.bouncycastle.asn1.crmf.CertReqMessages;
import org.bouncycastle.asn1.crmf.CertReqMsg;
import org.bouncycastle.asn1.crmf.CertRequest;
import org.bouncycastle.asn1.crmf.CertTemplateBuilder;
import org.bouncycastle.asn1.crmf.ProofOfPossession;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the CmpClient Interface conforming to RFC4210 (Certificate Management Protocol
 * (CMP)) and RFC4211 (Certificate Request Message Format (CRMF)) standards.
 */
public class CreateCertRequest {

  private static final Logger LOG = LoggerFactory.getLogger(CreateCertRequest.class);

  private X500Name issuerDn;
  private X500Name subjectDn;
  private List<String> sansList;
  private KeyPair subjectKeyPair;
  private Optional<Date> notBefore;
  private Optional<Date> notAfter;
  private String initAuthPassword;
  private int certReqId;

  public void setIssuerDn(X500Name issuerDn) {
    this.issuerDn = issuerDn;
  }

  public X500Name getIssuerDn() {
    return issuerDn;
  }

  public void setSubjectDn(X500Name subjectDn) {
    this.subjectDn = subjectDn;
  }

  public X500Name getSubjectDn() {
    return subjectDn;
  }

  public void setSansList(List<String> sansList) {
    this.sansList = sansList;
  }

  public void setSubjectKeyPair(KeyPair subjectKeyPair) {
    this.subjectKeyPair = subjectKeyPair;
  }

  public void setNotBefore(Optional<Date> notBefore) {
    this.notBefore = notBefore;
  }

  public void setNotAfter(Optional<Date> notAfter) {
    this.notAfter = notAfter;
  }

  public void setInitAuthPassword(String initAuthPassword) {
    this.initAuthPassword = initAuthPassword;
  }

  public String getInitAuthPassword() {
    return initAuthPassword;
  }

  public int getCertReqId() {
    return certReqId;
  }

  /**
   * Method to create {@link PKIMessage} from {@link CertRequest},{@link ProofOfPossession}, {@link
   * CertReqMsg}, {@link CertReqMessages}, {@link PKIHeader} and {@link PKIBody}.
   *
   * @return {@link PKIMessage}
   */
  public PKIMessage generateCertReq() throws CmpClientException, IOException {
    certReqId = CmpUtil.createRandomInt(Integer.MAX_VALUE);
    final CertTemplateBuilder certTemplateBuilder =
        new CertTemplateBuilder()
            .setIssuer(issuerDn)
            .setSubject(subjectDn)
            .setExtensions(CmpMessageHelper.generateExtension(sansList))
            .setValidity(CmpMessageHelper.generateOptionalValidity(notBefore, notAfter))
            .setPublicKey(
                SubjectPublicKeyInfo.getInstance(subjectKeyPair.getPublic().getEncoded()));

    final CertRequest certRequest = new CertRequest(certReqId, certTemplateBuilder.build(), null);
    final ProofOfPossession proofOfPossession =
        CmpMessageHelper.generateProofOfPossession(certRequest, subjectKeyPair);

    final AttributeTypeAndValue[] attrTypeVal = {
      new AttributeTypeAndValue(
          CRMFObjectIdentifiers.id_regCtrl_regToken, new DERUTF8String(initAuthPassword))
    };

    final CertReqMsg certReqMsg = new CertReqMsg(certRequest, proofOfPossession, attrTypeVal);
    final CertReqMessages certReqMessages = new CertReqMessages(certReqMsg);

    final PKIHeader pkiHeader =
        generatePkiHeader(subjectDn, issuerDn, CmpMessageHelper.protectionAlgoIdentifier());
    final PKIBody pkiBody = new PKIBody(PKIBody.TYPE_CERT_REQ, certReqMessages);

    return CmpMessageHelper.protectPkiMessage(pkiHeader, pkiBody, initAuthPassword);
  }
}
