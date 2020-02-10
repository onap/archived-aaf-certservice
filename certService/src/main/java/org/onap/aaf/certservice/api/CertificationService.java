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

package org.onap.aaf.certservice.api;

import org.onap.aaf.certservice.certification.exceptions.CSRDecryptionException;
import org.onap.aaf.certservice.certification.model.CSRModel;
import org.onap.aaf.certservice.certification.CSRModelFactory;
import org.onap.aaf.certservice.certification.CSRModelFactory.StringBase64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CertificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CertificationService.class);

    private CSRModelFactory csrModelFactory;

    @Autowired
    CertificationService(CSRModelFactory csrModelFactory) {
        this.csrModelFactory = csrModelFactory;
    }

    @GetMapping("/certificate/{caName}")
    /**
     * Request for signing certificate by given CA.
     * <p>
     *
     * @param CAName, the name of Certification Authority that will sign root certificate
     * @param CSR, Certificate Sign Request encoded in Base64 form
     * @param PK, Private key for CSR, encoded in Base64 form
     * @return JSON containing trusted certificates and certificate chain
     */
    public ResponseEntity<String> signCertificate(
            @PathVariable String caName,
            @RequestHeader("CSR") String encodedCSR,
            @RequestHeader("PK") String encodedPrivateKey
    ) {
        LOGGER.info("Received certificate signing request for CA named:: {}", caName);

        try {
            CSRModel csrModel = csrModelFactory.createCSRModel(new StringBase64(encodedCSR), new StringBase64(encodedPrivateKey));
            LOGGER.debug("Received CSR meta data: \n{}", csrModel.toString());
            return new ResponseEntity<>(csrModel.toString(), HttpStatus.OK);
        } catch (CSRDecryptionException e) {
            LOGGER.trace("Exception occur during certificate signing:", e);
            return new ResponseEntity<>("Wrong certificate signing request (CSR) format", HttpStatus.BAD_REQUEST);
        }
    }


}
