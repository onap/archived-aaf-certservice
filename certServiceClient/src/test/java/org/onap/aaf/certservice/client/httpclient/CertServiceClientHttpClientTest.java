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
import org.onap.aaf.certservice.client.TestData;
import org.onap.aaf.certservice.client.httpclient.exceptions.CertServiceResponseException;
import org.onap.aaf.certservice.client.httpclient.model.CertServiceResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class CertServiceClientHttpClientTest {

  private CertServiceClientHttpClient certServiceClientHttpClient;
  private CloseableHttpClient httpClient;
  private HttpEntity httpEntity;
  private StatusLine statusLine;
  private CloseableHttpResponse httpResponse;

  @BeforeEach
  void setUp() {

    httpClient = mock(CloseableHttpClient.class);
    httpEntity = mock(HttpEntity.class);
    statusLine = mock(StatusLine.class);
    httpResponse = mock(CloseableHttpResponse.class);

    CertServiceHttpClientProvider certServiceHttpClientProvider =
        mock(CertServiceHttpClientProvider.class);
    when(certServiceHttpClientProvider.getClient()).thenReturn(httpClient);
    when(certServiceHttpClientProvider.getCertServiceAddress()).thenReturn("");

    certServiceClientHttpClient =
        spy(new CertServiceClientHttpClient(certServiceHttpClientProvider));
  }

  @Test
  void shouldReturnCorrectListsOfCertificatedChainsAndTrustedCertificatesWhenRequestDataIsCorrect()
      throws Exception {

    // given
    mockServerResponse(HttpURLConnection.HTTP_OK, TestData.CORRECT_RESPONSE);

    // when
    CertServiceResponse certServiceResponse =
        certServiceClientHttpClient.getCertServiceData(TestData.CA_NAME, TestData.CSR, TestData.PK);
    List<String> certificateChain = certServiceResponse.getCertificateChain();
    List<String> trustedCertificate = certServiceResponse.getTrustedCertificates();

    // then
    assertNotNull(certServiceResponse);

    assertEquals(2, certificateChain.size());
    assertEquals(2, trustedCertificate.size());

    assertEquals(TestData.EXPECTED_FIRST_ELEMENT_OF_CERTIFICATE_CHAIN, certificateChain.get(0));
    assertEquals(
        TestData.EXPECTED_FIRST_ELEMENT_OF_TRUSTED_CERTIFICATES, trustedCertificate.get(0));
  }

  @Test
  void shouldThrowCertServiceResponseExceptionWhenPkHeaderIsMissing() throws Exception {

    // given
    mockServerResponse(HttpURLConnection.HTTP_BAD_REQUEST, TestData.MISSING_PK_RESPONSE);

    // when
    CertServiceResponseException exception =
        assertThrows(
            CertServiceResponseException.class,
            () -> {
              certServiceClientHttpClient.getCertServiceData(TestData.CA_NAME, TestData.CSR, "");
            });

    // then
    int responseCode = exception.getResponseCode();
    assertEquals(HttpURLConnection.HTTP_BAD_REQUEST, responseCode);
  }

  private void mockServerResponse(int serverCodeResponse, String stringResponse)
      throws IOException {
    when(statusLine.getStatusCode()).thenReturn(serverCodeResponse);
    when(httpResponse.getStatusLine()).thenReturn(statusLine);
    when(httpResponse.getEntity()).thenReturn(httpEntity);
    when(httpClient.execute(any(HttpGet.class))).thenReturn(httpResponse);

    when(httpEntity.getContent()).thenReturn(new ByteArrayInputStream(stringResponse.getBytes()));
  }
}
