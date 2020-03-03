/*
 * ============LICENSE_START=======================================================
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



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.PrivateKey;
import org.bouncycastle.util.encoders.Base64;
import org.onap.aaf.certservice.client.certification.exception.PkEncodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Base64PkEncoder {

    private static final byte[] BEGIN_PK = "-----BEGIN PRIVATE KEY-----\n".getBytes();
    private static final byte[] END_PK = "-----END PRIVATE KEY-----\n".getBytes();
    private static final byte[] NEW_LINE = "\n".getBytes();
    private static final int PEM_LINE_LENGTH = 64;

    private final Logger LOGGER = LoggerFactory.getLogger(Base64PkEncoder.class);

    public String encodePrivateKey(PrivateKey pk) throws PkEncodingException {
        LOGGER.info("Encoding PrivateKey to Base64 encoded PEM");
        try(ByteArrayOutputStream pemStream = new ByteArrayOutputStream()){
            pemStream.write(BEGIN_PK);
            byte[] base64PkBytes = Base64.encode(pk.getEncoded());
            int base64PkBytesLength = base64PkBytes.length;
            for(int i = 0; i < base64PkBytesLength; i = i + PEM_LINE_LENGTH) {
                pemStream.write(base64PkBytes, i, Math.min(PEM_LINE_LENGTH, base64PkBytesLength - i));
                pemStream.write(NEW_LINE);
            }
            pemStream.write(END_PK);
            return new String(Base64.encode(pemStream.toByteArray()));
        } catch (IOException e) {
            LOGGER.error("Exception occurred during encoding PrivateKey", e);
            throw new PkEncodingException(e);
        }
    }
}
