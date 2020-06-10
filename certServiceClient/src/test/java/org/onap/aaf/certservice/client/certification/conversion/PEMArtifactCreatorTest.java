package org.onap.aaf.certservice.client.certification.conversion;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.PrivateKey;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.onap.aaf.certservice.client.api.ExitableException;
import org.onap.aaf.certservice.client.certification.PrivateKeyToPemEncoder;

class PEMArtifactCreatorTest {
    private FilesCreator filesCreator = mock(FilesCreator.class);
    private PrivateKey privateKey = mock(PrivateKey.class);
    private PrivateKeyToPemEncoder pkEncoder = mock(PrivateKeyToPemEncoder.class);

    @Test
    void generateArtifactsShouldCallRequiredMethods() throws ExitableException {
        // given
        final String keystorePem = "keystore.pem";
        final String truststorePem = "truststore.pem";
        final String keyPem = "key.pem";
        final String key = "my private key";
        final PEMArtifactCreator creator = new PEMArtifactCreator(filesCreator, pkEncoder);

        // when
        when(pkEncoder.encodePrivateKeyToPem(privateKey)).thenReturn(key);
        creator.generateArtifacts(List.of("one", "two"), List.of("three", "four"), privateKey);

        // then
        verify(filesCreator, times(1)).saveDataToLocation("one\ntwo".getBytes(), keystorePem);
        verify(filesCreator, times(1)).saveDataToLocation("three\nfour".getBytes(), truststorePem);
        verify(filesCreator, times(1)).saveDataToLocation(key.getBytes(), keyPem);
    }
}
