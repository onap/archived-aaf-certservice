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

package org.onap.aaf.certservice.client.common;

public interface CsrConfigurationConstants {
    String ENV_COMMON_NAME = "COMMON_NAME";
    String ENV_ORGANIZATION = "ORGANIZATION";
    String ENV_ORGANIZATION_UNIT = "ORGANIZATION_UNIT";
    String ENV_LOCATION = "LOCATION";
    String ENV_STATE = "STATE";
    String ENV_COUNTRY = "COUNTRY";
    String ENV_SUBJECT_ALTERNATIVES_NAME = "SANS";
}
