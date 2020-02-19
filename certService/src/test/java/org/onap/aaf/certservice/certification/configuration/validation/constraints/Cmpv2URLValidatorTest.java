package org.onap.aaf.certservice.certification.configuration.validation.constraints;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Cmpv2URLValidatorTest {

    private Cmpv2URLValidator validator = new Cmpv2URLValidator();

    @Test
    public void givenCorrectURLWhenValidatingShouldReturnTrue() {
        //given
        String URL = "http://127.0.0.1/ejbca/publicweb/cmp/cmp";

        //when
        boolean result = validator.isValid(URL, null);

        //then
        assertTrue(result);
    }

    @Test
    public void givenIncorrectURLWhenValidatingShouldReturnFalse() {
        //given
        String URL = "httttp://127.0.0.1:80000/ejbca/publicweb/cmp/cmp";

        //when
        boolean result = validator.isValid(URL, null);

        //then
        assertFalse(result);
    }
}