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
import org.onap.aaf.certservice.client.exceptions.CsrConfigurationException;
import org.onap.aaf.certservice.client.model.CsrConfiguration;

import javax.security.auth.x500.X500Principal;
import java.io.IOException;
import java.io.StringWriter;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Optional;

import static org.onap.aaf.certservice.client.certification.EncryptionAlgorithmConstants.*;


public class CsrProcedure {

    private final static String SANS_DELIMITER = ":";

    private final PublicKey publicKey;
    private final PrivateKey privateKey;


    CsrProcedure(KeyPair keyPair) {
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
    }

    public String createCsrInPem(CsrConfiguration config) {
        ContentSigner contentSigner;
        String organizationUnit = config.getOrganizationUnit();
        String location = config.getLocation();
        String sans = config.getSans();

        String mandatoryParams = String.format("%s=%s, %s=%s, %s=%s, %s=%s",
                COMMON_NAME, config.getCommonName(),
                COUNTRY, config.getCountry(),
                STATE, config.getState(),
                ORGANIZATION,config.getOrganization());

        StringBuilder csrParameters  = new StringBuilder(mandatoryParams);

        Optional.ofNullable(organizationUnit)
                .filter(CsrProcedure::isOptionalParameterExists)
                .map(ou -> csrParameters.append(", " + ORGANIZATION_UNIT + "=").append(ou));

        Optional.ofNullable(location)
                .filter(CsrProcedure::isOptionalParameterExists)
                .map(l -> csrParameters.append(", " + LOCATION + "=").append(l));

        X500Principal subject = new X500Principal(csrParameters.toString());

        try {
            contentSigner = new JcaContentSignerBuilder(SIGN_ALGORITHM).build(privateKey);
        } catch (OperatorCreationException e) {
            throw new CsrConfigurationException("Cannot generate CSR");
        }

        JcaPKCS10CertificationRequestBuilder builder = new JcaPKCS10CertificationRequestBuilder(subject, publicKey);
        Optional.ofNullable(sans)
                .filter(CsrProcedure::isOptionalParameterExists)
                .map(s -> builder
                        .addAttribute(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest, handleSans(s).generate()));
        return convertPKC10CsrToPem(builder.build(contentSigner));
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

    private static ExtensionsGenerator handleSans(String sans) {
        ExtensionsGenerator generator = new ExtensionsGenerator();
        try {
            generator.addExtension(Extension.subjectAlternativeName, false, createGeneralNames(sans));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return generator;
    }

    private static GeneralNames createGeneralNames(String sans) {
        String[] sansTable = sans.split(SANS_DELIMITER);
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
}
