package org.onap.aaf.certservice.certification.configuration.validation.url.violations;

import org.springframework.stereotype.Service;

@Service
public interface URLServerViolation {
    boolean validate(String url);
}
