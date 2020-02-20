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

import org.onap.aaf.certservice.certification.configuration.model.Cmpv2Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Collections;
import java.util.List;

@Configuration
public class CmpServersConfig {

    private static final String CMP_SERVERS_CONFIG_FILENAME = "cmpServers.json";
    private String configPath;
    private List<Cmpv2Server> cmpServers;
    private final CmpServersConfigLoader cmpServersConfigLoader;

    @Autowired
    public CmpServersConfig(@Value("${app.config.path}") String configPath,
                            CmpServersConfigLoader cmpServersConfigLoader) {
        this.cmpServersConfigLoader = cmpServersConfigLoader;
        this.configPath = configPath;
    }

    @PostConstruct
    void loadConfiguration() {
        String configFilePath = configPath + File.separator + CMP_SERVERS_CONFIG_FILENAME;
        this.cmpServers = Collections.unmodifiableList(cmpServersConfigLoader.load(configFilePath));
    }

    public List<Cmpv2Server> getCmpServers() {
        return cmpServers;
    }
}
