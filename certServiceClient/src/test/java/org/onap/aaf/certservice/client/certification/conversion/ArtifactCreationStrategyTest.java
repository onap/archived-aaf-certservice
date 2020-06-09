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

import org.junit.jupiter.api.Test;
import org.onap.aaf.certservice.client.configuration.exception.ClientConfigurationException;
import org.onap.aaf.certservice.client.httpclient.exception.CertServiceApiResponseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.onap.aaf.certservice.client.CerServiceRequestTestData.CA_NAME;
import static org.onap.aaf.certservice.client.CerServiceRequestTestData.CSR;


class ArtifactCreationStrategyTest {

    @Test
    void getStrategyOfStringShouldReturnCorrectCreator() throws Exception {
        // given
        final String strategyP12 = "P12";
        final String path = "testPath";

        //when
        ArtifactCreator artifactCreator =
                ArtifactCreationStrategy.getStrategyOfString(strategyP12).generateArtifactCreator(path);
        // then
        assertThat(artifactCreator).isInstanceOf(PKCS12ArtifactCreator.class);

    }

    @Test
    void notSupportedStrategyShouldThrowException() throws Exception {
        // given
        String strategy = "notSupported";

        //when// then
        //Todo Change exception Type (implementing in other MR)
        assertThatExceptionOfType(ClientConfigurationException.class)
                .isThrownBy(() -> ArtifactCreationStrategy.getStrategyOfString(strategy));

    }


}
