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

package org.onap.aaf.certservice.cmpv2client.api;

import java.util.Date;

import org.onap.aaf.certservice.certification.configuration.model.Cmpv2Server;
import org.onap.aaf.certservice.certification.model.CsrModel;
import org.onap.aaf.certservice.cmpv2client.exceptions.CmpClientException;
import org.onap.aaf.certservice.cmpv2client.model.Cmpv2CertificationModel;

/**
 * This class represent CmpV2Client Interface for obtaining X.509 Digital Certificates in a Public
 * Key Infrastructure (PKI), making use of Certificate Management Protocol (CMPv2) operating on
 * newest version: cmp2000(2).
 */
public interface CmpClient {

  /**
   * Requests for a External Root CA Certificate to be created for the passed public keyPair wrapped
   * in a CSRMeta with common details, accepts self-signed certificate. Basic Authentication using
   * IAK/RV, Verification of the signature (proof-of-possession) on the request is performed and an
   * Exception thrown if verification fails or issue encountered in fetching certificate from CA.
   *
   * @param csrModel  Certificate Signing Request model. Must not be {@code null}.
   * @param server    CMPv2 Server. Must not be {@code null}.
   * @param notBefore An optional validity to set in the created certificate, Certificate not valid
   *                  before this date.
   * @param notAfter  An optional validity to set in the created certificate, Certificate not valid
   *                  after this date.
   * @return model for certification containing certificate chain and trusted certificates
   * @throws CmpClientException if client error occurs.
   */
  Cmpv2CertificationModel createCertificate(
      CsrModel csrModel,
      Cmpv2Server server,
      Date notBefore,
      Date notAfter)
      throws CmpClientException;

  /**
   * Requests for a External Root CA Certificate to be created for the passed public keyPair wrapped
   * in a CSRMeta with common details, accepts self-signed certificate. Basic Authentication using
   * IAK/RV, Verification of the signature (proof-of-possession) on the request is performed and an
   * Exception thrown if verification fails or issue encountered in fetching certificate from CA.
   *
   * @param csrModel  Certificate Signing Request Model. Must not be {@code null}.
   * @param server    CMPv2 server. Must not be {@code null}.
   * @return model for certification containing certificate chain and trusted certificates
   * @throws CmpClientException if client error occurs.
   */
  Cmpv2CertificationModel createCertificate(
      CsrModel csrModel,
      Cmpv2Server server)
      throws CmpClientException;
}