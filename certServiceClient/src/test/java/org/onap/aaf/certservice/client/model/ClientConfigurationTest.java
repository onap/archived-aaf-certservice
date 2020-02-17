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

package org.onap.aaf.certservice.client.model;


import org.junit.Rule;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import org.junit.jupiter.api.Test;
import org.onap.aaf.certservice.client.exceptions.ClientConfigurationException;

public class ClientConfigurationTest {

    @Rule
    public final EnvironmentVariables envs = new EnvironmentVariables();

    @Test
    public void readEnvVariables_shouldReturnConfigurationLoadedFromEnvironmentalVariables() throws ClientConfigurationException {
//        // given
//        envs.set(ClientConfigurationEnvs.REQUEST_URL.toString(), "http://cert-service:8080/v1/certificate/");
//        envs.set(ClientConfigurationEnvs.REQUEST_TIMEOUT.toString(), "35000");
//        envs.set(ClientConfigurationEnvs.OUTPUT_PATH.toString(), "/opt/app/osaaf");
//        envs.set(ClientConfigurationEnvs.CA_NAME.toString(), "caaaftest2");
//
//        // when
//        ClientConfiguration configuration = ClientConfiguration.readEnvVariables();
//
//        // then
//        assertThat(configuration.caName()).isEqualTo("caaaftest2");
//        assertThat(configuration.requestTimeout()).isEqualTo(Integer.valueOf("35000"));
//        assertThat(configuration.certsOutputPath()).isEqualTo("/opt/app/osaaf");
//        assertThat(configuration.urlToCertService()).isEqualTo("http://cert-service:8080/v1/certificate/");
    }

    @Test
    public void readEnvVariables_shouldReturnDefaultValuesOfTwoVariables() throws ClientConfigurationException {
//        // given
//        envs.set(ClientConfigurationEnvs.REQUEST_URL.toString(), null);
//        envs.set(ClientConfigurationEnvs.REQUEST_TIMEOUT.toString(), "");
//        envs.set(ClientConfigurationEnvs.OUTPUT_PATH.toString(), "/opt/app/osaaf");
//        envs.set(ClientConfigurationEnvs.CA_NAME.toString(), "caaaftest2");
//
//        // when
//        ClientConfiguration configuration = ClientConfiguration.readEnvVariables();
//
//        // then
//        assertThat(configuration.caName()).isEqualTo("caaaftest2");
//        assertThat(configuration.requestTimeout()).isEqualTo(Integer.valueOf("30000"));
//        assertThat(configuration.certsOutputPath()).isEqualTo("/opt/app/osaaf");
//        assertThat(configuration.urlToCertService()).isEqualTo("http://cert-service:8080/v1/certificate/");
    }

    @Test
    public void readEnvVariables_shouldReturnExceptionWhenCaNameIsNotProvided() {
//        // given
//        envs.set(ClientConfigurationEnvs.REQUEST_URL.toString(), null);
//        envs.set(ClientConfigurationEnvs.REQUEST_TIMEOUT.toString(), "");
//        envs.set(ClientConfigurationEnvs.OUTPUT_PATH.toString(), "/opt/app/osaaf");
//        envs.set(ClientConfigurationEnvs.CA_NAME.toString(), null);
//
//        // then
//        assertThatExceptionOfType(ClientConfigurationException.class)
//                .isThrownBy(ClientConfiguration::readEnvVariables);

    }
}
