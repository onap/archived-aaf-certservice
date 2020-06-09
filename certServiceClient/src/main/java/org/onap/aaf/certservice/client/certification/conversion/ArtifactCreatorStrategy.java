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

import org.onap.aaf.certservice.client.configuration.exception.ClientConfigurationException;

import java.util.Arrays;

public enum ArtifactCreatorStrategy {
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
            return null;
        }
    };

    public static ArtifactCreatorStrategy getStrategyOfString(String strategy) throws ClientConfigurationException {
        try {
            return valueOf(strategy);
        } catch (Exception e) {
            //TODO refactor to SpecificException (implementing in other MR)
            throw new ClientConfigurationException(
                    String.format("Output type: %s is not supported. Supported types: %s",
                            strategy, Arrays.toString(values())));
        }
    }

    public abstract ArtifactCreator generateArtifactCreator(String outputPath);
}
