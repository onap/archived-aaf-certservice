package org.onap.aaf.certservice.certification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.onap.aaf.certservice.certification.adapter.Cmpv2ClientAdapter;
import org.onap.aaf.certservice.certification.configuration.model.Cmpv2Server;
import org.onap.aaf.certservice.certification.exception.Cmpv2ClientAdapterException;
import org.onap.aaf.certservice.certification.exception.DecryptionException;
import org.onap.aaf.certservice.certification.model.CertificationModel;
import org.onap.aaf.certservice.certification.model.CsrModel;
import org.onap.aaf.certservice.cmpv2client.exceptions.CmpClientException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CertificationProviderTest {

    private CertificationProvider certificationProvider;

    @Mock
    private Cmpv2ClientAdapter cmpv2ClientAdapter;

    @BeforeEach
    void setUp() {
        certificationProvider = new CertificationProvider(cmpv2ClientAdapter);
    }

    @Test
    void certificationProviderShouldReturnCertificationModelWhenProvidedProperCsrModelAndCmpv2Server()
            throws CmpClientException, Cmpv2ClientAdapterException {
        // Given
        CsrModel testCsrModel = mock(CsrModel.class);
        Cmpv2Server testServer = mock(Cmpv2Server.class);
        CertificationModel expectedCertificationModel = mock(CertificationModel.class);
        when(
                cmpv2ClientAdapter.callCmpClient(eq(testCsrModel), eq(testServer))
        ).thenReturn(expectedCertificationModel);

        // When
        CertificationModel receivedCertificationModel = certificationProvider.signCsr(testCsrModel, testServer);

        // Then
        assertThat(receivedCertificationModel).isEqualTo(expectedCertificationModel);
    }

    @Test
    void certificationProviderThrowCmpClientWhenCallingClientFails()
            throws CmpClientException, Cmpv2ClientAdapterException {
        // Given
        CsrModel testCsrModel = mock(CsrModel.class);
        Cmpv2Server testServer = mock(Cmpv2Server.class);
        String expectedErrorMessage = "connecting to CMP client failed";
        when(
                cmpv2ClientAdapter.callCmpClient(eq(testCsrModel), eq(testServer))
        ).thenThrow(new CmpClientException(expectedErrorMessage));

        // When
        Exception exception = assertThrows(
                CmpClientException.class, () ->
                        certificationProvider.signCsr(testCsrModel, testServer)
        );

        // Then
        assertThat(exception.getMessage()).isEqualTo(expectedErrorMessage);
    }

}
