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

import java.security.PrivateKey;
import java.util.List;
import org.onap.aaf.certservice.client.certification.exception.PemToPKCS12ConverterException;

public class KeystoreTruststoreCreator {

    private final RandomPasswordGenerator generator;
    private final PemToPKCS12Converter converter;
    private final PKCS12FilesCreator creator;

    public KeystoreTruststoreCreator(PKCS12FilesCreator creator, RandomPasswordGenerator generator,
        PemToPKCS12Converter converter) {
        this.generator = generator;
        this.converter = converter;
        this.creator = creator;
    }

    public void createKeystore(List<String> data, int passwordLength, String alias, PrivateKey privateKey)
        throws PemToPKCS12ConverterException {
        String password = generator.generate(passwordLength);
        creator.saveKeystoreData(converter.convertKeystore(data, password, alias, privateKey), password);
    }

    public void createTruststore(List<String> data, int passwordLength, String alias)
        throws PemToPKCS12ConverterException {
        String password = generator.generate(passwordLength);
        creator.saveTruststoreData(converter.convertTruststore(data, password, alias), password);
    }
}
