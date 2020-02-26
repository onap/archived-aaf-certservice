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

package org.onap.aaf.certservice.client.certification.conversion;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.List;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.onap.aaf.certservice.client.certification.exception.PemToPKCS12ConverterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PemToPKCS12Converter {
    private final static String PKCS12 = "PKCS12";
    private final Logger LOGGER = LoggerFactory.getLogger(PemToPKCS12Converter.class);
    private final PrivateKey privateKey;

    public PemToPKCS12Converter(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public byte[] convertKeystore(List<String> certificateChain, String password, String alias) {
        LOGGER.debug("Converting PEM certificates to PKCS12 keystore.");
        return convert(certificateChain, password, certs -> getKeyStore(alias, password, certs));
    }

    public byte[] convertTruststore(List<String> certificateChain, String password, String alias) {
        LOGGER.debug("Converting PEM certificates to PKCS12 truststore.");
        return convert(certificateChain, password, certs -> getTrustStore(alias, certs));
    }

    private byte[] convert(List<String> certificateChain, String password, StoreEntryOperation op) {
        final Certificate[] X509Certificates = convertToCertificateArray(certificateChain);
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            KeyStore ks = op.getStore(X509Certificates);
            ks.store(bos, password.toCharArray());
            return bos.toByteArray();
        } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException e) {
            LOGGER.debug("Pem to PKCS12 converter failed, cause: ", e.getMessage());
            throw new PemToPKCS12ConverterException(e);
        }
    }

    private KeyStore getKeyStore(String alias, String password, Certificate[] certificates)
        throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        KeyStore ks = getKeyStoreInstance();
        ks.setKeyEntry(alias, privateKey, password.toCharArray(), certificates);
        return ks;
    }

    private static KeyStore getTrustStore(String alias, Certificate[] certificates)
        throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        KeyStore ks = getKeyStoreInstance();
        int i = 1;
        for (Certificate c : certificates) {
            ks.setCertificateEntry(alias + i++, c);
        }
        return ks;
    }

    private static KeyStore getKeyStoreInstance()
        throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        KeyStore ks = KeyStore.getInstance(PKCS12);
        ks.load(null);
        return ks;
    }

    private Certificate[] convertToCertificateArray(List<String> certificateChain) {
        return certificateChain.stream().map(s -> {
            try (PEMParser pem = new PEMParser(new StringReader(s))) {
                X509CertificateHolder certHolder = (X509CertificateHolder) pem.readObject();
                return new JcaX509CertificateConverter()
                    .setProvider(new BouncyCastleProvider())
                    .getCertificate(certHolder);
            } catch (IOException | CertificateException e) {
                LOGGER.debug("Certificates conversion failed, cause: ", e.getMessage());
                throw new PemToPKCS12ConverterException(e);
            }
        }).toArray(Certificate[]::new);
    }
}
