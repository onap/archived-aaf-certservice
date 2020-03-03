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

public class Base64PkEncoder {

    private static final byte[] BEGIN_PK = "-----BEGIN PRIVATE KEY-----\n".getBytes();
    private static final byte[] END_PK = "-----END PRIVATE KEY-----\n".getBytes();
    private static final byte[] NEW_LINE = "\n".getBytes();
    private static final int PEM_LINE_LENGTH = 64;

    public static String encodePrivateKey(PrivateKey pk) throws PkEncodingException {
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()){
            baos.write(BEGIN_PK);
            byte[] base64PkBytes = Base64.encode(pk.getEncoded());
            for(int i = 0; i<base64PkBytes.length; i=i+PEM_LINE_LENGTH) {
                baos.write(base64PkBytes, i, Math.min(PEM_LINE_LENGTH, base64PkBytes.length-i));
                baos.write(NEW_LINE);
            }
            baos.write(END_PK);
            return new String(Base64.encode(baos.toByteArray()));
        } catch (IOException e) {
            throw new PkEncodingException(e);
        }
    }
}
