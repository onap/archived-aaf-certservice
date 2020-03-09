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

package org.onap.aaf.certservice.client.httpclient.exception;

import org.onap.aaf.certservice.client.api.ExitCode;
import org.onap.aaf.certservice.client.api.ExitableException;

public class CertServiceApiResponseException extends ExitableException {
    private static final ExitCode EXIT_CODE = ExitCode.CERT_SERVICE_API_CONNECTION_EXCEPTION;

    public CertServiceApiResponseException(String url, int responseCode, String messageFromAPI) {

        super(String.format("Request failed for URL '%s'. Response code: %d . Message from API: %s",
                url,
                responseCode,
                messageFromAPI));
    }

    @Override
    public int applicationExitCode() {
        return EXIT_CODE.getValue();
    }

}
