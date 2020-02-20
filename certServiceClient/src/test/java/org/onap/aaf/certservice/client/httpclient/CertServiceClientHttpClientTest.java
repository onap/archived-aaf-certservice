/*
 * ============LICENSE_START=======================================================
 * PROJECT
 * ================================================================================
 * Copyright (C) 2020 Nokia. All rights reserved.
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
 * ============LICENSE_END=========================================================
 */

package org.onap.aaf.certservice.client.httpclient;

import org.apache.http.HttpEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.onap.aaf.certservice.client.TestData;
import org.onap.aaf.certservice.client.model.CertServiceResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CertServiceClientHttpClientTest {

  private CertServiceClientHttpClient certServiceClientHttpClient;

  @BeforeEach
  void setUp() {
    String certServiceAddress = "http://localhost:8080/v1/certificate/";
    int timeoutInSeconds = 30;
    CertServiceHttpClientProvider certServiceHttpClientProvider =
        new CertServiceHttpClientProvider(certServiceAddress, timeoutInSeconds);
    certServiceClientHttpClient =
        spy(new CertServiceClientHttpClient(certServiceHttpClientProvider));
  }

  @Test
  void certClientShouldReturnCorrectListsOfCertificatedChainsAndTrustedCertificates()
      throws Exception {

    // given
    Mockito.doReturn(TestData.CORRECT_RESPONSE)
        .when(certServiceClientHttpClient)
        .getStringResponse(any(HttpEntity.class));

    // when
    CertServiceResponse certServiceResponse =
        certServiceClientHttpClient.sendRequestToCertService(
            TestData.CA_NAME, TestData.CSR, TestData.PK);
    List<String> certificateChain = certServiceResponse.getCertificateChain();
    List<String> trustedCertificate = certServiceResponse.getTrustedCertificates();

    // then
    assertNotNull(certServiceResponse);

    assertEquals(certificateChain.size(), 2);
    assertEquals(trustedCertificate.size(), 2);

    assertEquals(TestData.EXPECTED_FIRST_ELEMENT_OF_CERTIFICATE_CHAIN, certificateChain.get(0));
    assertEquals(
        TestData.EXPECTED_FIRST_ELEMENT_OF_TRUSTED_CERTIFICATES, trustedCertificate.get(0));
  }
}
