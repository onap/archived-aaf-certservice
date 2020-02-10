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

package org.onap.aaf.certservice.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.aaf.certservice.certification.CSRFactory;
import org.onap.aaf.certservice.certification.CSRFactory.StringBase64;
import org.onap.aaf.certservice.certification.exceptions.CSRDecryptionException;
import org.onap.aaf.certservice.certification.model.CSRModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CertificationServiceTest {

    private CertificationService certificationService;

    @Mock
    private CSRFactory csrFactory;

    @BeforeEach
    void serUp() {
        MockitoAnnotations.initMocks(this);
        certificationService = new CertificationService(csrFactory);
    }

    @Test
    void shouldReturnDataAboutCSRBaseOnEncodedParameters() throws CSRDecryptionException {
        // given
        CSRModel mockedCSRModel = mock(CSRModel.class);
        when(mockedCSRModel.toString()).thenReturn("testData");
        when(csrFactory.createCSR(any(StringBase64.class), any(StringBase64.class)))
                .thenReturn(mockedCSRModel);

        // when
        ResponseEntity<String> testResponse =
                certificationService.signCertificate("TestCa", "encryptedCSR", "encryptedPK");

        // then
        assertEquals(testResponse.getStatusCode(), HttpStatus.OK);
        assertTrue(
                testResponse.toString().contains("testData")
        );
    }

    @Test
    void shouldReturnBadRequestIfCreatingCSRModelFails() throws CSRDecryptionException {
        // given
        when(csrFactory.createCSR(any(StringBase64.class), any(StringBase64.class)))
                .thenThrow(new CSRDecryptionException("creation fail",new IOException()));

        // when
        ResponseEntity<String> testResponse =
                certificationService.signCertificate("TestCa", "encryptedCSR", "encryptedPK");

        String expectedMessage = "Wrong certificate sign request (CSR) format";

        // then
        assertEquals(testResponse.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertTrue(
                testResponse.toString().contains(expectedMessage)
        );

    }

    private String encode(String data) {
        return new String(Base64.getEncoder().encode(data.getBytes()));
    }
}
