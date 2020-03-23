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

package org.onap.aaf.certservice.certification.configuration.validation;


import org.bouncycastle.asn1.x500.X500Name;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.onap.aaf.certservice.CertServiceApplication;
import org.onap.aaf.certservice.certification.configuration.CmpServersConfigLoadingException;
import org.onap.aaf.certservice.certification.configuration.model.Authentication;
import org.onap.aaf.certservice.certification.configuration.model.CaMode;
import org.onap.aaf.certservice.certification.configuration.model.Cmpv2Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.security.InvalidParameterException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CertServiceApplication.class)
class Cmpv2ServersConfigurationValidatorTest {

    @Autowired
    private Cmpv2ServersConfigurationValidator validator;


    @Test
    void shouldThrowExceptionWhenValidationOfSingleServerConfigFails() {
        // Given
        Cmpv2Server server = getServer();
        server.setCaName("");
        List<Cmpv2Server> servers = List.of(server);

        // When
        Exception exception = assertThrows(
                InvalidParameterException.class,
                () -> validator.validate(servers));

        // Then
        assertThat(exception.getMessage()).contains("caName", "length must be between 1 and 128");
    }

    @Test
    void shouldThrowExceptionWhenCaNamesAreNotUnique() {
        // Given
        List<Cmpv2Server> servers = List.of(getServer(), getServer());

        // When
        Exception exception = assertThrows(
                InvalidParameterException.class,
                () -> validator.validate(servers));

        // Then
        assertThat(exception.getMessage()).contains("CA names are not unique within given CMPv2 servers");
    }

    private Cmpv2Server getServer() {
        Cmpv2Server server = new Cmpv2Server();
        server.setCaName("NOTUNIQUECANAME");
        server.setCaMode(CaMode.CLIENT);
        server.setIssuerDN(new X500Name("CN=ManagementCA"));
        server.setUrl("http://127.0.0.1/ejbca/publicweb/cmp/cmp");
        server.setAuthentication(getAuthentication());
        return server;
    }

    private Authentication getAuthentication() {
        Authentication authentication = new Authentication();
        authentication.setRv("testRV");
        authentication.setIak("testIAK");
        return authentication;
    }

}