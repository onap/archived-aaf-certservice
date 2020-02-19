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

package org.onap.aaf.certservice.certification.configuration.validation;

import org.hibernate.validator.HibernateValidator;
import org.onap.aaf.certservice.certification.configuration.model.Cmpv2Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.security.InvalidParameterException;
import java.util.Set;

@Service
public class Cmpv2ServerValidator {

    private Validator validator;

    @Autowired
    public Cmpv2ServerValidator(final AutowireCapableBeanFactory autowireCapableBeanFactory) {
        ValidatorFactory validatorFactory = Validation.byProvider( HibernateValidator.class )
                .configure().constraintValidatorFactory(new SpringConstraintValidatorFactory(autowireCapableBeanFactory))
                .buildValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    public void validate(Cmpv2Server serverDetails) {
        Set<ConstraintViolation<Cmpv2Server>> violations = validator.validate(serverDetails);
        if (!violations.isEmpty()) {
             throw new InvalidParameterException(violations.toString());
        }
    }

}
