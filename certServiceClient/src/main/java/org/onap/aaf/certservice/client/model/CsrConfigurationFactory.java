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

    private String commonName;
    private String organization;
    private String organizationUnit;
    private String location;
    private String state;
    private String country;
    private String subjectAlternativesName;


    CsrConfigurationFactory() {
        EnvProvider envProvider = new EnvProvider();
        this.commonName = envProvider.readEnvVariable(CsrConfigurationEnvs.COMMON_NAME.toString());
        this.organization = envProvider.readEnvVariable(CsrConfigurationEnvs.ORGANIZATION.toString());
        this.organizationUnit = envProvider.readEnvVariable(CsrConfigurationEnvs.ORGANIZATION_UNIT.toString());
        this.location = envProvider.readEnvVariable(CsrConfigurationEnvs.LOCATION.toString());
        this.state = envProvider.readEnvVariable(CsrConfigurationEnvs.STATE.toString());
        this.country = envProvider.readEnvVariable(CsrConfigurationEnvs.COUNTRY.toString());
        this.subjectAlternativesName = envProvider.readEnvVariable(CsrConfigurationEnvs.SANS.toString());
    }


    @Override
    public CsrConfiguration create() {

        CsrConfiguration configuration = new CsrConfiguration();

        Optional.ofNullable(commonName).filter(EnvValidationUtils::isEnvExists)
                .filter(EnvValidationUtils::isCommonNameValid)
                .map(configuration::setCommonName)
                .orElseThrow(() -> new CsrConfigurationException(CsrConfigurationEnvs.COMMON_NAME + " is invalid."));

        Optional.ofNullable(organization).filter(EnvValidationUtils::isEnvExists)
                .filter(org -> !EnvValidationUtils.isSpecialCharsPresent(org))
                .map(configuration::setOrganization)
                .orElseThrow(() -> new CsrConfigurationException(CsrConfigurationEnvs.ORGANIZATION + " is invalid."));

        Optional.ofNullable(state).filter(EnvValidationUtils::isEnvExists)
                .map(configuration::setState)
                .orElseThrow(() -> new CsrConfigurationException(CsrConfigurationEnvs.STATE + " is invalid."));

        Optional.ofNullable(country).filter(EnvValidationUtils::isEnvExists)
                .filter(EnvValidationUtils::isCountryValid)
                .map(configuration::setCountry)
                .orElseThrow(() -> new CsrConfigurationException(CsrConfigurationEnvs.COUNTRY + " is invalid."));

        Optional.ofNullable(organizationUnit).filter(EnvValidationUtils::isEnvExists)
                .map(configuration::setOrganizationUnit);

        Optional.ofNullable(location).filter(EnvValidationUtils::isEnvExists)
                .map(configuration::setLocation);

        Optional.ofNullable(subjectAlternativesName).filter(EnvValidationUtils::isEnvExists)
                .map(configuration::setSubjectAlternativeNames);

        return configuration;
    }
}
