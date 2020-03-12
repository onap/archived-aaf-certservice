/*
 * ============LICENSE_START=======================================================
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

package org.onap.aaf.certservice.client.configuration.factory;

import org.onap.aaf.certservice.client.configuration.exception.ClientConfigurationException;
import org.onap.aaf.certservice.client.configuration.exception.CsrConfigurationException;
import org.onap.aaf.certservice.client.configuration.model.ConfigurationModel;

import java.util.regex.Pattern;

public abstract class AbstractConfigurationFactory<T extends ConfigurationModel> {

    abstract T create() throws ClientConfigurationException, CsrConfigurationException;

    public Boolean isPathValid(String path) {
        return path.matches("^/|(/[a-zA-Z0-9_-]+)+/?$");
    }

    public Boolean isAlphaNumeric(String caName) {
        return caName.matches("^[a-zA-Z0-9]*$");
    }

    public Boolean isCountryValid(String country) {
        return country.matches("^([A-Z][A-Z])$");
    }

    public Boolean isCommonNameValid(String commonName) {
        return !isSpecialCharsPresent(commonName) &&
                !isHttpProtocolsPresent(commonName) &&
                !isIpAddressPresent(commonName) &&
                !isPortNumberPresent(commonName);
    }

    public Boolean isSpecialCharsPresent(String stringToCheck) {
        return Pattern.compile("[~#@*$+%!()?/{}<>\\|_^]").matcher(stringToCheck).find();
    }

    private Boolean isPortNumberPresent(String stringToCheck) {
        return Pattern.compile(":[0-9]{1,5}").matcher(stringToCheck).find();
    }

    private Boolean isIpAddressPresent(String stringToCheck) {
        return Pattern.compile("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}").matcher(stringToCheck).find();
    }

    private Boolean isHttpProtocolsPresent(String stringToCheck) {
        return Pattern.compile("[h][t][t][p][:][/][/]|[h][t][t][p][s][:][/][/]").matcher(stringToCheck).find();
    }
}
