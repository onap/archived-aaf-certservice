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

import org.onap.aaf.certservice.client.certification.PrivateKeyToPemEncoder;
import org.onap.aaf.certservice.client.configuration.exception.ClientConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public enum ArtifactCreationStrategy {

    P12 {
        @Override
        public ArtifactCreator generateArtifactCreator(String outputPath) {
            return new PKCS12ArtifactCreatorFactory(outputPath).create();
        }
    },
    JKS {
        @Override
        public ArtifactCreator generateArtifactCreator(String outputPath) {
            return null;
        }
    },
    PEM {
        @Override
        public ArtifactCreator generateArtifactCreator(String outputPath) {
            return new PEMArtifactCreator(new FilesCreator(outputPath), new PrivateKeyToPemEncoder());
        }
    };

    private static final Logger LOGGER = LoggerFactory.getLogger(ArtifactCreationStrategy.class);

    public static ArtifactCreationStrategy getStrategyOfString(String strategy) throws ClientConfigurationException {
        try {
            return valueOf(strategy);
        } catch (Exception e) {
            LOGGER.error("Output type: {} is not supported. Supported types: {}", strategy, Arrays.toString(values()));
            //TODO refactor to SpecificException (implementing in other MR)
            throw new ClientConfigurationException("//TODO constructor with exception");
        }
    }

    public abstract ArtifactCreator generateArtifactCreator(String outputPath);
}
