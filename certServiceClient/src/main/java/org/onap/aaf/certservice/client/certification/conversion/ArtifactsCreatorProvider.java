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

import org.onap.aaf.certservice.client.certification.PrivateKeyToPemEncoder;
import org.onap.aaf.certservice.client.certification.writer.CertFileWriter;

public enum ArtifactsCreatorProvider {
    P12("PKCS12") {
        @Override
        ArtifactsCreator create(String destPath) {
            return ConvertedArtifactsCreatorFactory.createConverter(destPath, getExtension(this.toString()), getKeyStoreType());
        }
    },
    JKS("JKS") {
        @Override
        ArtifactsCreator create(String destPath) {
            return ConvertedArtifactsCreatorFactory.createConverter(destPath, getExtension(this.toString()), getKeyStoreType());
        }
    },
    PEM("PEM"){
        @Override
        ArtifactsCreator create(String destPath) {
            return new PemArtifactsCreator(
                    new CertFileWriter(destPath),
                    new PrivateKeyToPemEncoder());
        }
    };
    private final String name;
    ArtifactsCreatorProvider(String name) {
        this.name = name;
    }

    public static ArtifactsCreator getCreator(String outputType, String destPath) {
        return valueOf(outputType).create(destPath);
    }

    abstract ArtifactsCreator create(String destPath);

    String getKeyStoreType() {
        return name;
    }

    static String getExtension(String conversionName) {
        return String.format(".%s", conversionName.toLowerCase());
    }

}
