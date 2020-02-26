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

package org.onap.aaf.certservice.client.certification;


import org.junit.jupiter.api.Test;
import org.onap.aaf.certservice.client.configuration.model.CsrConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.onap.aaf.certservice.client.certification.EncryptionAlgorithmConstants.KEY_SIZE;
import static org.onap.aaf.certservice.client.certification.EncryptionAlgorithmConstants.RSA_ENCRYPTION_ALGORITHM;

public class CsrProcedureTest {

    CsrConfiguration config = mock(CsrConfiguration.class);
    KeyPairFactory keyPair = new KeyPairFactory(RSA_ENCRYPTION_ALGORITHM, KEY_SIZE);

    @Test
    void createCsrInPem_shouldSucceedWhenAllFieldsAreSetCorrectly() {
        when(config.getCommonName()).thenReturn("onap.org");
        when(config.getSans()).thenReturn("dupa.com:dupa.com.pl:dupa.pl");
        when(config.getCountry()).thenReturn("US");
        when(config.getLocation()).thenReturn("San-Francisco");
        when(config.getOrganization()).thenReturn("Linux-Foundation");
        when(config.getOrganizationUnit()).thenReturn("ONAP");
        when(config.getState()).thenReturn("California");

        assertThat(new CsrProcedure(keyPair.create()).createCsrInPem(config)).isNotEmpty();
     }
}

