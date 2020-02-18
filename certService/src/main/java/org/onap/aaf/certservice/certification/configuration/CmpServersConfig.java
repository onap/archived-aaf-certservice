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

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.onap.aaf.certservice.certification.CertificationModelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CmpServersConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(CertificationModelFactory.class);
    private static final String CMP_SERVERS_CONFIG_FILENAME = "cmpServers.json";
    private List<Cmpv2Server> cmpServers;

    @PostConstruct
    private void loadConfiguration() {
        try {
            cmpServers = this.loadConfigFromFile().getCmpv2Servers();
        } catch (IOException e) {
            LOGGER.error("Exception occured during CMP Servers configuration loading: ", e);
        }
    }

    public List<Cmpv2Server> getCmpServers() {
        return new ArrayList<>(cmpServers);
    }

    private CmpServers loadConfigFromFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String configFilePath = getClass().getClassLoader().getResource(CMP_SERVERS_CONFIG_FILENAME).getFile();
        return objectMapper.readValue(new File(configFilePath), CmpServers.class);
    }
}
