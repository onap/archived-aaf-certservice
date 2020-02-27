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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.CertException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.openssl.jcajce.JcaMiscPEMGenerator;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.util.io.pem.PemObjectGenerator;
import org.bouncycastle.util.io.pem.PemWriter;
import org.onap.aaf.certservice.certification.configuration.model.Cmpv2Server;
import org.onap.aaf.certservice.certification.exception.Cmpv2ClientAdapterException;
import org.onap.aaf.certservice.certification.model.CertificationModel;
import org.onap.aaf.certservice.certification.model.CsrModel;
import org.onap.aaf.certservice.cmpv2client.api.CmpClient;
import org.onap.aaf.certservice.cmpv2client.exceptions.CmpClientException;
import org.onap.aaf.certservice.cmpv2client.external.CSRMeta;
import org.onap.aaf.certservice.cmpv2client.external.RDN;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Cmpv2ClientAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Cmpv2ClientAdapter.class);
    private static final int VALID_PERIOD_IN_DAYS = 365;

    private final CmpClient cmpClient;

    @Autowired
    public Cmpv2ClientAdapter(CmpClient cmpClient) {
        this.cmpClient = cmpClient;
    }

    public CertificationModel callCmpClient(CsrModel csrModel, Cmpv2Server server)
            throws CmpClientException, Cmpv2ClientAdapterException {
        List<List<X509Certificate>> certificates = cmpClient.createCertificate(server.getCaName(),
                server.getCaMode().getProfile(), createCsrMeta.apply(csrModel, server),
                convertToX509Certificate(csrModel.getCsr(), csrModel.getPrivateKey()));
        return new CertificationModel(convertFromX509CertificateList.apply(certificates.get(0)),
                convertFromX509CertificateList.apply(certificates.get(1)));
    }

    private String convertFromX509Certificate(X509Certificate certificate) {
        StringWriter sw = new StringWriter();
        try (PemWriter pw = new PemWriter(sw)) {
            PemObjectGenerator gen = new JcaMiscPEMGenerator(certificate);
            pw.writeObject(gen);
        } catch (IOException e) {
            LOGGER.error("Exception occurred during convert of X509 certificate", e);
        }
        return sw.toString();
    }

    private X509Certificate convertToX509Certificate(PKCS10CertificationRequest csr, PrivateKey privateKey)
            throws Cmpv2ClientAdapterException {
        try {
            X509v3CertificateBuilder certificateGenerator =
                    new X509v3CertificateBuilder(csr.getSubject(), createSerial.get(),
                            Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)),
                            Date.from(LocalDateTime.now().plusDays(VALID_PERIOD_IN_DAYS).toInstant(ZoneOffset.UTC)),
                            new PKCS10CertificationRequest(csr.getEncoded()).getSubject(), SubjectPublicKeyInfo
                                                                                                   .getInstance(
                                                                                                           ASN1Sequence
                                                                                                                   .getInstance(
                                                                                                                           csr.getSubjectPublicKeyInfo()
                                                                                                                                   .getEncoded())));
            ContentSigner signer =
                    new BcRSAContentSignerBuilder(csr.getSignatureAlgorithm(), csr.getSignatureAlgorithm())
                            .build(PrivateKeyFactory.createKey(privateKey.getEncoded()));
            X509CertificateHolder holder = certificateGenerator.build(signer);
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509", "BC");
            return (X509Certificate) certificateFactory.generateCertificate(
                    new ByteArrayInputStream(holder.toASN1Structure().getEncoded()));
        } catch (IOException | CertificateException | NoSuchProviderException | OperatorCreationException e) {
            throw new Cmpv2ClientAdapterException(e);
        }

    }

    private Supplier<BigInteger> createSerial = () -> {
        byte[] serial = new byte[16];
        new SecureRandom().nextBytes(serial);
        return new BigInteger(serial).abs();
    };

    private BiFunction<CsrModel, Cmpv2Server, CSRMeta> createCsrMeta = (csrModel, server) -> new CSRMeta(
            Arrays.stream(csrModel.getSubjectData().getRDNs()).map(this.convertFromBcRDN).filter(Objects::isNull)
                    .collect(Collectors.toList()));

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

    private Function<List<X509Certificate>, List<String>> convertFromX509CertificateList =
            certificates -> certificates.stream().map(this::convertFromX509Certificate).filter(String::isEmpty)
                                    .collect(Collectors.toList());

}
