package org.onap.aaf.certservice.certification.adapter;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.onap.aaf.certservice.certification.configuration.model.Authentication;
import org.onap.aaf.certservice.certification.configuration.model.CaMode;
import org.onap.aaf.certservice.certification.configuration.model.Cmpv2Server;
import org.onap.aaf.certservice.certification.model.CsrModel;
import org.onap.aaf.certservice.cmpv2client.external.CSRMeta;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CSRMetaBuilderTest {

    private CSRMetaBuilder csrMetaBuilder;

    private static final String TEST_CA = "testCA";
    private static final X500Name TEST_SUBJECT_DATA = new X500Name("CN=testIssuer");

    @BeforeEach
    void setUp() {
        csrMetaBuilder = new CSRMetaBuilder();
    }

    @Test
    void shouldBuildCsrMetaWhenGivenCsrModelAndCmpv2ServerAreCorrect() {
        // Given
        CsrModel testCsrModel = mock(CsrModel.class);
        Cmpv2Server testServer = createTestServer();

        PKCS10CertificationRequest certificationRequest = mock(PKCS10CertificationRequest.class);
        when(testCsrModel.getCsr()).thenReturn(certificationRequest);
        PrivateKey mockPrivateKey = mock(PrivateKey.class);
        when(testCsrModel.getPrivateKey()).thenReturn(mockPrivateKey);
        PublicKey mockPublicKey = mock(PublicKey.class);
        when(testCsrModel.getPublicKey()).thenReturn(mockPublicKey);
        List<String> testSans = Arrays.asList("SAN01","SAN02");
        when(testCsrModel.getSans()).thenReturn(testSans);

        when(testCsrModel.getSubjectData()).thenReturn(TEST_SUBJECT_DATA);

        // When
        CSRMeta createdCSRMeta = csrMetaBuilder.build(testCsrModel, testServer);

        // Then
        assertThat(createdCSRMeta.password()).isEqualTo(testServer.getAuthentication().getIak());
        assertThat(createdCSRMeta.caUrl()).isEqualTo(testServer.getUrl());
        assertThat(createdCSRMeta.sans()).containsAll(testSans);
        assertThat(createdCSRMeta.keyPair().getPrivate()).isEqualTo(mockPrivateKey);
        assertThat(createdCSRMeta.keyPair().getPublic()).isEqualTo(mockPublicKey);
        assertThat(createdCSRMeta.x500Name()).isEqualTo(TEST_SUBJECT_DATA);
        assertThat(createdCSRMeta.issuerx500Name()).isEqualTo(TEST_SUBJECT_DATA);
    }

    private Cmpv2Server createTestServer() {
        Cmpv2Server testServer = new Cmpv2Server();
        testServer.setCaName(TEST_CA);
        testServer.setIssuerDN(TEST_SUBJECT_DATA);
        testServer.setUrl("http://test.ca.server");
        Authentication testAuthentication = new Authentication();
        testAuthentication.setIak("testIak");
        testAuthentication.setRv("testRv");
        testServer.setAuthentication(testAuthentication);
        testServer.setCaMode(CaMode.RA);

        return testServer;
    }

}
