/*============LICENSE_START=======================================================
 * aaf-certservice-client
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
import org.onap.aaf.certservice.client.certification.writer.CertFileWriter;

class PemArtifactsCreatorTest {
    private CertFileWriter certFileWriter = mock(CertFileWriter.class);
    private PrivateKey privateKey = mock(PrivateKey.class);
    private PrivateKeyToPemEncoder pkEncoder = mock(PrivateKeyToPemEncoder.class);

    @Test
    void pemArtifactsCreatorShouldCallRequiredMethods() throws ExitableException {
        // given
        final String keystorePem = "keystore.pem";
        final String truststorePem = "truststore.pem";
        final String keyPem = "key.pem";
        final String key = "my private key";
        final PemArtifactsCreator creator = new PemArtifactsCreator(certFileWriter, pkEncoder);

        // when
        when(pkEncoder.encodePrivateKeyToPem(privateKey)).thenReturn(key);
        creator.create(List.of("one", "two"), List.of("three", "four"), privateKey);

        // then
        verify(certFileWriter, times(1)).saveData("one\ntwo".getBytes(), keystorePem);
        verify(certFileWriter, times(1)).saveData("three\nfour".getBytes(), truststorePem);
        verify(certFileWriter, times(1)).saveData(key.getBytes(), keyPem);
    }
}
