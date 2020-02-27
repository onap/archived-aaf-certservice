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

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.aaf.certservice.certification.CertificationModelFactory;
import org.onap.aaf.certservice.certification.exception.Cmpv2ClientAdapterException;
import org.onap.aaf.certservice.certification.exception.Cmpv2ServerNotFoundException;
import org.onap.aaf.certservice.certification.exception.CsrDecryptionException;
import org.onap.aaf.certservice.certification.exception.DecryptionException;
import org.onap.aaf.certservice.certification.exception.KeyDecryptionException;
import org.onap.aaf.certservice.certification.model.CertificationModel;
import org.onap.aaf.certservice.cmpv2client.exceptions.CmpClientException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class CertificationServiceTest {

    private static final String TEST_CA_NAME = "TestCa";
    private static final String TEST_ENCODED_CSR = "encodedCSR";
    private static final String TEST_ENCODED_PK = "encodedPK";
    private static final String TEST_WRONG_ENCODED_CSR = "wrongEncodedCSR";
    private static final String TEST_WRONG_ENCODED_PK = "wrongEncodedPK";
    private static final String TEST_WRONG_CA_NAME = "wrongTestCa";

    private CertificationService certificationService;

    @Mock
    private CertificationModelFactory certificationModelFactory;

    @BeforeEach
    void serUp() {
        MockitoAnnotations.initMocks(this);
        certificationService = new CertificationService(certificationModelFactory);
    }

    @Test
    void shouldReturnDataAboutCsrBaseOnEncodedParameters()
            throws DecryptionException, CmpClientException, Cmpv2ClientAdapterException {
        // given
        CertificationModel testCertificationModel = new CertificationModel(
                Arrays.asList("ENTITY_CERT", "INTERMEDIATE_CERT"),
                Arrays.asList("CA_CERT", "EXTRA_CA_CERT")
        );
        when(certificationModelFactory.createCertificationModel(TEST_ENCODED_CSR, TEST_ENCODED_PK, TEST_CA_NAME))
                .thenReturn(testCertificationModel);

        // when
        ResponseEntity<String> testResponse =
                certificationService.signCertificate(TEST_CA_NAME, TEST_ENCODED_CSR, TEST_ENCODED_PK);

        CertificationModel responseCertificationModel = new Gson().fromJson(testResponse.getBody(), CertificationModel.class);

        // then
        assertEquals(HttpStatus.OK, testResponse.getStatusCode());
        assertThat(responseCertificationModel
        ).isEqualToComparingFieldByField(testCertificationModel);

    }

    @Test
    void shouldThrowCsrDecryptionExceptionWhenCreatingCsrModelFails()
            throws DecryptionException, CmpClientException, Cmpv2ClientAdapterException {
        // given
        String expectedMessage = "Incorrect CSR, decryption failed";
        when(certificationModelFactory.createCertificationModel(TEST_WRONG_ENCODED_CSR, TEST_ENCODED_PK, TEST_CA_NAME))
                .thenThrow(new CsrDecryptionException(expectedMessage));

        // when
        Exception exception = assertThrows(
                CsrDecryptionException.class, () ->
                        certificationService.signCertificate(TEST_CA_NAME, TEST_WRONG_ENCODED_CSR, TEST_ENCODED_PK)
        );

        String actualMessage = exception.getMessage();

        // then
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void shouldThrowPemDecryptionExceptionWhenCreatingPemModelFails()
            throws DecryptionException, CmpClientException, Cmpv2ClientAdapterException {
        // given
        String expectedMessage = "Incorrect PEM, decryption failed";
        when(certificationModelFactory.createCertificationModel(TEST_ENCODED_CSR, TEST_WRONG_ENCODED_PK, TEST_CA_NAME))
                .thenThrow(new KeyDecryptionException(expectedMessage));

        // when
        Exception exception = assertThrows(
                KeyDecryptionException.class, () ->
                        certificationService.signCertificate(TEST_CA_NAME, TEST_ENCODED_CSR, TEST_WRONG_ENCODED_PK)
        );

        String actualMessage = exception.getMessage();

        // then
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void shouldThrowCmpv2ServerNotFoundWhenGivenWrongCaName()
            throws DecryptionException, CmpClientException, Cmpv2ClientAdapterException {
        // given
        String expectedMessage = "No server found for given CA name";
        when(certificationModelFactory.createCertificationModel(TEST_ENCODED_CSR, TEST_ENCODED_PK, TEST_WRONG_CA_NAME))
                .thenThrow(new Cmpv2ServerNotFoundException(expectedMessage));

        // when
        Exception exception = assertThrows(
                Cmpv2ServerNotFoundException.class, () ->
                        certificationService.signCertificate(TEST_WRONG_CA_NAME, TEST_ENCODED_CSR, TEST_ENCODED_PK)
        );

        String actualMessage = exception.getMessage();

        // then
        assertEquals(expectedMessage, actualMessage);
    }
}
