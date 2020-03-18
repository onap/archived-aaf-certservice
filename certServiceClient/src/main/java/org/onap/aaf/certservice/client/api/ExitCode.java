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
package org.onap.aaf.certservice.client.api;

public enum ExitCode {
    SUCCESS_EXIT_CODE(0),
    CLIENT_CONFIGURATION_EXCEPTION(1),
    CSR_CONFIGURATION_EXCEPTION(2),
    KEY_PAIR_GENERATION_EXCEPTION(3),
    CSR_GENERATION_EXCEPTION(4),
    CERT_SERVICE_API_CONNECTION_EXCEPTION(5),
    HTTP_CLIENT_EXCEPTION(6),
    PKCS12_CONVERSION_EXCEPTION(7),
    PK_TO_PEM_ENCODING_EXCEPTION(8);

    private final int value;

    ExitCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}