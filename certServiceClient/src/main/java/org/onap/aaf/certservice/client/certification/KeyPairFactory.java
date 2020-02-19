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

import org.onap.aaf.certservice.client.exceptions.KeyPairGenerationException;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class KeyPairFactory {
    private String encryptionAlgorithm;
    private int keySize;

    public KeyPairFactory(String encryptionAlgorithm, int keySize) {
        this.encryptionAlgorithm = encryptionAlgorithm;
        this.keySize = keySize;
    }

    public KeyPair crate() {
        try {
            return createKeyPairGenerator().generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new KeyPairGenerationException(e.getMessage());
        }
    }

    public KeyPairGenerator createKeyPairGenerator() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(encryptionAlgorithm);
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator;
    }
}
