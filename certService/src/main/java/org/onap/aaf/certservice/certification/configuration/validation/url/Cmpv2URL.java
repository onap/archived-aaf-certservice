package org.onap.aaf.certservice.certification.configuration.validation.url;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target( { FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = URLValidator.class)
public @interface Cmpv2URL {
    String message() default "Server URL is invalid.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
