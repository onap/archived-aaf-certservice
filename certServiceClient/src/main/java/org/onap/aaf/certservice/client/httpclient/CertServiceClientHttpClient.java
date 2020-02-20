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
import org.onap.aaf.certservice.client.exceptions.CertServiceResponseException;
import org.onap.aaf.certservice.client.model.CertServiceResponse;
import org.onap.aaf.certservice.client.model.ErrorCertServiceResponse;

import java.io.IOException;

public class CertServiceClientHttpClient {

  private CertServiceHttpClientProvider certServiceHttpClientProvider;

  private Gson gson = new Gson();

  public CertServiceClientHttpClient(CertServiceHttpClientProvider certServiceHttpClientProvider) {
    this.certServiceHttpClientProvider = certServiceHttpClientProvider;
  }

  public CertServiceResponse sendRequestToCertService(String caName, String CSR, String PK)
      throws CertServiceResponseException {

    CloseableHttpClient httpClient = certServiceHttpClientProvider.getClient();

    HttpGet httpget = getHttpGetMethod(caName, CSR, PK);
    try (httpClient) {
      HttpResponse httpResponse = httpClient.execute(httpget);
      String jsonResponse = getStringResponse(httpResponse.getEntity());
      int responseCode = getStatusCode(httpResponse);
      if (HttpStatus.SC_OK == responseCode) {
        return extractCertServiceResponseFromJson(jsonResponse);
      }
      throw generateErrorResponseException(httpResponse);

    } catch (IOException e) {
      e.printStackTrace(); // TODO logger Wrong connect with CertService
    }

    return null;
  }

  int getStatusCode(HttpResponse httpResponse) {
    return httpResponse.getStatusLine().getStatusCode();
  }

  String getStringResponse(HttpEntity httpEntity) throws IOException {
    return EntityUtils.toString(httpEntity, "UTF-8");
  }

  HttpGet getHttpGetMethod(String caName, String CSR, String PK) {
    String certServiceAddress = certServiceHttpClientProvider.getCertServiceAddress();
    String url = certServiceAddress + caName;
    HttpGet httpget = new HttpGet(url);
    httpget.addHeader("CSR", CSR);
    httpget.addHeader("PK", PK);
    return httpget;
  }

  private CertServiceResponse extractCertServiceResponseFromJson(String jsonResponse) {
    return gson.fromJson(jsonResponse, CertServiceResponse.class);
  }

  private CertServiceResponseException generateErrorResponseException(HttpResponse httpResponse)
      throws IOException {
    String response = getStringResponse(httpResponse.getEntity());
    ErrorCertServiceResponse errorCertServiceResponse =
        gson.fromJson(response, ErrorCertServiceResponse.class);
    return new CertServiceResponseException(errorCertServiceResponse);
  }
}
