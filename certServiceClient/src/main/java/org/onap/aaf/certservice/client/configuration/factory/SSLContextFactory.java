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
import org.onap.aaf.certservice.client.configuration.EnvsForTls;
import org.onap.aaf.certservice.client.configuration.TlsConfigurationEnvs;
import org.onap.aaf.certservice.client.configuration.exception.ClientConfigurationException;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class SSLContextFactory {

    private EnvsForTls envsForTls;

    public SSLContextFactory(EnvsForTls envsForTls) {
        this.envsForTls = envsForTls;
    }

    public SSLContext create() throws ClientConfigurationException {
        String keystorePath = envsForTls.getKeystorePath()
                .orElseThrow(() -> new ClientConfigurationException(createEnvMissingMessage(TlsConfigurationEnvs.KEYSTORE_PATH)));
        String keystorePassword = envsForTls.getKeystorePassword()
                .orElseThrow(() -> new ClientConfigurationException(createEnvMissingMessage(TlsConfigurationEnvs.KEYSTORE_PASSWORD)));
        String truststorePath = envsForTls.getTruststorePath()
                .orElseThrow(() -> new ClientConfigurationException(createEnvMissingMessage(TlsConfigurationEnvs.TRUSTSTORE_PATH)));
        String truststorePassword = envsForTls.getTruststorePassword()
                .orElseThrow(() -> new ClientConfigurationException(createEnvMissingMessage(TlsConfigurationEnvs.TRUSTSTORE_PASSWORD)));

        SSLContext sslContext = null;
        try {
            KeyStore identityKeystore = setupKeystore(keystorePath, keystorePassword);
            KeyStore trustKeystore = setupKeystore(truststorePath, truststorePassword);

            sslContext = SSLContexts.custom()
                    .loadKeyMaterial(identityKeystore, keystorePassword.toCharArray())
                    .loadTrustMaterial(trustKeystore, null)
                    .build();
        } catch (Exception e) {
            throw new ClientConfigurationException("TLS configuration exception: " + e);
        }

        return sslContext;
    }

    private String createEnvMissingMessage(TlsConfigurationEnvs keystorePath) {
        return keystorePath + " env is missing.";
    }

    private KeyStore setupKeystore(String keystorePath, String certPassword)
            throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        KeyStore keyStore = KeyStore.getInstance("jks");
        FileInputStream identityKeyStoreFile = new FileInputStream(new File(
                keystorePath));
        keyStore.load(identityKeyStoreFile, certPassword.toCharArray());
        return keyStore;
    }
}
