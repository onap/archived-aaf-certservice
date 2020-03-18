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
package org.onap.aaf.certservice.client;

import org.onap.aaf.certservice.client.api.ExitStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppExitHandler {
    public static final Logger LOGGER = LoggerFactory.getLogger(AppExitHandler.class);

    public void exit(ExitStatus exitStatus) {
        LOGGER.debug("Application exits with following message: {}", exitStatus.getMessage());
        LOGGER.debug("Application exits with following exit code: {}", exitStatus.getExitCodeValue());
        System.exit(exitStatus.getExitCodeValue());
    }
}
