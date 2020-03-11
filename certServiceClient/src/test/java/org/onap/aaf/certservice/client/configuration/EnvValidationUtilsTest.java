/*============LICENSE_START=======================================================
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

package org.onap.aaf.certservice.client.configuration;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EnvValidationUtilsTest {

    @ParameterizedTest
    @ValueSource(strings = {"/var/log", "/", "/var/log/", "/second_var", "/second-var"})
    public void shouldAcceptValidPath(String path){
        assertTrue(EnvValidationUtils.isPathValid(path));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/var/log?", "", "var_", "var", "//", "/var//log"})
    public void shouldRejectInvalidPath(String path){
        assertFalse(EnvValidationUtils.isPathValid(path));
    }

    @ParameterizedTest
    @ValueSource(strings = {"PL", "DE", "PT", "US"})
    public void shouldAcceptValidCountryCode(String countryCode){
        assertTrue(EnvValidationUtils.isCountryValid(countryCode));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1P", "PLP", "P#", "&*"})
    public void shouldRejectInvalidCountryCode(String countryCode){
        assertFalse(EnvValidationUtils.isCountryValid(countryCode));
    }

    @ParameterizedTest
    @ValueSource(strings = {"caname", "caname1", "123caName", "ca1name"})
    public void shouldAcceptValidAlphanumeric(String caName){
        assertTrue(EnvValidationUtils.isAlphaNumeric(caName));
    }

    @ParameterizedTest
    @ValueSource(strings = {"44caname$", "#caname1", "1c_aname", "ca1-name"})
    public void shouldRejectInvalidAlphanumeric(String caName){
        assertFalse(EnvValidationUtils.isAlphaNumeric(caName));
    }

    @ParameterizedTest
    @ValueSource(strings = {"example.com", "www.example.com"})
    public void shouldAcceptValidCommonName(String commonName){
        assertTrue(EnvValidationUtils.isCommonNameValid(commonName));
    }

    @ParameterizedTest
    @ValueSource(strings = {"https://example.com", "http://example.com", "example.com:8080", "0.0.0.0", "@#$%.com"})
    public void shouldRejectInvalidCommonName(String commonName){
        assertFalse(EnvValidationUtils.isCommonNameValid(commonName));
    }
}