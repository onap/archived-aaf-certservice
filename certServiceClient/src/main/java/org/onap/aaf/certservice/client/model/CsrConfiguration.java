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
import org.jetbrains.annotations.Nullable;
import org.onap.aaf.certservice.client.common.CsrConfigurationConstants;
import org.onap.aaf.certservice.client.exceptions.CsrConfigurationException;

import java.util.Optional;
import java.util.regex.Pattern;

@Value.Immutable(prehash = true)
public interface CsrConfiguration {

    @Value.Parameter
    String commonName();

    @Value.Parameter
    String organization();

    @Value.Parameter
    String state();

    @Value.Parameter
    String country();

    @Value.Parameter
    @Nullable
    String organizationUnit();

    @Value.Parameter
    @Nullable
    String location();

    @Value.Parameter
    @Nullable
    String subjectAlternativeNames();

    
    static CsrConfiguration readEnvVariables() throws CsrConfigurationException {
        
        String commonName = System.getenv(CsrConfigurationConstants.ENV_COMMON_NAME);
        String organization = System.getenv(CsrConfigurationConstants.ENV_ORGANIZATION);
        String organizationUnit = System.getenv(CsrConfigurationConstants.ENV_ORGANIZATION_UNIT);
        String location = System.getenv(CsrConfigurationConstants.ENV_LOCATION);
        String state = System.getenv(CsrConfigurationConstants.ENV_STATE);
        String country = System.getenv(CsrConfigurationConstants.ENV_COUNTRY);
        String subjectAlternativesName = System.getenv(CsrConfigurationConstants.ENV_SUBJECT_ALTERNATIVES_NAME);

        ImmutableCsrConfiguration.Builder csrConfigBuilder = ImmutableCsrConfiguration.builder();

        Optional.ofNullable(commonName).filter(CsrConfiguration::isEnvExists)
                .filter(CsrConfiguration::isCommonNameValid)
                .map(csrConfigBuilder::commonName)
                .orElseThrow(() -> new CsrConfigurationException(CsrConfigurationConstants.ENV_COMMON_NAME + " is invalid."));

        Optional.ofNullable(organization).filter(CsrConfiguration::isEnvExists)
                .filter(org -> !isSpecialCharsPresent(org))
                .map(csrConfigBuilder::organization)
                .orElseThrow(() -> new CsrConfigurationException(CsrConfigurationConstants.ENV_ORGANIZATION + " is invalid."));

        Optional.ofNullable(state).filter(CsrConfiguration::isEnvExists)
                .map(csrConfigBuilder::state)
                .orElseThrow(() -> new CsrConfigurationException(CsrConfigurationConstants.ENV_STATE + " is invalid."));

        Optional.ofNullable(country).filter(CsrConfiguration::isEnvExists)
                .filter(CsrConfiguration::isCountryValid)
                .map(csrConfigBuilder::country)
                .orElseThrow(() -> new CsrConfigurationException(CsrConfigurationConstants.ENV_COUNTRY + " is invalid."));

        Optional.ofNullable(organizationUnit).filter(CsrConfiguration::isEnvExists)
                .map(csrConfigBuilder::organizationUnit);

        Optional.ofNullable(location).filter(CsrConfiguration::isEnvExists)
                .map(csrConfigBuilder::location);

        Optional.ofNullable(subjectAlternativesName).filter(CsrConfiguration::isEnvExists)
                .map(csrConfigBuilder::subjectAlternativeNames);


        return csrConfigBuilder.build();
    }

    static Boolean isEnvExists( String envValue) {
        return envValue != null && !"".equals(envValue);
    }

    static Boolean isCountryValid(String country) {
        return country.matches("^([A-Z][A-Z])$");
    }

    static Boolean isCommonNameValid(String commonName) {
        return !isSpecialCharsPresent(commonName) ||
               !isHttpProtocolsPresent(commonName) ||
               !isIpAddressPresent(commonName) ||
               !isPortNumberPresent(commonName);
    }

    static Boolean isPortNumberPresent(String stringToCheck) {
        return Pattern.compile(":[0-9]{1,5}").matcher(stringToCheck).find();
    }

    static Boolean isIpAddressPresent(String stringToCheck) {
        return Pattern.compile("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}").matcher(stringToCheck).find();
    }

    static Boolean isHttpProtocolsPresent(String stringToCheck) {
        return Pattern.compile("[h][t][t][p][:][/][/]|[h][t][t][p][s][:][/][/]").matcher(stringToCheck).find();
    }


    static Boolean isSpecialCharsPresent(String stringToCheck) {
        return Pattern.compile("[~#@*$+%!()?/{}<>\\|_^]").matcher(stringToCheck).find();
    }
}
