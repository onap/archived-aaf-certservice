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
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.onap.aaf.certservice.client.model.CertServiceResponse;

import java.io.IOException;

public class CertServiceClientHttpClient {

    private final String CERT_SERVICE_ENDPOINT = "/v1/certificate/";

    private  String certServiceAddress;
    private  int timeoutInSecond;

    private Gson gson = new Gson();


    public CertServiceClientHttpClient(String certServiceAddress, int timeoutInSeconds){
        this.certServiceAddress = certServiceAddress;
        this.timeoutInSecond = timeoutInSeconds;
    }

    public CertServiceResponse sendRequestToCertService(String caName, String CSR, String PK) {

        CloseableHttpClient httpClient = getHttpClient();

        HttpGet httpget = getHttpGetMethod(caName, CSR, PK);
        try {
            HttpResponse httpResponse = httpClient.execute(httpget);
            String jsonResponse = getStringResponse(httpResponse);
            int responseCode = httpResponse.getStatusLine().getStatusCode();
            if(HttpStatus.SC_OK == responseCode){
                return extractCertServiceResponseFromJson(jsonResponse);
            } else {
                //TODO What is expected result ?
            }

        } catch (IOException e) {
            e.printStackTrace(); //TODO logger Wrong connect with CertService
        }

        return null;
    }

    private String getStringResponse(HttpResponse httpResponse) throws IOException {
        HttpEntity httpEntity = httpResponse.getEntity();
        return EntityUtils.toString(httpEntity, "UTF-8");
    }

    private CertServiceResponse extractCertServiceResponseFromJson(String jsonResponse) {
        return gson.fromJson(jsonResponse, CertServiceResponse.class);
    }

    private CloseableHttpClient getHttpClient() {
        int configTimeout = timeoutInSecond * 1000 ;
        RequestConfig config = RequestConfig.custom()
                .setConnectionRequestTimeout(configTimeout)
                .build();
        return HttpClientBuilder.create()
                .setDefaultRequestConfig(config)
                .build();
    }

    private HttpGet getHttpGetMethod(String caName, String CSR, String PK) {
        String url = certServiceAddress + CERT_SERVICE_ENDPOINT + caName;
        HttpGet httpget = new HttpGet(url);
        httpget.addHeader("CSR", CSR);
        httpget.addHeader("PK", PK);
        return httpget;
    }

}
