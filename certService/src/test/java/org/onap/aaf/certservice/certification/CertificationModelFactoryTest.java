package org.onap.aaf.certservice.certification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.onap.aaf.certservice.certification.model.CertificationModel;
import org.onap.aaf.certservice.certification.model.CsrModel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.onap.aaf.certservice.certification.CertificationData.CA_CERT;
import static org.onap.aaf.certservice.certification.CertificationData.ENTITY_CERT;
import static org.onap.aaf.certservice.certification.CertificationData.INTERMEDIATE_CERT;
import static org.onap.aaf.certservice.certification.CertificationData.NEW_CA_CERT;


class CertificationModelFactoryTest {


    private CertificationModelFactory certificationModelFactory;

    @BeforeEach
    void setUp() {
        certificationModelFactory = new CertificationModelFactory();
    }

    @Test
    void shouldCreateProperCertificationModelWhenGivenProperCsrModelAndCaName() {
        // given
        final String testCaName = "testCA";
        CsrModel mockedCsrModel = mock(CsrModel.class);

        // when
        CertificationModel certificationModel = certificationModelFactory.createCertificationModel(mockedCsrModel ,testCaName);

        //then
        assertEquals(2, certificationModel.getCertificateChain().size());
        assertThat(certificationModel.getCertificateChain()).contains(INTERMEDIATE_CERT, ENTITY_CERT);
        assertEquals(2, certificationModel.getTrustedCertificates().size());
        assertThat(certificationModel.getTrustedCertificates()).contains(CA_CERT, NEW_CA_CERT);
    }

}
