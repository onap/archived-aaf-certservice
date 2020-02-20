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
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.onap.aaf.certservice.client.TestData;
import org.onap.aaf.certservice.client.exceptions.CertServiceResponseException;
import org.onap.aaf.certservice.client.model.CertServiceResponse;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CertServiceClientHttpClientTest {

  private CertServiceClientHttpClient certServiceClientHttpClient;
  private HttpGet httpGetMethod;
  private CloseableHttpClient httpClient;
  private HttpEntity httpEntity;
  private StatusLine statusLine;
  private CloseableHttpResponse httpResponse;

  @BeforeEach
  void setUp() {

    httpClient = mock(CloseableHttpClient.class);
    httpEntity = mock(HttpEntity.class);
    statusLine = mock(StatusLine.class);
    httpGetMethod = mock(HttpGet.class);
    httpResponse = mock(CloseableHttpResponse.class);

    CertServiceHttpClientProvider certServiceHttpClientProvider =
        mock(CertServiceHttpClientProvider.class);
    when(certServiceHttpClientProvider.getClient()).thenReturn(httpClient);

    certServiceClientHttpClient =
        spy(new CertServiceClientHttpClient(certServiceHttpClientProvider));
  }

  @Test
  void certClientShouldReturnCorrectListsOfCertificatedChainsAndTrustedCertificates()
      throws Exception {

    mockServerResponse(200, TestData.CORRECT_RESPONSE);

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

  @Test
  void certClientShouldReturn400ErrorWhenPKisMissing() throws Exception {

    mockServerResponse(400, TestData.MISSING_PK_RESPONSE);

    // when
    CertServiceResponseException exception =
        assertThrows(
            CertServiceResponseException.class,
            () -> {
              certServiceClientHttpClient.sendRequestToCertService(
                  TestData.CA_NAME, TestData.CSR, "");
            });

    // then
    int responseCode = exception.getResponseCode();
    assertEquals(400, responseCode);
  }

  private void mockServerResponse(int serverCodeResponse, String correctResponse)
      throws IOException {
    when(statusLine.getStatusCode()).thenReturn(serverCodeResponse);
    when(httpResponse.getStatusLine()).thenReturn(statusLine);
    when(httpResponse.getEntity()).thenReturn(httpEntity);
    when(httpClient.execute(httpGetMethod)).thenReturn(httpResponse);

    // given
    Mockito.doReturn(correctResponse)
        .when(certServiceClientHttpClient)
        .getStringResponse(any(HttpEntity.class));

    Mockito.doReturn(httpGetMethod)
        .when(certServiceClientHttpClient)
        .getHttpGetMethod(anyString(), anyString(), anyString());
  }
}
