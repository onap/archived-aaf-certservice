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

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Base64PkEncoderTest {

    private static final String BEGIN_PK = "-----BEGIN PRIVATE KEY-----\n";
    private static final String END_PK = "-----END PRIVATE KEY-----\n";
    private static final String ENCRYPTION_ALGORITHM = "RSA";

    @Test
    public void shouldBePossibleToDecodeEncodedPem() throws NoSuchAlgorithmException, InvalidKeySpecException, PkEncodingException {
        //given
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ENCRYPTION_ALGORITHM);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        Base64PkEncoder testedPkEncoder = new Base64PkEncoder();
        //when
        String base64EncodedPem = testedPkEncoder.encodePrivateKey(privateKey);

        //then
        String decodedPem = new String(Base64.decode(base64EncodedPem));
        PrivateKey privateKeyDecoded = decodePkFromPem(decodedPem);

        assertPemIsValid(decodedPem);
        assertEquals(privateKey, privateKeyDecoded);
    }

    private PrivateKey decodePkFromPem(String decodedPem) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] encodedKey = extractEncodedKeyFromPem(decodedPem);
        return generatePkFromEncodedKey(encodedKey);
    }

    private byte[] extractEncodedKeyFromPem(String decodedPEM) {
        String pemData = decodedPEM.replaceFirst(BEGIN_PK, "")
                .replaceFirst(END_PK, "");
        return Base64.decode(pemData);
    }

    private PrivateKey generatePkFromEncodedKey(byte[] encodedKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(encodedKey);
        KeyFactory kf = KeyFactory.getInstance(ENCRYPTION_ALGORITHM);
        return kf.generatePrivate(pkcs8EncodedKeySpec);
    }

    private void assertPemIsValid(String decodedPEM) {
        assertThat(decodedPEM).startsWith(BEGIN_PK);
        assertThat(decodedPEM).endsWith(END_PK);
    }

}