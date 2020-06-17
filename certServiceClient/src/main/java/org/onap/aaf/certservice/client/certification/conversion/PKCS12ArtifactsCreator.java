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

import org.onap.aaf.certservice.client.certification.exception.PemToPKCS12ConverterException;

import java.security.PrivateKey;
import java.util.List;

public class PKCS12ArtifactsCreator implements ArtifactsCreator {

    private static final String CERTIFICATE_ALIAS = "certificate";
    private static final String TRUSTED_CERTIFICATE_ALIAS = "trusted-certificate-";
    private static final int PASSWORD_LENGTH = 24;
    private final RandomPasswordGenerator passwordGenerator;
    private final PemToPKCS12Converter certConverter;
    private final PKCS12FilesCreator filesCreator;

    PKCS12ArtifactsCreator(PKCS12FilesCreator filesCreator, RandomPasswordGenerator passwordGenerator,
                           PemToPKCS12Converter certConverter) {
        this.passwordGenerator = passwordGenerator;
        this.certConverter = certConverter;
        this.filesCreator = filesCreator;
    }

    @Override
    public void create(List<String> keystoreData, List<String> truststoreData, PrivateKey privateKey) throws PemToPKCS12ConverterException {
        createKeystore(keystoreData, privateKey);
        createTruststore(truststoreData);
    }

    private void createKeystore(List<String> data, PrivateKey privateKey)
            throws PemToPKCS12ConverterException {
        Password password = passwordGenerator.generate(PASSWORD_LENGTH);
        filesCreator.saveKeystoreData(certConverter.convertKeystore(data, password, CERTIFICATE_ALIAS, privateKey),
                password.getCurrentPassword());
    }

    private void createTruststore(List<String> data)
            throws PemToPKCS12ConverterException {
        Password password = passwordGenerator.generate(PASSWORD_LENGTH);
        filesCreator.saveTruststoreData(certConverter.convertTruststore(data, password, TRUSTED_CERTIFICATE_ALIAS),
                password.getCurrentPassword());
    }
}
