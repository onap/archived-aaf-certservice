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

import java.io.IOException;
import java.io.StringReader;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import org.onap.aaf.certservice.certification.exceptions.CsrDecryptionException;


public class PemObjectFactory {

    public PemObject createPmObject(String pem) throws CsrDecryptionException {

        try (StringReader stringReader = new StringReader(pem);
             PemReader pemReader = new PemReader(stringReader)) {
            return pemReader.readPemObject();
        } catch (IOException e) {
            throw new CsrDecryptionException("Unable to create PEM", e);
        }
    }

}
