/*
 * ============LICENSE_START=======================================================
 * PROJECT
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

package org.onap.aaf.certservice.certification.configuration;

import org.junit.jupiter.api.Test;
import org.onap.aaf.certservice.certification.configuration.model.Cmpv2Server;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class CmpServersConfigLoaderTest {
    private static final String existingConfigFilename = "cmpServers.json";
    private static final String nonexistingConfigFilename = "nonexisting_cmpServers.json";
    private static final Map<String, String> expectedFirstCmpServer = Map.of(
            "CA_NAME", "TEST",
            "URL", "http://127.0.0.1/ejbca/publicweb/cmp/cmp",
            "ISSUER_DN", "CN=ManagementCA",
            "CA_MODE", "CLIENT",
            "IAK", "xxx",
            "RV", "yyy"
    );
    private static final Map<String, String> expectedSecondCmpServer = Map.of(
            "CA_NAME", "TEST2",
            "URL", "http://127.0.0.1/ejbca/publicweb/cmp/cmpRA",
            "ISSUER_DN", "CN=ManagementCA2",
            "CA_MODE", "RA",
            "IAK", "xxx",
            "RV", "yyy"
    );

    @Test
    public void shouldLoadCmpServersConfigWhenFileAvailable() throws IOException {
        // Given
        String path = getClass().getClassLoader().getResource(existingConfigFilename).getFile();

        // When
        List<Cmpv2Server> cmpServers = new CmpServersConfigLoader().load(path);

        // Then
        assertThat(cmpServers).isNotNull();
        assertThat(cmpServers).hasSize(2);
        verifyThatCmpServerEquals(cmpServers.get(0), expectedFirstCmpServer);
        verifyThatCmpServerEquals(cmpServers.get(1), expectedSecondCmpServer);
    }

    @Test()
    public void shouldReturnEmptyListWhenFileMissing() {
        // When
        List<Cmpv2Server> cmpServers = new CmpServersConfigLoader().load(nonexistingConfigFilename);

        // Then
        assertThat(cmpServers).isNotNull();
        assertThat(cmpServers).isEmpty();
    }

    private void verifyThatCmpServerEquals(Cmpv2Server cmpv2Server, Map<String, String> expected) {
        assertThat(cmpv2Server.getCaName()).isEqualTo(expected.get("CA_NAME"));
        assertThat(cmpv2Server.getUrl()).isEqualTo(expected.get("URL"));
        assertThat(cmpv2Server.getIssuerDN()).isEqualTo(expected.get("ISSUER_DN"));
        assertThat(cmpv2Server.getCaMode().name()).isEqualTo(expected.get("CA_MODE"));
        assertThat(cmpv2Server.getAuthentication().getIak()).isEqualTo(expected.get("IAK"));
        assertThat(cmpv2Server.getAuthentication().getRv()).isEqualTo(expected.get("RV"));
    }
}