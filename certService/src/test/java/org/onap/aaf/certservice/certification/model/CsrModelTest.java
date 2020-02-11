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
import org.bouncycastle.util.io.pem.PemObject;
import org.junit.jupiter.api.Test;
import org.onap.aaf.certservice.certification.PemObjectFactory;
import org.onap.aaf.certservice.certification.exceptions.CsrDecryptionException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.onap.aaf.certservice.certification.TestData.TEST_CSR;
import static org.onap.aaf.certservice.certification.TestData.TEST_PK;
import static org.onap.aaf.certservice.certification.TestUtils.pemObjectToString;


class CsrModelTest {


    @Test
    void shouldByConstructedAndReturnProperFields() throws CsrDecryptionException, IOException {
        // given
        PemObject testPublicKey = generateTestPublicKey();

        // when
        CsrModel csrModel = generateTestCsrModel();


        // then
        assertEquals(
                pemObjectToString(csrModel.getPrivateKey()).trim(),
                TEST_PK.trim());
        assertEquals(
                pemObjectToString(csrModel.getPublicKey()).trim(),
                pemObjectToString((testPublicKey)).trim());
        assertThat(csrModel.getSansData())
                .contains(
                        "gerrit.onap.org", "test.onap.org", "onap.com");
        assertThat(csrModel.getSubjectData().toString())
                .contains(
                        "C=US,ST=California,L=San-Francisco,O=Linux-Foundation,OU=ONAP,CN=onap.org,E=tester@onap.org");
    }

    @Test
    void shouldThrowExceptionWhenKeyIsNotCorrect() throws IOException, CsrDecryptionException {
        // given
        PemObjectFactory pemObjectFactory = new PemObjectFactory();
        PKCS10CertificationRequest testCsr = mock(PKCS10CertificationRequest.class);
        SubjectPublicKeyInfo wrongKryInfo = mock(SubjectPublicKeyInfo.class);
        when(testCsr.getSubjectPublicKeyInfo())
                .thenReturn(wrongKryInfo);
        when(wrongKryInfo.getEncoded())
                .thenThrow(new IOException());
        PemObject testPrivateKey = pemObjectFactory.createPmObject(TEST_PK);
        CsrModel csrModel = new CsrModel(testCsr, testPrivateKey);

        // when
        Exception exception = assertThrows(
                CsrDecryptionException.class,
                csrModel::getPublicKey
        );

        String expectedMessage = "Reading Public Key from CSR failed";
        String actualMessage = exception.getMessage();

        // then
        assertTrue(actualMessage.contains(expectedMessage));
    }

    private CsrModel generateTestCsrModel() throws CsrDecryptionException, IOException {
        PemObjectFactory pemObjectFactory = new PemObjectFactory();
        PKCS10CertificationRequest testCsr = new PKCS10CertificationRequest(
                pemObjectFactory.createPmObject(TEST_CSR).getContent()
        );
        PemObject testPrivateKey = pemObjectFactory.createPmObject(TEST_PK);
        return new CsrModel(testCsr, testPrivateKey);
    }

    private PemObject generateTestPublicKey() throws CsrDecryptionException, IOException {
        PemObjectFactory pemObjectFactory = new PemObjectFactory();
        PKCS10CertificationRequest testCsr = new PKCS10CertificationRequest(
                pemObjectFactory.createPmObject(TEST_CSR).getContent()
        );
        return new PemObject("PUBLIC KEY", testCsr.getSubjectPublicKeyInfo().getEncoded());
    }
}
