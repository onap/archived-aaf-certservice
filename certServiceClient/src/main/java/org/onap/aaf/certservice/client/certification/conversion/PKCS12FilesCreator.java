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

package org.onap.aaf.certservice.client.certification.conversion;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

import org.onap.aaf.certservice.client.certification.exception.PemToPKCS12ConverterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PKCS12FilesCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(PKCS12FilesCreator.class);

    private static final String KEYSTORE_JKS = "keystore.jks";
    private static final String KEYSTORE_PASS = "keystore.pass";
    private static final String TRUSTSTORE_JKS = "truststore.jks";
    private static final String TRUSTSTORE_PASS = "truststore.pass";

    private static final String KEY_PEM = "key.pem";
    private static final String KEYSTORE_PEM = "keystore.pem";
    private static final String TRUSTSTORE_PEM = "truststore.pem";

    private final String keystoreJksPath;
    private final String keystorePassPath;
    private final String truststoreJksPath;
    private final String truststorePassPath;

    private final String keyPemPath;
    private final String keystorePemPath;
    private final String truststorePemPath;


    PKCS12FilesCreator(String path) {
        keystoreJksPath = Path.of(path, KEYSTORE_JKS).toString();
        keystorePassPath = Path.of(path, KEYSTORE_PASS).toString();
        truststoreJksPath = Path.of(path, TRUSTSTORE_JKS).toString();
        truststorePassPath = Path.of(path, TRUSTSTORE_PASS).toString();
        keyPemPath = Path.of(path, KEY_PEM).toString();
        keystorePemPath = Path.of(path, KEYSTORE_PEM).toString();
        truststorePemPath = Path.of(path, TRUSTSTORE_PEM).toString();
    }

    void saveKeystoreData(byte[] keystoreData, String keystorePassword) throws PemToPKCS12ConverterException {
        LOGGER.debug("Attempt to create PKCS12 keystore files and saving data. Keystore path: {}", keystoreJksPath);

        saveDataToLocation(keystoreData, keystoreJksPath);
        saveDataToLocation(keystorePassword.getBytes(), keystorePassPath);
    }

    void saveTruststoreData(byte[] truststoreData, String truststorePassword)
        throws PemToPKCS12ConverterException {
        LOGGER.debug("Attempt to create PKCS12 truststore files and saving data. Truststore path: {}", truststoreJksPath);

        saveDataToLocation(truststoreData, truststoreJksPath);
        saveDataToLocation(truststorePassword.getBytes(), truststorePassPath);
    }

    void savePemArtifacts(byte[] keystore, byte[] truststore, byte[] privateKey) throws PemToPKCS12ConverterException {
        LOGGER.debug("Attempt to create PEM private key file and saving data. Private key path: {}", keyPemPath);
        saveDataToLocation(privateKey, keyPemPath);

        LOGGER.debug("Attempt to create PEM truststore file and saving data. Truststore path: {}", truststorePemPath);
        saveDataToLocation(keystore, keystorePemPath);

        LOGGER.debug("Attempt to create PEM keystore file and saving data. Keystore path: {}", keystorePemPath);
        saveDataToLocation(truststore, truststorePemPath);
    }

    private void saveDataToLocation(byte[] data, String path) throws PemToPKCS12ConverterException {
        try (FileOutputStream fos = new FileOutputStream(path)) {
            fos.write(data);
        } catch (IOException e) {
            LOGGER.error("Certificates artifacts creation failed, exception message: {}", e.getMessage());
            throw new PemToPKCS12ConverterException(e);
        }
    }
}
