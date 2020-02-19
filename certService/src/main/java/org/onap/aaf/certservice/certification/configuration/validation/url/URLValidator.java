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

   private List<URLServerViolation> violations;

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
