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


package org.onap.aaf.certservice.certification.configuration.validation.url.violations;

import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class RequestTypeViolation implements URLServerViolation {

    private final static List<String> validRequests = Collections.singletonList("http");

    @Override
    public boolean validate(String serverUrl) {
        try {
            AtomicBoolean isValid = new AtomicBoolean(false);
            String protocol = new URL(serverUrl).getProtocol();
            validRequests.forEach(requestType -> {
                if (protocol.equals(requestType)) {
                    isValid.set(true);
                }
            });
            return isValid.get();
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
