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

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

import org.onap.aaf.certservice.client.certification.exception.PemToPKCS12ConverterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PKCS12FilesCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(PKCS12FilesCreator.class);
    private final String outputPath;


    PKCS12FilesCreator(String path) {
        outputPath = path;
    }

    void saveDataToLocation(byte[] data, String filename) throws PemToPKCS12ConverterException {
        LOGGER.debug("Attempt to save file {} in path {}", filename, outputPath);
        try (FileOutputStream fos = new FileOutputStream(outputPath + filename)) {
            fos.write(data);
        } catch (IOException e) {
            LOGGER.error("File creation failed, exception message: {}", e.getMessage());
            throw new PemToPKCS12ConverterException(e);
        }
    }
}
