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
import org.onap.aaf.certservice.client.certification.KeyPairFactory;
import org.onap.aaf.certservice.client.configuration.EnvsForClient;
import org.onap.aaf.certservice.client.configuration.EnvsForCsr;
import org.onap.aaf.certservice.client.configuration.factories.ClientConfigurationFactory;
import org.onap.aaf.certservice.client.configuration.factories.CsrConfigurationFactory;
import org.onap.aaf.certservice.client.configuration.model.ClientConfiguration;
import org.onap.aaf.certservice.client.configuration.model.CsrConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.onap.aaf.certservice.client.certification.EncryptionAlgorithmConstants.KEY_SIZE;
import static org.onap.aaf.certservice.client.certification.EncryptionAlgorithmConstants.RSA_ENCRYPTION_ALGORITHM;

public class CertServiceClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(CertServiceClient.class);

    public void run() {
        ClientConfiguration clientConfiguration;
        CsrConfiguration csrConfiguration;
        KeyPairFactory keyPairFactory = new KeyPairFactory(RSA_ENCRYPTION_ALGORITHM, KEY_SIZE);

        try {
            clientConfiguration = new ClientConfigurationFactory(new EnvsForClient()).create();
            csrConfiguration = new CsrConfigurationFactory(new EnvsForCsr()).create();
            keyPairFactory.create();
        } catch (ExitableException e) {
            exit(e.applicationExitCode());
        }

        exit(0);
    }

    public void exit(int exitCode) {
        LOGGER.debug("Application exits with following exit code: " + exitCode);
        System.exit(exitCode);
    }

}
