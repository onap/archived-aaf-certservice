package org.onap.aaf.certservice.certification.configuration.validation.url.violations;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PortNumberViolationTest {

    private PortNumberViolation violation = new PortNumberViolation();

    @Test
    public void givenValidPortShouldReturnTrue() {
        //given
        String validURL1 = "http://127.0.0.1:8080/ejbca/publicweb/cmp/cmp";
        String validURL2 = "http://127.0.0.1:1/ejbca/publicweb/cmp/cmp";
        String validURL3 = "http://127.0.0.1:65535/ejbca/publicweb/cmp/cmp";

        //when
        boolean result1 = violation.validate(validURL1);
        boolean result2 = violation.validate(validURL2);
        boolean result3 = violation.validate(validURL3);

        //then
        assertTrue(result1);
        assertTrue(result2);
        assertTrue(result3);
    }

    @Test
    public void givenEmptyPortShouldReturnTrue() {
        //given
        String validURL = "http://127.0.0.1/ejbca/publicweb/cmp/cmp";

        //when
        boolean result = violation.validate(validURL);

        //then
        assertTrue(result);
    }

    @Test
    public void givenInvalidPortShouldReturnFalse() {
        //given
        String invalidURL1 = "http://127.0.0.1:0/ejbca/publicweb/cmp/cmp";
        String invalidURL2 = "http://127.0.0.1:65536/ejbca/publicweb/cmp/cmp";

        //when
        boolean result1 = violation.validate(invalidURL1);
        boolean result2 = violation.validate(invalidURL2);

        //then
        assertFalse(result1);
        assertFalse(result2);
    }
}