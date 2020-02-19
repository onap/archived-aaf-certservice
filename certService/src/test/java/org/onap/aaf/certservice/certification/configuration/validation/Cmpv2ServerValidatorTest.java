package org.onap.aaf.certservice.certification.configuration.validation;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.onap.aaf.certservice.CertServiceApplication;
import org.onap.aaf.certservice.certification.configuration.model.Authentication;
import org.onap.aaf.certservice.certification.configuration.model.CaMode;
import org.onap.aaf.certservice.certification.configuration.model.Cmpv2Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CertServiceApplication.class)
class Cmpv2ServerValidatorTest {

    @Autowired
    private Cmpv2ServerValidator validator;

    private Authentication authentication;
    private Cmpv2Server server;

    @BeforeEach
    private void init() {
        authentication = new Authentication();
        authentication.setRv("asdsa");
        authentication.setIak("IAK");
        server = new Cmpv2Server();
        server.setCaMode(CaMode.CLIENT);
        server.setCaName("asdsdasd");
        server.setIssuerDN("fsdasdasd");
        server.setUrl("http://test.test.test:60000/");
        server.setAuthentication(authentication);
    }

    @Test
    public void givenValidServerDetailsWhenValidatingShouldNotThrowAnyException() {
        //then
        assertDoesNotThrow(() -> validator.validate(server));
    }

    @Test
    public void givenWrongProtocolInURLServerDetailsWhenValidatingShouldThrowException() {
        //given
        server.setUrl("https://test.test.test:60000/");
        //then
        assertThrows(IllegalArgumentException.class, () -> {validator.validate(server);});
    }

    @Test
    public void givenWrongPortInURLServerDetailsWhenValidatingShouldThrowException() {
        //given
        server.setUrl("http://test.test.test:70000/");
        //then
        assertThrows(IllegalArgumentException.class, () -> validator.validate(server));
    }

    @Test
    public void givenCANameLengthInURLServerDetailsWhenValidatingShouldThrowException() {
        //given
        server.setCaName("");
        //then
        assertThrows(IllegalArgumentException.class, () -> validator.validate(server));
    }

    @Test
    public void givenWrongIssuerDNLengthInURLServerDetailsWhenValidatingShouldThrowException() {
        //given
        server.setIssuerDN("123");
        //then
        assertThrows(IllegalArgumentException.class, () -> validator.validate(server));
    }

    @Test
    public void givenWrongRVLengthInURLServerDetailsWhenValidatingShouldThrowException() {
        //given
        authentication.setRv("");
        //then
        assertThrows(IllegalArgumentException.class, () -> validator.validate(server));
    }

    @Test
    public void givenWrongIAKLengthInURLServerDetailsWhenValidatingShouldThrowException() {
        //given
        authentication.setIak("");
        //then
        assertThrows(IllegalArgumentException.class, () -> validator.validate(server));
    }
}