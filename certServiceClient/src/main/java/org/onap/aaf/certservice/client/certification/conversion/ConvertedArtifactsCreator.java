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
import org.onap.aaf.certservice.client.certification.exception.CertFileWriterException;
import org.onap.aaf.certservice.client.certification.exception.PemConversionException;
import org.onap.aaf.certservice.client.certification.writer.CertFileWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConvertedArtifactsCreator implements ArtifactsCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConvertedArtifactsCreator.class);
    private static final String CERTIFICATE_ALIAS = "certificate";
    private static final String TRUSTED_CERTIFICATE_ALIAS = "trusted-certificate-";
    private static final int PASSWORD_LENGTH = 24;
    private final String KEYSTORE = "keystore";
    private final String TRUSTSTORE = "truststore";
    private String PASS_EXT = ".pass";
    private final String fileExtension;
    private final RandomPasswordGenerator generator;
    private final PemConverter converter;
    private final CertFileWriter creator;

    public ConvertedArtifactsCreator(CertFileWriter creator, RandomPasswordGenerator generator,
                                     PemConverter converter, String fileExtension) {
        this.generator = generator;
        this.converter = converter;
        this.creator = creator;
        this.fileExtension = fileExtension;
    }

    @Override
    public void create(List<String> keystoreData, List<String> truststoreData, PrivateKey privateKey)
        throws PemConversionException, CertFileWriterException {
        createKeystore(keystoreData,privateKey);
        createTruststore(truststoreData);
    }

    private void createKeystore(List<String> data, PrivateKey privateKey)
        throws PemConversionException, CertFileWriterException {
        Password password = generator.generate(PASSWORD_LENGTH);
        String keystoreArtifactName = KEYSTORE + fileExtension;
        String keystorePass = KEYSTORE + PASS_EXT;

        LOGGER.debug("Attempt to create keystore files and saving data. File names: {}, {}", keystoreArtifactName, keystorePass);

        creator.saveData(converter.convertKeystore(data, password, CERTIFICATE_ALIAS, privateKey), keystoreArtifactName);
        creator.saveData(getPasswordAsBytes(password), keystorePass);
    }

    private void createTruststore(List<String> data)
        throws PemConversionException, CertFileWriterException {
        Password password = generator.generate(PASSWORD_LENGTH);
        String truststoreArtifactName = TRUSTSTORE + fileExtension;
        String truststorePass = TRUSTSTORE + PASS_EXT;

        LOGGER.debug("Attempt to create truststore files and saving data. File names: {}, {}", truststoreArtifactName, truststorePass);

        creator.saveData(converter.convertTruststore(data, password, TRUSTED_CERTIFICATE_ALIAS), truststoreArtifactName);
        creator.saveData(getPasswordAsBytes(password), truststorePass);
    }

    String getFileExtension() {
        return fileExtension;
    }

    private byte[] getPasswordAsBytes(Password password) {
        return password.getCurrentPassword().getBytes();
    }
}
