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
import org.onap.aaf.certservice.client.certification.exception.PemToPKCS12ConverterException;

class PKCS12ArtifactCreatorTest {

    private PKCS12FilesCreator filesCreator = mock(PKCS12FilesCreator.class);
    private RandomPasswordGenerator passwordGenerator = mock(RandomPasswordGenerator.class);
    private PemToPKCS12Converter converter = mock(PemToPKCS12Converter.class);
    private PrivateKey privateKey = mock(PrivateKey.class);

    @Test
    void createKeystoreShouldCallRequiredMethods() throws PemToPKCS12ConverterException {
        // given
        final Password password = new Password("d9D_u8LooYaXH4G48DtN#vw0");
        final List<String> certificates = List.of("a", "b");
        final int passwordLength = 24;
        final String alias = "certificate";
        final byte[] keystoreBytes = "this is a keystore test".getBytes();
        PKCS12ArtifactCreator creator = new PKCS12ArtifactCreator(filesCreator, passwordGenerator, converter);

        // when
        when(passwordGenerator.generate(passwordLength)).thenReturn(password);
        when(converter.convertKeystore(certificates, password, alias, privateKey)).thenReturn(keystoreBytes);
        creator.createKeystore(certificates, privateKey);

        // then
        verify(passwordGenerator, times(1)).generate(passwordLength);
        verify(converter, times(1)).convertKeystore(certificates, password, alias, privateKey);
        verify(filesCreator, times(1)).saveKeystoreData(keystoreBytes, password.getCurrentPassword());
    }

    @Test
    void createTruststoreShouldCallRequiredMethods() throws PemToPKCS12ConverterException {
        // given
        final Password password = new Password("d9D_u8LooYaXH4G48DtN#vw0");
        final List<String> certificateChain = List.of("a", "b");
        final List<String> trustAnchors = List.of("a", "b");
        final int passwordLength = 24;
        final String alias = "trusted-certificate-";
        final byte[] truststoreBytes = "this is a truststore test".getBytes();
        PKCS12ArtifactCreator creator = new PKCS12ArtifactCreator(filesCreator, passwordGenerator, converter);

        // when
        when(passwordGenerator.generate(passwordLength)).thenReturn(password);
        when(converter.convertTruststore(certificateChain, password, alias)).thenReturn(truststoreBytes);
        creator.createTruststore(certificateChain);

        // then
        verify(passwordGenerator, times(1)).generate(passwordLength);
        verify(converter, times(1)).convertTruststore(trustAnchors, password, alias);
        verify(filesCreator, times(1)).saveTruststoreData(truststoreBytes, password.getCurrentPassword());
    }
    //TODO
    // 1. Password generator should be called twice times
    // 2. Convert keystore should be called
    // 3. Keystore file creator should be called -> KeystoreCreationFuncion Should Be Called
    // 2. Convert truststore func should be called
    // 3. Trustoster file creator should be called -> TrustStore creation function should be called
    @Test
    void generatePKCS12ArtifactShouldCallRequiredMethods() throws PemToPKCS12ConverterException {
        // given
        final Password password = new Password("d9D_u8LooYaXH4G48DtN#vw0");
        final List<String> certificateChain = List.of("a", "b");
        final List<String> trustAnchors = List.of("a", "b");
        final int passwordLength = 24;
        final String certificateAlias = "certificate";
        final String trustedCertificateAlias = "trusted-certificate-";
        final byte[] keystoreBytes = "this is a keystore test".getBytes();
        final byte[] truststoreBytes = "this is a truststore test".getBytes();
        PKCS12ArtifactCreator creator = new PKCS12ArtifactCreator(filesCreator, passwordGenerator, converter);

        // when
        when(passwordGenerator.generate(passwordLength)).thenReturn(password);
        when(converter.convertKeystore(certificateChain, password, certificateAlias, privateKey)).thenReturn(keystoreBytes);
        when(converter.convertTruststore(trustAnchors, password, trustedCertificateAlias)).thenReturn(truststoreBytes);
//        creator.createKeystore(certificateChain, privateKey);
        creator.generateArtifacts(certificateChain, trustAnchors, privateKey);

        //then
        // then
        verify(passwordGenerator, times(2)).generate(passwordLength);
        verify(converter, times(1)).convertKeystore(
                certificateChain, password, certificateAlias, privateKey);
        verify(filesCreator, times(1)).saveKeystoreData(
                keystoreBytes, password.getCurrentPassword());
        verify(converter, times(1)).convertTruststore(trustAnchors, password, trustedCertificateAlias);
        verify(filesCreator, times(1)).saveTruststoreData(truststoreBytes, password.getCurrentPassword());
    }
}
