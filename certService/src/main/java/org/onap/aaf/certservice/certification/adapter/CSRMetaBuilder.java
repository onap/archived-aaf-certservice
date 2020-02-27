/*
 * ============LICENSE_START=======================================================
 * Cert Service
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

package org.onap.aaf.certservice.certification.adapter;

import java.security.KeyPair;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.CertException;
import org.onap.aaf.certservice.certification.configuration.model.Cmpv2Server;
import org.onap.aaf.certservice.certification.model.CsrModel;
import org.onap.aaf.certservice.cmpv2client.external.CSRMeta;
import org.onap.aaf.certservice.cmpv2client.external.RDN;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class CSRMetaBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSRMetaBuilder.class);

    CSRMeta build(CsrModel csrModel, Cmpv2Server server) {
        CSRMeta csrMeta = createCsrMeta.apply(csrModel, server);
        addSans.apply(csrModel, csrMeta);
        csrMeta.keyPair(new KeyPair(csrModel.getPublicKey(), csrModel.getPrivateKey()));
        csrMeta.password(server.getAuthentication().getIak());
        csrMeta.setIssuerName(server.getIssuerDN());
        csrMeta.caUrl(server.getUrl());
        csrMeta.setName(csrModel.getSubjectData());
        return csrMeta;
    }

    private BiFunction<CsrModel, Cmpv2Server, CSRMeta> createCsrMeta = (csrModel, server) -> new CSRMeta(
            (Arrays.stream(csrModel.getSubjectData().getRDNs()).map(this.convertFromBcRDN).filter(Objects::isNull)
                     .collect(Collectors.toList())));

    private BiFunction<CsrModel, CSRMeta, CSRMeta> addSans = (csrModel, csrMeta) -> {
        csrModel.getSans().forEach(csrMeta::san);
        return  csrMeta;
    };

    private Function<org.bouncycastle.asn1.x500.RDN, String> convertRDNToString =
            rdn -> rdn.getFirst().getType().getId() + "=" + IETFUtils.valueToString(rdn.getFirst().getValue());

    private Function<org.bouncycastle.asn1.x500.RDN, RDN> convertFromBcRDN = rdn -> {
        RDN result = null;
        try {
            result = new RDN(convertRDNToString.apply(rdn));
        } catch (CertException e) {
            LOGGER.error("Exception occurred during convert of RDN", e);
        }
        return result;
    };


}
