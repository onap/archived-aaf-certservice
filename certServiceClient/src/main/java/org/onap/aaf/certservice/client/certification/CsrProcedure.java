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

import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.ExtensionsGenerator;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.onap.aaf.certservice.client.configuration.exception.CsrConfigurationException;
import org.onap.aaf.certservice.client.configuration.model.CsrConfiguration;

import javax.security.auth.x500.X500Principal;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.Base64;
import java.util.Optional;

import static org.onap.aaf.certservice.client.certification.EncryptionAlgorithmConstants.*;


public class CsrProcedure {

    private final static String SANS_DELIMITER = ":";

    private final CsrConfiguration configuration;


    CsrProcedure(CsrConfiguration configuration) {
        this.configuration = configuration;
    }

    public String createCsrInPem() {
        KeyPair keyPair = new KeyPairFactory(RSA_ENCRYPTION_ALGORITHM, KEY_SIZE).create();
        JcaPKCS10CertificationRequestBuilder builder;
        ContentSigner contentSigner;

        String csrParameters  = setOptionalParameters(new StringBuilder(setMandatoryParameters()));
        X500Principal subject = new X500Principal(csrParameters);

        try {
            contentSigner = new JcaContentSignerBuilder(SIGN_ALGORITHM).build(keyPair.getPrivate());
            builder = new JcaPKCS10CertificationRequestBuilder(subject, keyPair.getPublic());
            Optional.ofNullable(configuration.getSans())
                    .filter(CsrProcedure::isOptionalParameterExists)
                    .map(s -> builder
                            .addAttribute(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest, handleSans().generate()));
        } catch (OperatorCreationException e) {
            throw new CsrConfigurationException("Cannot generate CSR");
        }

        return decodeString(convertPKC10CsrToPem(builder.build(contentSigner)));
    }


    private String setMandatoryParameters() {
        return String.format("%s=%s, %s=%s, %s=%s, %s=%s",
                COMMON_NAME, configuration.getCommonName(),
                COUNTRY, configuration.getCountry(),
                STATE, configuration.getState(),
                ORGANIZATION,configuration.getOrganization());
    }

    private String setOptionalParameters(StringBuilder csrParameters) {
        Optional.ofNullable(configuration.getOrganizationUnit())
                .filter(CsrProcedure::isOptionalParameterExists)
                .map(ou -> csrParameters.append(", " + ORGANIZATION_UNIT + "=").append(ou));
        Optional.ofNullable(configuration.getLocation())
                .filter(CsrProcedure::isOptionalParameterExists)
                .map(l -> csrParameters.append(", " + LOCATION + "=").append(l));
        return csrParameters.toString();
    }

    private static String convertPKC10CsrToPem(PKCS10CertificationRequest request) {
        final StringWriter stringWriter = new StringWriter();
        try (JcaPEMWriter pemWriter = new JcaPEMWriter(stringWriter)) {
            pemWriter.writeObject(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringWriter.toString();
    }

    private ExtensionsGenerator handleSans() {
        ExtensionsGenerator generator = new ExtensionsGenerator();
        try {
            generator.addExtension(Extension.subjectAlternativeName, false, createGeneralNames());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return generator;
    }

    private GeneralNames createGeneralNames() {
        String[] sansTable = this.configuration.getSans().split(SANS_DELIMITER);
        int length = sansTable.length;
        GeneralName[] generalNames = new GeneralName[length];
        for(int i=0 ; i < length; i ++ ){
           generalNames[i] = new GeneralName(GeneralName.dNSName, sansTable[i]);
        }
        return new GeneralNames(generalNames);
    }

    private static Boolean isOptionalParameterExists(String parameter) {
       return parameter != null && !"".equals(parameter);
    }

    private static String decodeString(String csrInPem) {
        return Base64.getEncoder().encodeToString(csrInPem.getBytes(StandardCharsets.UTF_8));
    }
}
