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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.bouncycastle.asn1.cmp.PKIMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClient {

  private static final Logger LOG = LoggerFactory.getLogger(HttpClient.class);

  private static final String CONTENT_TYPE = "Content-type";
  private static final String CMP_REQUEST_MIMETYPE = "application/pkixcmp";

  public static byte[] postRequest(
      final PKIMessage pkiMessage,
      final String urlString,
      final String caName,
      final CloseableHttpClient httpClient)
      throws IOException {
    final ByteArrayOutputStream byteArrOutputStream = new ByteArrayOutputStream();
    try {
      final HttpPost postRequest = new HttpPost(urlString);
      final byte[] requestBytes = pkiMessage.getEncoded();

      postRequest.setEntity(new ByteArrayEntity(requestBytes));
      postRequest.setHeader(CONTENT_TYPE, CMP_REQUEST_MIMETYPE);

      try (CloseableHttpResponse response = httpClient.execute(postRequest)) {
        response.getEntity().writeTo(byteArrOutputStream);
      }
      return byteArrOutputStream.toByteArray();
    } catch (IOException ex) {
      LOG.error("Connection error {}, while trying to connect CA {}", ex.getMessage(), caName);
      throw ex;
    }
  }
}
