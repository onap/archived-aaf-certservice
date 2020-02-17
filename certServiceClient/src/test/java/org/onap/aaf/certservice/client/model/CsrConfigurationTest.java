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

public class CsrConfigurationTest {

    @Rule
    public final EnvironmentVariables envs = new EnvironmentVariables();

    @Test
    public void readEnvVariables_shouldReturnConfigurationLoadedFromEnvironmentalVariables() {
//        // given
//        envs.set(CsrConfigurationConstants.ENV_COMMON_NAME, "onap.org");
//        envs.set(CsrConfigurationConstants.ENV_SUBJECT_ALTERNATIVES_NAME, "test-name");
//        envs.set(CsrConfigurationConstants.ENV_COUNTRY, "US");
//        envs.set(CsrConfigurationConstants.ENV_LOCATION, "San-Francisco");
//        envs.set(CsrConfigurationConstants.ENV_ORGANIZATION, "Linux-Foundation");
//        envs.set(CsrConfigurationConstants.ENV_ORGANIZATION_UNIT, "ONAP");
//        envs.set(CsrConfigurationConstants.ENV_STATE, "California");
//
//        // when
//        CsrConfiguration configuration = CsrConfiguration.readEnvVariables();
//
//        // then
//        assertThat(configuration.commonName()).isEqualTo("onap.org");
//        assertThat(configuration.subjectAlternativeNames()).isEqualTo("test-name");
//        assertThat(configuration.country()).isEqualTo("US");
//        assertThat(configuration.location()).isEqualTo("San-Francisco");
//        assertThat(configuration.organization()).isEqualTo("Linux-Foundation");
//        assertThat(configuration.organizationUnit()).isEqualTo("ONAP");
//        assertThat(configuration.state()).isEqualTo("California");
    }

    @Test
    public void readEnvVariables_passWhenNotRequiredVariablesAreNotExist() throws ClientConfigurationException {
//        // given
//        envs.set(CsrConfigurationConstants.ENV_COMMON_NAME, "onap.org");
//        envs.set(CsrConfigurationConstants.ENV_SUBJECT_ALTERNATIVES_NAME, null);
//        envs.set(CsrConfigurationConstants.ENV_COUNTRY, "US");
//        envs.set(CsrConfigurationConstants.ENV_LOCATION, null);
//        envs.set(CsrConfigurationConstants.ENV_ORGANIZATION, "Linux-Foundation");
//        envs.set(CsrConfigurationConstants.ENV_ORGANIZATION_UNIT, null);
//        envs.set(CsrConfigurationConstants.ENV_STATE, "California");
//
//        // when
//        CsrConfiguration configuration = CsrConfiguration.readEnvVariables();
//
//        // then
//        assertThat(configuration.commonName()).isEqualTo("onap.org");
//        assertThat(configuration.country()).isEqualTo("US");
//        assertThat(configuration.organization()).isEqualTo("Linux-Foundation");
//        assertThat(configuration.state()).isEqualTo("California");
    }

    @Test
    public void readEnvVariables_shouldReturnExceptionWhenRequiredEnvIsNotProvided() {
//        // given
//        envs.set(CsrConfigurationConstants.ENV_COMMON_NAME, null);
//        envs.set(CsrConfigurationConstants.ENV_SUBJECT_ALTERNATIVES_NAME, "test-name");
//        envs.set(CsrConfigurationConstants.ENV_COUNTRY, "US");
//        envs.set(CsrConfigurationConstants.ENV_LOCATION, "San-Francisco");
//        envs.set(CsrConfigurationConstants.ENV_ORGANIZATION, "Linux-Foundation");
//        envs.set(CsrConfigurationConstants.ENV_ORGANIZATION_UNIT, "ONAP");
//        envs.set(CsrConfigurationConstants.ENV_STATE, "California");
//
//        // then
//        assertThatExceptionOfType(CsrConfigurationException.class)
//                .isThrownBy(CsrConfiguration::readEnvVariables)
//                .withMessageContaining(CsrConfigurationConstants.ENV_COMMON_NAME + " is invalid");

    }

    @Test
    public void readEnvVariables_shouldReturnExceptionWhenRequiredEnvIsNotValid() {
//        // given
//        envs.set(CsrConfigurationConstants.ENV_COMMON_NAME, "onap.org");
//        envs.set(CsrConfigurationConstants.ENV_SUBJECT_ALTERNATIVES_NAME, "test-name");
//        envs.set(CsrConfigurationConstants.ENV_COUNTRY, "US");
//        envs.set(CsrConfigurationConstants.ENV_LOCATION, "San-Francisco");
//        envs.set(CsrConfigurationConstants.ENV_ORGANIZATION, "Linux-Foundation#");
//        envs.set(CsrConfigurationConstants.ENV_ORGANIZATION_UNIT, "ONAP");
//        envs.set(CsrConfigurationConstants.ENV_STATE, "California");
//
//        // then
//        assertThatExceptionOfType(CsrConfigurationException.class)
//                .isThrownBy(CsrConfiguration::readEnvVariables)
//                .withMessageContaining(CsrConfigurationConstants.ENV_ORGANIZATION + " is invalid.");

    }
}
