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

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.onap.aaf.certservice.client.certification.EncryptionAlgorithmConstants;
import org.onap.aaf.certservice.client.certification.KeyPairFactory;

class PKCS12FilesCreatorTest {

    private final String RESOURCES_PATH = "src/test/resources";
    private final String CERT1_PATH = RESOURCES_PATH + "/cert1.pem";
    private final String CERT2_PATH = RESOURCES_PATH + "/cert2.pem";

    @Test
    void saveKeystoreData() throws IOException {
//        List<String> keystoreCerts = getCertificateChain();
//        List<String> truststoreCerts = getCertificateChain();
//        PKCS12FilesCreator creator = new PKCS12FilesCreator("src/test/resources/");
//
//        final PrivateKey key = new KeyPairFactory(EncryptionAlgorithmConstants.RSA_ENCRYPTION_ALGORITHM,
//            EncryptionAlgorithmConstants.KEY_SIZE)
//            .create()
//            .getPrivate();
//
//        PemToPKCS12Converter converter = new PemToPKCS12Converter(key);
//        RandomPasswordGenerator generator = new RandomPasswordGenerator();
//        String keystorepass = generator.generate(24);
//        String truststorepass = generator.generate(24);
//
//        byte[] keystoreData = converter.convertKeystore(keystoreCerts, keystorepass, "keystoreEntry");
//        byte[] truststoreData = converter.convertTruststore(truststoreCerts, truststorepass, "trustedCert");
//
//        creator.saveKeystoreData(keystoreData, keystorepass);
//        creator.saveTruststoreData(truststoreData, truststorepass);
    }

    @Test
    void saveTruststoreData() {
    }

    private List<String> getCertificateChain() throws IOException {
//        return new ArrayList<>();
        return Arrays.asList(new String[]{
            Files.readString(
                Path.of(CERT1_PATH), StandardCharsets.UTF_8),
            Files.readString(
                Path.of(CERT2_PATH), StandardCharsets.UTF_8)
        });
    }
}