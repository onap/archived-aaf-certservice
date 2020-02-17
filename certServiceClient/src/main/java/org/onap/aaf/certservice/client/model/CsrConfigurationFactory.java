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

import org.onap.aaf.certservice.client.common.CsrConfigurationEnvs;
import org.onap.aaf.certservice.client.common.EnvProvider;
import org.onap.aaf.certservice.client.common.EnvValidationUtils;
import org.onap.aaf.certservice.client.exceptions.CsrConfigurationException;

import java.util.Optional;

class CsrConfigurationFactory implements AbstractConfigurationFactory<CsrConfiguration> {

    @Override
    public CsrConfiguration create() {
        String commonName = EnvProvider.readEnvVariable(CsrConfigurationEnvs.COMMON_NAME.toString());
        String organization = EnvProvider.readEnvVariable(CsrConfigurationEnvs.ORGANIZATION.toString());
        String organizationUnit = EnvProvider.readEnvVariable(CsrConfigurationEnvs.ORGANIZATION_UNIT.toString());
        String location = EnvProvider.readEnvVariable(CsrConfigurationEnvs.LOCATION.toString());
        String state = EnvProvider.readEnvVariable(CsrConfigurationEnvs.STATE.toString());
        String country = EnvProvider.readEnvVariable(CsrConfigurationEnvs.COUNTRY.toString());
        String subjectAlternativesName = EnvProvider.readEnvVariable(CsrConfigurationEnvs.SANS.toString());

        ImmutableCsrConfiguration.Builder csrConfigBuilder = ImmutableCsrConfiguration.builder();

        Optional.ofNullable(commonName).filter(EnvValidationUtils::isEnvExists)
                .filter(EnvValidationUtils::isCommonNameValid)
                .map(csrConfigBuilder::commonName)
                .orElseThrow(() -> new CsrConfigurationException(CsrConfigurationEnvs.COMMON_NAME + " is invalid."));

        Optional.ofNullable(organization).filter(EnvValidationUtils::isEnvExists)
                .filter(org -> !EnvValidationUtils.isSpecialCharsPresent(org))
                .map(csrConfigBuilder::organization)
                .orElseThrow(() -> new CsrConfigurationException(CsrConfigurationEnvs.ORGANIZATION + " is invalid."));

        Optional.ofNullable(state).filter(EnvValidationUtils::isEnvExists)
                .map(csrConfigBuilder::state)
                .orElseThrow(() -> new CsrConfigurationException(CsrConfigurationEnvs.STATE + " is invalid."));

        Optional.ofNullable(country).filter(EnvValidationUtils::isEnvExists)
                .filter(EnvValidationUtils::isCountryValid)
                .map(csrConfigBuilder::country)
                .orElseThrow(() -> new CsrConfigurationException(CsrConfigurationEnvs.COUNTRY + " is invalid."));

        Optional.ofNullable(organizationUnit).filter(EnvValidationUtils::isEnvExists)
                .map(csrConfigBuilder::organizationUnit);

        Optional.ofNullable(location).filter(EnvValidationUtils::isEnvExists)
                .map(csrConfigBuilder::location);

        Optional.ofNullable(subjectAlternativesName).filter(EnvValidationUtils::isEnvExists)
                .map(csrConfigBuilder::subjectAlternativeNames);

        return csrConfigBuilder.build();
    }
}
