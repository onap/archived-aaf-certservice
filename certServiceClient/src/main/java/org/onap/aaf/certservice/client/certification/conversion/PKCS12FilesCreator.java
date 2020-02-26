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
import org.onap.aaf.certservice.client.certification.exception.PemToPKCS12ConverterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PKCS12FilesCreator {

    private final String keystoreJksPath;
    private final String keystorePassPath;
    private final String truststoreJksPath;
    private final String truststorePassPath;
    private final Logger LOGGER = LoggerFactory.getLogger(PKCS12FilesCreator.class);


    public PKCS12FilesCreator(String path) {
        keystoreJksPath = path + "keystore.jks";
        keystorePassPath = path + "keystore.pass";
        truststoreJksPath = path + "truststore.jks";
        truststorePassPath = path + "truststore.pass";
    }

    public void saveKeystoreData(byte[] keystoreData, String keystorePassword) throws PemToPKCS12ConverterException {
        LOGGER.debug("Creating PKCS12 keystore files and saving data.");

        saveDataToLocation(keystoreData, keystoreJksPath);
        saveDataToLocation(keystorePassword.getBytes(), keystorePassPath);
    }

    public void saveTruststoreData(byte[] truststoreData, String truststorePassword)
        throws PemToPKCS12ConverterException {
        LOGGER.debug("Creating PKCS12 truststore files and saving data.");

        saveDataToLocation(truststoreData, truststoreJksPath);
        saveDataToLocation(truststorePassword.getBytes(), truststorePassPath);
    }

    private void saveDataToLocation(byte[] data, String path) throws PemToPKCS12ConverterException {
        try (FileOutputStream fos = new FileOutputStream(path)){
            fos.write(data);
        } catch (IOException e) {
            LOGGER.debug("PKCS12 files creation failed, cause: " + e.getMessage());
            throw new PemToPKCS12ConverterException(e);
        }
    }
}
