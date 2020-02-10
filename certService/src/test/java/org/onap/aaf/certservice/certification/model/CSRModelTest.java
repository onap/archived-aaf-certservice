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

package org.onap.aaf.certservice.certification.model;

import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.io.pem.PemObject;
import org.junit.jupiter.api.Test;
import org.onap.aaf.certservice.certification.PemObjectFactory;
import org.onap.aaf.certservice.certification.exceptions.CSRDecryptionException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.onap.aaf.certservice.certification.TestUtils.*;

class CSRModelTest {


    @Test
    void csrModelShouldByConstructedAndReturnProperFields() throws CSRDecryptionException, IOException {
        // given
        PemObject testPublicKey = generateTestPublicKey();

        // when
        CSRModel csrModel = generateTestCSRModel();


        // then
        assertEquals(
                pemObjectToString(csrModel.getPrivateKey()).trim(),
                TEST_PK.trim());
        assertEquals(
                pemObjectToString(csrModel.getPublicKey()).trim(),
                pemObjectToString((testPublicKey)).trim());
        assertThat(csrModel.getSANsData())
                .contains("gerrit.onap.org", "test.onap.org", "onap.com");
        assertThat(csrModel.getSubjectData().toString())
                .contains("C=US,ST=California,L=San-Francisco,O=Linux-Foundation,OU=ONAP,CN=onap.org,E=tester@onap.org");
    }

    @Test
    void gettingPublicKeyFromCSRModelShouldThrowExceptionIfKeyIsNotCorrect() throws IOException, CSRDecryptionException {
        // given
        PemObjectFactory pemObjectFactory = new PemObjectFactory();
        PKCS10CertificationRequest testCSR = mock(PKCS10CertificationRequest.class);
        SubjectPublicKeyInfo wrongKryInfo = mock(SubjectPublicKeyInfo.class);
        when(testCSR.getSubjectPublicKeyInfo())
                .thenReturn(wrongKryInfo);
        when(wrongKryInfo.getEncoded())
                .thenThrow(new IOException());
        PemObject testPrivateKey = pemObjectFactory.createPmObject(TEST_PK);
        CSRModel csrModel = new CSRModel(testCSR, testPrivateKey);

        // when
        Exception exception = assertThrows(
                CSRDecryptionException.class,
                csrModel::getPublicKey
        );

        String expectedMessage = "Reading Public Key from CSR failed";
        String actualMessage = exception.getMessage();

        // then
        assertTrue(actualMessage.contains(expectedMessage));
    }

    private CSRModel generateTestCSRModel() throws CSRDecryptionException, IOException {
        PemObjectFactory pemObjectFactory = new PemObjectFactory();
        PKCS10CertificationRequest testCSR = new PKCS10CertificationRequest(
                pemObjectFactory.createPmObject(TEST_CSR).getContent()
        );
        PemObject testPrivateKey = pemObjectFactory.createPmObject(TEST_PK);
        return new CSRModel(testCSR, testPrivateKey);
    }

    private PemObject generateTestPublicKey() throws CSRDecryptionException, IOException {
        PemObjectFactory pemObjectFactory = new PemObjectFactory();
        PKCS10CertificationRequest testCSR = new PKCS10CertificationRequest(
                pemObjectFactory.createPmObject(TEST_CSR).getContent()
        );
        return new PemObject("PUBLIC KEY", testCSR.getSubjectPublicKeyInfo().getEncoded());
    }
}
