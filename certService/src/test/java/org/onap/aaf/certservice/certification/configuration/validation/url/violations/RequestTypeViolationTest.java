package org.onap.aaf.certservice.certification.configuration.validation.url.violations;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestTypeViolationTest {

    private RequestTypeViolation violation = new RequestTypeViolation();

    @Test
    public void givenValidRequestTypeShouldReturnTrue() {
        //given
        String validURL = "http://127.0.0.1/ejbca/publicweb/cmp/cmp";

        //when
        boolean result = violation.validate(validURL);

        //then
        assertTrue(result);
    }

    @Test
    public void givenInvalidRequestTypeShouldReturnFalse() {
        //given
        String invalidURL = "htestps://127.0.0.1/ejbca/publicweb/cmp/cmp";

        //when
        boolean result = violation.validate(invalidURL);

        //then
        assertFalse(result);
    }
}