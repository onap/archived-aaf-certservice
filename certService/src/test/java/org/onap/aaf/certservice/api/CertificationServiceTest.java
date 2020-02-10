package org.onap.aaf.certservice.api;

import com.fasterxml.jackson.databind.ser.Serializers;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.onap.aaf.certservice.certification.TestUtils.TEST_CSR;
import static org.onap.aaf.certservice.certification.TestUtils.TEST_PK;

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
