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

import static org.onap.aaf.certservice.client.certification.EncryptionAlgorithmConstants.*;


public class CsrProcedure {

    private final static String SANS_DELIMITER = ":";

    private PublicKey publicKey;
    private PrivateKey privateKey;


    CsrProcedure(KeyPair keyPair) {
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
    }

    public String createCsrInPem(CsrConfiguration config) throws IOException {
        ContentSigner contentSigner;

        X500Principal subject = new X500Principal(String.format("%s=%s, %s=%s, %s=%s, %s=%s, %s=%s, %s=%s",
                COUNTRY, config.getCountry(),
                STATE, config.getState(),
                LOCATION, config.getLocation(),
                ORGANIZATION,config.getOrganization(),
                ORGANIZATION_UNIT, config.getOrganizationUnit(),
                COMMON_NAME, config.getCommonName()));

        try {
            contentSigner = new JcaContentSignerBuilder(SIGN_ALGORITHM).build(privateKey);
        } catch (OperatorCreationException e) {
            throw new CsrConfigurationException("Cannot generate CSR");
        }

        return convertPKC10CsrToPem(
                new JcaPKCS10CertificationRequestBuilder(subject, publicKey)
                .addAttribute(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest, handleSans(config.getSans()).generate())
                .build(contentSigner));
    }


    private static String convertPKC10CsrToPem(PKCS10CertificationRequest request) throws IOException {
        final StringWriter stringWriter = new StringWriter();
        final JcaPEMWriter pemWriter = new JcaPEMWriter(stringWriter);
        pemWriter.writeObject(request);
        pemWriter.flush();
        pemWriter.close();
        return stringWriter.toString();
    }

    private static GeneralNames addSans(String sans) {
        String[] sansTable = sans.split(SANS_DELIMITER);
        int length = sansTable.length;
        GeneralName[] generalNames = new GeneralName[length];
        for(int i=0 ; i < length; i ++ ){
           generalNames[i] = new GeneralName(GeneralName.dNSName, sansTable[i]);
        }
        return new GeneralNames(generalNames);
    }

    private static ExtensionsGenerator handleSans(String sans) throws IOException {
        ExtensionsGenerator generator = new ExtensionsGenerator();
        generator.addExtension(Extension.subjectAlternativeName, false, addSans(sans));
        return generator;
    }
}
