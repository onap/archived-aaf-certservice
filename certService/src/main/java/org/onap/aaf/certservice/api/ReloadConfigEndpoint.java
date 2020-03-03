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

package org.onap.aaf.certservice.api;

import org.onap.aaf.certservice.certification.configuration.CmpServersConfig;
import org.onap.aaf.certservice.certification.configuration.CmpServersConfig.LoadingStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReloadConfigEndpoint {

    private final CmpServersConfig cmpServersConfig;

    @Autowired
    public ReloadConfigEndpoint(CmpServersConfig cmpServersConfig) {
        this.cmpServersConfig = cmpServersConfig;
    }

    @GetMapping("/reload")
    public ResponseEntity<String> reload() {
        LoadingStatus loadingStatus = cmpServersConfig.reloadConfiguration();
        return getLoadingStatusResponse(loadingStatus);
    }

    private ResponseEntity<String> getLoadingStatusResponse(LoadingStatus loadingStatus) {
        if (loadingStatus.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(loadingStatus.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
