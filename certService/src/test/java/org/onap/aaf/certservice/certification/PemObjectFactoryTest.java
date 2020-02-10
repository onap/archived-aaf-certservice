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

import org.bouncycastle.util.io.pem.PemObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.onap.aaf.certservice.certification.exceptions.CSRDecryptionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.onap.aaf.certservice.certification.TestUtils.*;

class PemObjectFactoryTest {


    private PemObjectFactory pemObjectFactory;

    @BeforeEach
    void setUp() {
        pemObjectFactory = new PemObjectFactory();
    }

    @Test
    void certificateParserShouldTransformStringInToPemObjectAndBackToString() throws CSRDecryptionException {
        // when
        PemObject pemObject = pemObjectFactory.createPmObject(TEST_PEM);
        String parsedPemObject = pemObjectToString(pemObject);

        // then
        assertEquals(parsedPemObject, TEST_PEM);
    }

    @Test
    void certificateParserShouldThrowExceptionIfParsingPemFailed() {
        // when
        Exception exception = assertThrows(
                CSRDecryptionException.class,
                () -> pemObjectFactory.createPmObject(TEST_WRONG_PEM)
        );

        String expectedMessage = "Creating PEM from string failed";
        String actualMessage = exception.getMessage();

        // then
        assertTrue(actualMessage.contains(expectedMessage));
    }

}
