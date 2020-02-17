package org.onap.aaf.certservice.client.model;

import org.junit.Rule;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import org.junit.jupiter.api.Test;
import org.onap.aaf.certservice.client.common.CsrConfigurationConstants;
import org.onap.aaf.certservice.client.exceptions.ClientConfigurationException;
import org.onap.aaf.certservice.client.exceptions.CsrConfigurationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class CsrConfigurationTest {

    @Rule
    public final EnvironmentVariables envs = new EnvironmentVariables();

    @Test
    public void readEnvVariables_shouldReturnConfigurationLoadedFromEnvironmentalVariables() {
        // given
        envs.set(CsrConfigurationConstants.ENV_COMMON_NAME, "onap.org");
        envs.set(CsrConfigurationConstants.ENV_SUBJECT_ALTERNATIVES_NAME, "test-name");
        envs.set(CsrConfigurationConstants.ENV_COUNTRY, "US");
        envs.set(CsrConfigurationConstants.ENV_LOCATION, "San-Francisco");
        envs.set(CsrConfigurationConstants.ENV_ORGANIZATION, "Linux-Foundation");
        envs.set(CsrConfigurationConstants.ENV_ORGANIZATION_UNIT, "ONAP");
        envs.set(CsrConfigurationConstants.ENV_STATE, "California");

        // when
        CsrConfiguration configuration = CsrConfiguration.readEnvVariables();

        // then
        assertThat(configuration.commonName()).isEqualTo("onap.org");
        assertThat(configuration.subjectAlternativeNames()).isEqualTo("test-name");
        assertThat(configuration.country()).isEqualTo("US");
        assertThat(configuration.location()).isEqualTo("San-Francisco");
        assertThat(configuration.organization()).isEqualTo("Linux-Foundation");
        assertThat(configuration.organizationUnit()).isEqualTo("ONAP");
        assertThat(configuration.state()).isEqualTo("California");
    }

    @Test
    public void readEnvVariables_passWhenNotRequiredVariablesAreNotExist() throws ClientConfigurationException {
        // given
        envs.set(CsrConfigurationConstants.ENV_COMMON_NAME, "onap.org");
        envs.set(CsrConfigurationConstants.ENV_SUBJECT_ALTERNATIVES_NAME, null);
        envs.set(CsrConfigurationConstants.ENV_COUNTRY, "US");
        envs.set(CsrConfigurationConstants.ENV_LOCATION, null);
        envs.set(CsrConfigurationConstants.ENV_ORGANIZATION, "Linux-Foundation");
        envs.set(CsrConfigurationConstants.ENV_ORGANIZATION_UNIT, null);
        envs.set(CsrConfigurationConstants.ENV_STATE, "California");

        // when
        CsrConfiguration configuration = CsrConfiguration.readEnvVariables();

        // then
        assertThat(configuration.commonName()).isEqualTo("onap.org");
        assertThat(configuration.country()).isEqualTo("US");
        assertThat(configuration.organization()).isEqualTo("Linux-Foundation");
        assertThat(configuration.state()).isEqualTo("California");
    }

    @Test
    public void readEnvVariables_shouldReturnExceptionWhenRequiredEnvIsNotProvided() {
        // given
        envs.set(CsrConfigurationConstants.ENV_COMMON_NAME, null);
        envs.set(CsrConfigurationConstants.ENV_SUBJECT_ALTERNATIVES_NAME, "test-name");
        envs.set(CsrConfigurationConstants.ENV_COUNTRY, "US");
        envs.set(CsrConfigurationConstants.ENV_LOCATION, "San-Francisco");
        envs.set(CsrConfigurationConstants.ENV_ORGANIZATION, "Linux-Foundation");
        envs.set(CsrConfigurationConstants.ENV_ORGANIZATION_UNIT, "ONAP");
        envs.set(CsrConfigurationConstants.ENV_STATE, "California");

        // then
        assertThatExceptionOfType(CsrConfigurationException.class)
                .isThrownBy(CsrConfiguration::readEnvVariables)
                .withMessageContaining(CsrConfigurationConstants.ENV_COMMON_NAME + " is invalid");

    }

    @Test
    public void readEnvVariables_shouldReturnExceptionWhenRequiredEnvIsNotValid() {
        // given
        envs.set(CsrConfigurationConstants.ENV_COMMON_NAME, "onap.org");
        envs.set(CsrConfigurationConstants.ENV_SUBJECT_ALTERNATIVES_NAME, "test-name");
        envs.set(CsrConfigurationConstants.ENV_COUNTRY, "US");
        envs.set(CsrConfigurationConstants.ENV_LOCATION, "San-Francisco");
        envs.set(CsrConfigurationConstants.ENV_ORGANIZATION, "Linux-Foundation#");
        envs.set(CsrConfigurationConstants.ENV_ORGANIZATION_UNIT, "ONAP");
        envs.set(CsrConfigurationConstants.ENV_STATE, "California");

        // then
        assertThatExceptionOfType(CsrConfigurationException.class)
                .isThrownBy(CsrConfiguration::readEnvVariables)
                .withMessageContaining(CsrConfigurationConstants.ENV_ORGANIZATION + " is invalid.");

    }
}
