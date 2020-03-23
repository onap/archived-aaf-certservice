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

package org.onap.aaf.certservice.certification.configuration.validation;

import org.onap.aaf.certservice.certification.configuration.model.Cmpv2Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;

@Service
public class Cmpv2ServersConfigurationValidator {

    private final Cmpv2ServerConfigurationValidator validator;

    @Autowired
    public Cmpv2ServersConfigurationValidator(Cmpv2ServerConfigurationValidator validator) {
        this.validator = validator;
    }

    public void validate(List<Cmpv2Server> servers) {
        servers.forEach(validator::validate);
        validateUniqueCaNames(servers);
    }

    private void validateUniqueCaNames(List<Cmpv2Server> servers) {
        long distinctCAs = getNumberOfUniqueCaNames(servers);
        if (servers.size() != distinctCAs) {
            throw new InvalidParameterException("CA names are not unique within given CMPv2 servers");
        }
    }

    private long getNumberOfUniqueCaNames(List<Cmpv2Server> servers) {
        return servers.stream().map(Cmpv2Server::getCaName)
                .distinct()
                .count();
    }

}
