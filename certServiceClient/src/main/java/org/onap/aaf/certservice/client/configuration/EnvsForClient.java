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

public class EnvsForClient {

    private String urlToCertService;
    private String requestTimeOut;
    private String outputPath;
    private String caName;

    public EnvsForClient() {
        this.urlToCertService = readEnvVariable(ClientConfigurationEnvs.REQUEST_URL.toString());
        this.requestTimeOut = readEnvVariable(ClientConfigurationEnvs.REQUEST_TIMEOUT.toString());
        this.outputPath = readEnvVariable(ClientConfigurationEnvs.OUTPUT_PATH.toString());
        this.caName = readEnvVariable(ClientConfigurationEnvs.CA_NAME.toString());
    }

    public String getUrlToCertService() {
        return urlToCertService;
    }

    public String getRequestTimeOut() {
        return requestTimeOut;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public String getCaName() {
        return caName;
    }
}
