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

import static org.assertj.core.api.Assertions.assertThat;


class ArtifactsCreatorProviderTest {

    private static final String P12 = "P12";
    private static final String JKS = "JKS";
    private static final String PEM = "PEM";
    private static final String TEST_PATH = "testPath";

    @Test
    void artifactsProviderShouldReturnP12Creator(){

        // when
        ArtifactsCreator artifactsCreator =
                ArtifactsCreatorProvider.getCreator(P12, TEST_PATH);
        // then
        assertThat(artifactsCreator).isInstanceOf(ConvertedArtifactsCreator.class);
        ConvertedArtifactsCreator p12Creator = (ConvertedArtifactsCreator) artifactsCreator;
        String jksExtension = ".p12";
        assertThat(p12Creator.getFileExtension()).isEqualTo(jksExtension);
    }

    @Test
    void artifactsProviderShouldReturnJKSCreator(){

        // when
        ArtifactsCreator artifactsCreator =
                ArtifactsCreatorProvider.getCreator(JKS, TEST_PATH);
        // then
        assertThat(artifactsCreator).isInstanceOf(ConvertedArtifactsCreator.class);
        ConvertedArtifactsCreator jksCreator = (ConvertedArtifactsCreator) artifactsCreator;
        String jksExtension = ".jks";
        assertThat(jksCreator.getFileExtension()).isEqualTo(jksExtension);
    }

    @Test
    void artifactsProviderShouldReturnPemCreator(){

        // when
        ArtifactsCreator artifactsCreator =
            ArtifactsCreatorProvider.getCreator(PEM, TEST_PATH);
        // then
        assertThat(artifactsCreator).isInstanceOf(PemArtifactsCreator.class);
    }
}
