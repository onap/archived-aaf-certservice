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

package org.onap.aaf.certservice.client.configuration;

import static org.onap.aaf.certservice.client.configuration.EnvProvider.readEnvVariable;

public class EnvsForCsr {
    private String commonName;
    private String organization;
    private String organizationUnit;
    private String location;
    private String state;
    private String country;
    private String subjectAlternativesName;

    public EnvsForCsr() {
        this.commonName = readEnvVariable(CsrConfigurationEnvs.COMMON_NAME.toString());
        this.organization = readEnvVariable(CsrConfigurationEnvs.ORGANIZATION.toString());
        this.organizationUnit = readEnvVariable(CsrConfigurationEnvs.ORGANIZATION_UNIT.toString());
        this.location = readEnvVariable(CsrConfigurationEnvs.LOCATION.toString());
        this.state = readEnvVariable(CsrConfigurationEnvs.STATE.toString());
        this.country = readEnvVariable(CsrConfigurationEnvs.COUNTRY.toString());
        this.subjectAlternativesName = readEnvVariable(CsrConfigurationEnvs.SANS.toString());
    }

    public String getCommonName() {
        return commonName;
    }

    public String getOrganization() {
        return organization;
    }

    public String getOrganizationUnit() {
        return organizationUnit;
    }

    public String getLocation() {
        return location;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }


    public String getSubjectAlternativesName() {
        return subjectAlternativesName;
    }
}
