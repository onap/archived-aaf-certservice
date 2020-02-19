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


package org.onap.aaf.certservice.certification.configuration.validation.url;

import org.onap.aaf.certservice.certification.configuration.validation.url.violations.URLServerViolation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class URLValidator implements ConstraintValidator<Cmpv2URL, String> {

   private final List<URLServerViolation> violations;

   @Autowired
   public URLValidator(List<URLServerViolation> violations) {
      this.violations = violations;
   }

   @Override
   public void initialize(Cmpv2URL constraint) { }

   @Override
   public boolean isValid(String url, ConstraintValidatorContext context) {
      AtomicBoolean isValid = new AtomicBoolean(true);
      violations.forEach(violation -> {
         if (!violation.validate(url)) {
            isValid.set(false);
         }
      });
      return isValid.get();
   }
}
