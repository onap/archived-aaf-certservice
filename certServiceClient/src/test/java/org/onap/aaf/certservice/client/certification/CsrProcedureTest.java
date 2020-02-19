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
import org.onap.aaf.certservice.client.model.CsrConfiguration;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.onap.aaf.certservice.client.certification.EncryptionAlgorithmConstants.KEY_SIZE;
import static org.onap.aaf.certservice.client.certification.EncryptionAlgorithmConstants.RSA_ENCRYPTION_ALGORITHM;

public class CsrProcedureTest {

    final String COMMON_NAME_VALID = "onap.org";
    final String SANS_VALID = "dupa.com:dupa.com.pl:dupa.pl";
    final String COUNTRY_VALID = "US";
    final String LOCATION_VALID = "San-Francisco";
    final String ORGANIZATION_VALID =  "Linux-Foundation";
    final String ORGANIZATION_UNIT_VALID = "ONAP";
    final String STATE_VALID = "California";

    CsrConfiguration config = mock(CsrConfiguration.class);
    KeyPairFactory keyPair = new KeyPairFactory(RSA_ENCRYPTION_ALGORITHM, KEY_SIZE);

    @Test
    void createCsrInPem_shouldSucceed() throws IOException {
        when(config.getCommonName()).thenReturn(COMMON_NAME_VALID);
        when(config.getSans()).thenReturn(SANS_VALID);
        when(config.getCountry()).thenReturn(COUNTRY_VALID);
        when(config.getLocation()).thenReturn(LOCATION_VALID);
        when(config.getOrganization()).thenReturn(ORGANIZATION_VALID);
        when(config.getOrganizationUnit()).thenReturn(ORGANIZATION_UNIT_VALID);
        when(config.getState()).thenReturn(STATE_VALID);

        CsrProcedure csr = new CsrProcedure(keyPair.create());

        System.out.println(csr.createCsrInPem(config));
     }
}
