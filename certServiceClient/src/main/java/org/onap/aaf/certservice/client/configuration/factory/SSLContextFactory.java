/*============LICENSE_START=======================================================
 * aaf-certservice-client
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

package org.onap.aaf.certservice.client.configuration.factory;

import org.apache.http.ssl.SSLContexts;
import org.onap.aaf.certservice.client.configuration.exception.ClientConfigurationException;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public class SSLContextFactory {
    private static final String TRUSTSTORE_PATH = "/etc/onap/aaf/certservice/certs/truststore.jks";
    private static final String KEYSTORE_PATH = "/etc/onap/aaf/certservice/certs/certServiceClient-keystore.jks";
    private static final String CERT_PASSWORD = "secret";

    public SSLContext create() throws ClientConfigurationException {
        SSLContext sslContext = null;
        try {
            KeyStore identityKeyStore = setupKeyStore(KEYSTORE_PATH, CERT_PASSWORD);
            KeyStore trustKeyStore = setupKeyStore(TRUSTSTORE_PATH, CERT_PASSWORD);

            sslContext = SSLContexts.custom()
                    .loadKeyMaterial(identityKeyStore, CERT_PASSWORD.toCharArray())
                    .loadTrustMaterial(trustKeyStore, null)
                    .build();
        } catch (Exception e) {
            throw new ClientConfigurationException("TLS configuration exception: " + e);
        }

        return sslContext;
    }

    private static KeyStore setupKeyStore(String keystorePath, String certPassword)
            throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        KeyStore keyStore = KeyStore.getInstance("jks");
        FileInputStream identityKeyStoreFile = new FileInputStream(new File(
                keystorePath));
        keyStore.load(identityKeyStoreFile, certPassword.toCharArray());
        return keyStore;
    }
}
