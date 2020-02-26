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

import com.google.gson.Gson;
import org.onap.aaf.certservice.certification.exception.Cmpv2ServerNotFoundException;
import org.onap.aaf.certservice.certification.exception.CsrDecryptionException;
import org.onap.aaf.certservice.certification.exception.ErrorResponseModel;
import org.onap.aaf.certservice.certification.exception.KeyDecryptionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CertificationExceptionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CertificationExceptionController.class);

    @ExceptionHandler(value = CsrDecryptionException.class)
    public ResponseEntity<String> handle(CsrDecryptionException exception) {
        LOGGER.error("Exception occurred during decoding certificate sign request:", exception);
        return getErrorResponseEntity("Wrong certificate signing request (CSR) format", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = KeyDecryptionException.class)
    public ResponseEntity<String> handle(KeyDecryptionException exception) {
        LOGGER.error("Exception occurred during decoding key:", exception);
        return getErrorResponseEntity("Wrong key (PK) format", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Cmpv2ServerNotFoundException.class)
    public ResponseEntity<String> handle(Cmpv2ServerNotFoundException exception) {
        LOGGER.error("Exception occurred selecting CMPv2 server:", exception);
        return getErrorResponseEntity("Certification authority not found for given CAName", HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<String> getErrorResponseEntity(String errorMessage, HttpStatus status) {
        ErrorResponseModel errorResponse = new ErrorResponseModel(errorMessage);
        return new ResponseEntity<>(
                new Gson().toJson(errorResponse),
                status
        );
    }

}
