/**
 * ============LICENSE_START====================================================
 * org.onap.aaf
 * ===========================================================================
 * Copyright (c) 2018 AT&T Intellectual Property. All rights reserved.
 * ===========================================================================
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
 * ============LICENSE_END====================================================
 *
 */
package org.onap.aaf.certservice.client.cert.external;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Factory {

    private static final KeyPairGenerator keygen;
    private static final SecureRandom random;
    private static final String KEY_ALGO = "RSA";
    static final String SIG_ALGO = "SHA256withRSA";
    private static final int KEY_LENGTH = 2048;
    private static final int SUB = 0x08;

    static {
        random = new SecureRandom();
        KeyPairGenerator tempKeygen;
        try {
            tempKeygen = KeyPairGenerator.getInstance(KEY_ALGO);//,"BC");
            tempKeygen.initialize(KEY_LENGTH, random);
        } catch (NoSuchAlgorithmException e) {
            tempKeygen = null;
            e.printStackTrace(System.err);
        }
        keygen = tempKeygen;
    }

    public static KeyPair generateKeyPair(Trans trans) {
        TimeTaken tt;
        if (trans!=null) {
            tt = trans.start("Generate KeyPair", SUB);
        } else {
            tt = null;
        }
        try {
            return keygen.generateKeyPair();
        } finally {
            if (tt!=null) {
                tt.done();
            }
        }
    }
}
