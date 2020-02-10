/*
 * ============LICENSE_START=======================================================
 * PROJECT
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

package org.onap.aaf.certservice.certification;

import org.bouncycastle.util.encoders.Base64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.onap.aaf.certservice.certification.exceptions.CSRDecryptionException;
import org.onap.aaf.certservice.certification.model.CSRModel;
import org.onap.aaf.certservice.certification.CSRFactory.StringBase64;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.onap.aaf.certservice.certification.TestUtils.*;

class CSRFactoryTest {

    private CSRFactory csrFactory;

    @BeforeEach
    void setUp() {
        csrFactory = new CSRFactory();
    }

    @Test
    void certificateProviderShouldDecryptCSRAndReturnStringWithDataAboutIt() throws CSRDecryptionException {
        // given
        String encoderCST = new String(Base64.encode(TEST_CSR.getBytes()));
        String encoderPK = new String(Base64.encode(TEST_PK.getBytes()));

        // when
        CSRModel decryptedCSR = csrFactory.createCSR(new StringBase64(encoderCST), new StringBase64(encoderPK));

        // then
        assertTrue(
                decryptedCSR.toString().contains("C=US,ST=California,L=San-Francisco,O=Linux-Foundation,OU=ONAP,CN=onap.org,E=tester@onap.org") &&
                        decryptedCSR.toString().contains("SANs: [gerrit.onap.org, test.onap.org, onap.com]")
        );
    }


    @Test
    void certificateProviderShouldThrowCSRDecryptionExceptionIfCSRareIncorrect() {
        // given
        String encoderPK = new String(Base64.encode(TEST_PK.getBytes()));
        String wrongCSR = new String(Base64.encode(TEST_WRONG_CSR.getBytes()));

        // when
        Exception exception = assertThrows(
                CSRDecryptionException.class,
                () -> csrFactory.createCSR(new StringBase64(wrongCSR), new StringBase64(encoderPK))
        );

        String expectedMessage = "Incorrect CSR, decryption failed";
        String actualMessage = exception.getMessage();

        // then
        assertTrue(actualMessage.contains(expectedMessage));
    }

}
