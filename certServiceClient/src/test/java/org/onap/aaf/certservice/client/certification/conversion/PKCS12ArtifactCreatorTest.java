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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.onap.aaf.certservice.client.certification.exception.PemToPKCS12ConverterException;

class PKCS12ArtifactCreatorTest {

    private static final int passwordLength = 24;
    private static final String CERTIFICATE_ALIAS = "certificate";
    private static final String TRUSTED_CERTIFICATE_ALIAS = "trusted-certificate-";

    private FilesCreator filesCreator;
    private RandomPasswordGenerator passwordGenerator;
    private PemToPKCS12Converter converter;
    private PrivateKey privateKey;
    private Password password;
    private List<String> keystoreCertificateChain;
    private List<String> trustedCertificateChain;
    private PKCS12ArtifactCreator artifactCreator;
    private byte[] keystoreBytes;
    private byte[] truststoreBytes;

    @BeforeEach
    void setUp() {
        filesCreator = mock(FilesCreator.class);
        passwordGenerator = mock(RandomPasswordGenerator.class);
        converter = mock(PemToPKCS12Converter.class);
        privateKey = mock(PrivateKey.class);
        artifactCreator = new PKCS12ArtifactCreator(filesCreator, passwordGenerator, converter);
        password = new Password("d9D_u8LooYaXH4G48DtN#vw0");
        keystoreCertificateChain = List.of("a", "b");
        trustedCertificateChain = List.of("c", "d");
        keystoreBytes = "this is a keystore test".getBytes();
        truststoreBytes = "this is a truststore test".getBytes();
    }

    @Test
    void generateArtifactsShouldCallConverterAndFilesCreatorMethods() throws PemToPKCS12ConverterException {
        // given
        mockPasswordGeneratorAndPKSC12Converter();

        //when
        artifactCreator.generateArtifacts(keystoreCertificateChain, trustedCertificateChain, privateKey);

        // then
        verify(converter, times(1))
                .convertKeystore(keystoreCertificateChain, password, CERTIFICATE_ALIAS, privateKey);
        verify(filesCreator, times(1))
                .saveKeystoreData(keystoreBytes, password.getCurrentPassword());
        verify(converter, times(1))
                .convertTruststore(trustedCertificateChain, password, TRUSTED_CERTIFICATE_ALIAS);
        verify(filesCreator, times(1))
                .saveTruststoreData(truststoreBytes, password.getCurrentPassword());
    }

    @Test
    void generateArtifactsMethodShouldCallPasswordGeneratorTwice() throws PemToPKCS12ConverterException {
        // given
        mockPasswordGeneratorAndPKSC12Converter();

        //when
        artifactCreator.generateArtifacts(keystoreCertificateChain, trustedCertificateChain, privateKey);

        // then
        verify(passwordGenerator, times(2)).generate(passwordLength);
    }

    private void mockPasswordGeneratorAndPKSC12Converter() throws PemToPKCS12ConverterException {
        when(passwordGenerator.generate(passwordLength)).thenReturn(password);
        when(converter.convertKeystore(keystoreCertificateChain, password, CERTIFICATE_ALIAS, privateKey))
                .thenReturn(keystoreBytes);
        when(converter.convertTruststore(trustedCertificateChain, password, TRUSTED_CERTIFICATE_ALIAS))
                .thenReturn(truststoreBytes);
    }
}
