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

package org.onap.aaf.certservice.client.certification;


import org.bouncycastle.util.encoders.Base64;
import org.junit.jupiter.api.Test;
import org.onap.aaf.certservice.client.certification.exception.PkEncodingException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Base64PkEncoderTest {

    private static final String BEGIN_PK = "-----BEGIN PRIVATE KEY-----\n";
    private static final String END_PK = "-----END PRIVATE KEY-----\n";
    private static final String ENCRYPTION_ALGORITHM = "RSA";
    private static final String RESOURCES_DIR = "src/test/resources/";
    private static final String PRIVATE_KEY_PEM_FILE = "privateKeyPem";
    private static final String PRIVATE_KEY_PEM_ENCODED_FILE = "privateKeyPem_Encoded";

    @Test
    public void shouldReturnProperlyEncodedPrivateKey() throws InvalidKeySpecException, NoSuchAlgorithmException, PkEncodingException, IOException {
        //given
        String pem = Files.readString(Paths.get(RESOURCES_DIR + PRIVATE_KEY_PEM_FILE));
        String expectedOutput = Files.readString(Paths.get(RESOURCES_DIR + PRIVATE_KEY_PEM_ENCODED_FILE));
        PrivateKey privateKey = extractPrivateKeyFromPem(pem);
        Base64PkEncoder testedPkEncoder = new Base64PkEncoder();
        //when
        String encodedPrivateKey = testedPkEncoder.encodePrivateKey(privateKey);
        //then
        assertEquals(expectedOutput, encodedPrivateKey);
    }

    private PrivateKey extractPrivateKeyFromPem(String decodedPEM) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String pemData = decodedPEM.replaceFirst(BEGIN_PK, "")
                .replaceFirst(END_PK, "");
        byte[] decodedPk = Base64.decode(pemData);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(decodedPk);
        KeyFactory kf = KeyFactory.getInstance(ENCRYPTION_ALGORITHM);
        return kf.generatePrivate(pkcs8EncodedKeySpec);
    }
}