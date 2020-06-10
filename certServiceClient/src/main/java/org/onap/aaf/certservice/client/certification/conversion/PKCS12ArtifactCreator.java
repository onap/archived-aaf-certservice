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

import java.security.PrivateKey;
import java.util.List;
import org.onap.aaf.certservice.client.certification.exception.PemToPKCS12ConverterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PKCS12ArtifactCreator implements ArtifactCreator{
    private static final Logger LOGGER = LoggerFactory.getLogger(PKCS12ArtifactCreator.class);
    private static final String CERTIFICATE_ALIAS = "certificate";
    private static final String TRUSTED_CERTIFICATE_ALIAS = "trusted-certificate-";
    private static final int PASSWORD_LENGTH = 24;
    private static final String KEYSTORE_P12 = "keystore.p12";
    private static final String KEYSTORE_PASS = "keystore.pass";
    private static final String TRUSTSTORE_P12 = "truststore.p12";
    private static final String TRUSTSTORE_PASS = "truststore.pass";
    private final RandomPasswordGenerator generator;
    private final PemToPKCS12Converter converter;
    private final PKCS12FilesCreator creator;

    public PKCS12ArtifactCreator(PKCS12FilesCreator creator, RandomPasswordGenerator generator,
                                 PemToPKCS12Converter converter) {
        this.generator = generator;
        this.converter = converter;
        this.creator = creator;
    }

    public void createKeystore(List<String> data, PrivateKey privateKey)
        throws PemToPKCS12ConverterException {
        Password password = generator.generate(PASSWORD_LENGTH);

        LOGGER.debug("Attempt to create PKCS12 keystore files and saving data. File names: {}, {}", KEYSTORE_P12, KEYSTORE_PASS);

        creator.saveDataToLocation(converter.convertKeystore(data, password, CERTIFICATE_ALIAS, privateKey), KEYSTORE_P12);
        creator.saveDataToLocation(password.getCurrentPassword().getBytes(), KEYSTORE_PASS);
    }

    public void createTruststore(List<String> data)
        throws PemToPKCS12ConverterException {
        Password password = generator.generate(PASSWORD_LENGTH);

        LOGGER.debug("Attempt to create PKCS12 truststore files and saving data. File names: {}, {}", TRUSTSTORE_P12, TRUSTSTORE_PASS);

        creator.saveDataToLocation(converter.convertTruststore(data, password, TRUSTED_CERTIFICATE_ALIAS), TRUSTSTORE_P12);
        creator.saveDataToLocation(password.getCurrentPassword().getBytes(), TRUSTSTORE_PASS);
    }

    @Override
    public void generateArtifacts(List<String> keystoreData, List<String> truststoreData, PrivateKey privateKey) throws PemToPKCS12ConverterException {
        createKeystore(keystoreData,privateKey);
        createTruststore(truststoreData);
    }
}
