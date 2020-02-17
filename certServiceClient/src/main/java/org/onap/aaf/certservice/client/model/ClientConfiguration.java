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

import org.immutables.value.Value;
import org.onap.aaf.certservice.client.common.ClientConfigurationConstants;
import org.onap.aaf.certservice.client.exceptions.ClientConfigurationException;

import java.util.Optional;

@Value.Immutable(prehash = true)
public interface ClientConfiguration {

    Integer DEFAULT_TIMEOUT_MS = 30000;
    String DEFAULT_REQUEST_URL = "http://cert-service:8080/v1/certificate/";



    @Value.Default
    default String urlToCertService() {
        return DEFAULT_REQUEST_URL;
    }

    @Value.Default
    default Integer requestTimeout() {
        return DEFAULT_TIMEOUT_MS;
    }

    @Value.Parameter
    String certsOutputPath();

    @Value.Parameter
    String caName();

    static ClientConfiguration readEnvVariables() throws ClientConfigurationException {

        String urlToCertService = System.getenv(ClientConfigurationConstants.ENV_URL_TO_CERT_SERVICE);
        String requestTimeOut = System.getenv(ClientConfigurationConstants.ENV_TIMEOUT_FOR_REST_API_CALL);
        String outputPath = System.getenv(ClientConfigurationConstants.ENV_CERTS_OUTPUT_PATH);
        String caName = System.getenv(ClientConfigurationConstants.ENV_CA_NAME);

        ImmutableClientConfiguration.Builder clientConfigBuilder = ImmutableClientConfiguration.builder();

        Optional.ofNullable(urlToCertService).filter(ClientConfiguration::isEnvExists)
                .map(clientConfigBuilder::urlToCertService);

        Optional.ofNullable(requestTimeOut).filter(ClientConfiguration::isEnvExists)
                .map(timeout -> clientConfigBuilder.requestTimeout(Integer.valueOf(timeout)));


        Optional.ofNullable(outputPath).filter(ClientConfiguration::isEnvExists)
                .filter(ClientConfiguration::isPathValid)
                .map(clientConfigBuilder::certsOutputPath)
                .orElseThrow(() -> new ClientConfigurationException(ClientConfigurationConstants.ENV_CERTS_OUTPUT_PATH + " is invalid"));

        Optional.ofNullable(caName).filter(ClientConfiguration::isEnvExists)
                .filter(ClientConfiguration::isAlphaNumeric)
                .map(clientConfigBuilder::caName)
                .orElseThrow(() -> new ClientConfigurationException(ClientConfigurationConstants.ENV_CA_NAME + " is invalid."));


        return clientConfigBuilder.build();
    }


    static Boolean isPathValid(String path) {
         return path.matches("^/|(/[a-zA-Z0-9_-]+)+$");
    }

    static Boolean isAlphaNumeric(String caName) {
        return caName.matches("^[a-zA-Z0-9]*$");
    }

    static Boolean isEnvExists( String envValue) {
        return envValue != null && !"".equals(envValue);
    }

}
