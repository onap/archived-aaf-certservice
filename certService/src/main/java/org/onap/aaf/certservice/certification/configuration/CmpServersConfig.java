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

package org.onap.aaf.certservice.certification.configuration;

import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import org.onap.aaf.certservice.certification.configuration.model.Cmpv2Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CmpServersConfig {

    private static final String CMP_SERVERS_CONFIG_FILENAME = "cmpServers.json";
    private List<Cmpv2Server> cmpServers;
    private CmpServersConfigLoader configLoader;

    @Autowired
    public CmpServersConfig(CmpServersConfigLoader configLoader) {
        this.configLoader = configLoader;
    }

    @PostConstruct
    private void loadConfiguration() {
        cmpServers = Collections.unmodifiableList(configLoader.load(CMP_SERVERS_CONFIG_FILENAME));
    }

    public List<Cmpv2Server> getCmpServers() {
        return cmpServers;
    }
}
