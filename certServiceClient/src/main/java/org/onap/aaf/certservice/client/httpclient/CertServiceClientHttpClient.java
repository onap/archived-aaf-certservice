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

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.onap.aaf.certservice.client.httpclient.exceptions.CertServiceResponseException;
import org.onap.aaf.certservice.client.httpclient.model.CertServiceResponse;
import org.onap.aaf.certservice.client.httpclient.model.ErrorCertServiceResponse;

import java.io.IOException;
import java.util.Collections;

public class CertServiceClientHttpClient {

  private static final String CSR_HEADER_NAME = "CSR";
  private static final String PK_HEADER_NAME = "PK";
  private static final String CHARSET_UTF_8 = "UTF-8";

  private final CertServiceHttpClientProvider certServiceHttpClientProvider;

  private final Gson gson = new Gson();

  public CertServiceClientHttpClient(CertServiceHttpClientProvider certServiceHttpClientProvider) {
    this.certServiceHttpClientProvider = certServiceHttpClientProvider;
  }

  public CertServiceResponse getCertServiceData(String caName, String csr, String pk)
      throws CertServiceResponseException {

    CloseableHttpClient httpClient = certServiceHttpClientProvider.getClient();

    try (httpClient) {
      HttpResponse httpResponse =
          httpClient.execute(
              createHttpPayload(
                  caName, csr, pk, certServiceHttpClientProvider.getCertServiceAddress()));
      String jsonResponse = getStringResponse(httpResponse.getEntity());
      int responseCode = getStatusCode(httpResponse);
      if (HttpStatus.SC_OK == responseCode) {
        return extractCertServiceResponseFromJson(jsonResponse);
      }
      throw generateErrorResponseException(jsonResponse);

    } catch (IOException e) {
      e.printStackTrace(); // TODO logger Wrong connect with CertService
    }

    return new CertServiceResponse(Collections.emptyList(), Collections.emptyList());
  }

  int getStatusCode(HttpResponse httpResponse) {
    return httpResponse.getStatusLine().getStatusCode();
  }

  String getStringResponse(HttpEntity httpEntity) throws IOException {
    return EntityUtils.toString(httpEntity, CHARSET_UTF_8);
  }

  HttpGet createHttpPayload(String caName, String csr, String pk, String certServiceAddress) {
    String url = certServiceAddress + caName;
    HttpGet httpget = new HttpGet(url);
    httpget.addHeader(CSR_HEADER_NAME, csr);
    httpget.addHeader(PK_HEADER_NAME, pk);
    return httpget;
  }

  private CertServiceResponse extractCertServiceResponseFromJson(String jsonResponse) {
    return gson.fromJson(jsonResponse, CertServiceResponse.class);
  }

  private CertServiceResponseException generateErrorResponseException(String jsonResponse) {
    ErrorCertServiceResponse errorCertServiceResponse =
        gson.fromJson(jsonResponse, ErrorCertServiceResponse.class);
    return new CertServiceResponseException(errorCertServiceResponse);
  }
}
