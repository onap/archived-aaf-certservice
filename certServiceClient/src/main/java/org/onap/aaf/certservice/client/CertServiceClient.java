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

package org.onap.aaf.certservice.client;

import org.onap.aaf.certservice.client.api.ExitableException;
import org.onap.aaf.certservice.client.certification.CsrFactory;
import org.onap.aaf.certservice.client.certification.KeyPairFactory;
import org.onap.aaf.certservice.client.configuration.EnvsForClient;
import org.onap.aaf.certservice.client.configuration.EnvsForCsr;
import org.onap.aaf.certservice.client.configuration.factory.ClientConfigurationFactory;
import org.onap.aaf.certservice.client.configuration.factory.CsrConfigurationFactory;
import org.onap.aaf.certservice.client.configuration.model.ClientConfiguration;
import org.onap.aaf.certservice.client.configuration.model.CsrConfiguration;

import java.security.KeyPair;
import java.util.Optional;

import static org.onap.aaf.certservice.client.certification.EncryptionAlgorithmConstants.KEY_SIZE;
import static org.onap.aaf.certservice.client.certification.EncryptionAlgorithmConstants.RSA_ENCRYPTION_ALGORITHM;

public class CertServiceClient {
    private AppExitHandler appExitHandler;

    public CertServiceClient(AppExitHandler appExitHandler) {
        this.appExitHandler = appExitHandler;
    }

    public void run() {
        ClientConfiguration clientConfiguration;
        CsrConfiguration csrConfiguration;
        clientConfiguration = new ClientConfigurationFactory(new EnvsForClient()).create();
        csrConfiguration = new CsrConfigurationFactory(new EnvsForCsr()).create();

        KeyPairFactory keyPairFactory = new KeyPairFactory(RSA_ENCRYPTION_ALGORITHM, KEY_SIZE);
        Optional<KeyPair> keyPair = generateKeyPair(keyPairFactory);

        CsrFactory csrFactory = new CsrFactory(csrConfiguration);
        Optional<String> csr = generateCsr(csrFactory, keyPair.get());

        //ToDo: private key from keyPair.getPrivateKey() should be (Base64) encoded before passing it to an http header

        appExitHandler.exit(0);
    }

    public Optional<KeyPair> generateKeyPair(KeyPairFactory keyPairFactory) {
        try {
            return Optional.of(keyPairFactory.create());
        } catch (ExitableException e) {
            appExitHandler.exit(e.applicationExitCode());
        }
        return Optional.empty();
    }

    public Optional<String> generateCsr(CsrFactory csrFactory, KeyPair keyPair) {
        try {
            return Optional.of(csrFactory.createEncodedCsr(keyPair));
        } catch (ExitableException e) {
            appExitHandler.exit(e.applicationExitCode());
        }
        return Optional.empty();
    }
}
