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

package org.onap.aaf.certservice.certification;

import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.util.io.pem.PemObject;
import org.onap.aaf.certservice.certification.exceptions.CSRDecryptionException;
import org.onap.aaf.certservice.certification.model.CSRModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;


@Service
public class CSRFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSRFactory.class);
    private final PemObjectFactory pemObjectFactory = new PemObjectFactory();

    public CSRModel createCSR(StringBase64 csr, StringBase64 privateKey) throws CSRDecryptionException {
        LOGGER.debug("Decoded received CSR: \n{}", csr.asString());

        try {
            PemObject pemObject = pemObjectFactory.createPmObject(csr.asString());
            PKCS10CertificationRequest decryptedCSR = new PKCS10CertificationRequest(
                    pemObject.getContent()
            );
            PemObject decryptedKey = pemObjectFactory.createPmObject(privateKey.asString());
            return new CSRModel(decryptedCSR, decryptedKey);
        } catch (IOException e) {
            throw new CSRDecryptionException("Incorrect CSR, decryption failed", e);
        }
    }

    public static class StringBase64 {
        private final String value;
        private final Base64.Decoder DECODER = Base64.getDecoder();

        public StringBase64(String value) {
            this.value = value;
        }

        public String asString() {
            return new String(DECODER.decode(value));
        }
    }
}


