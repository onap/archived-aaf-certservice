package org.onap.aaf.certservice.client.configuration.factory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.onap.aaf.certservice.client.configuration.EnvsForTls;
import org.onap.aaf.certservice.client.configuration.exception.ClientConfigurationException;

import javax.net.ssl.SSLContext;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class SSLContextFactoryTest {

    public static final String INVALID_KEYSTORE_PATH = "nonexistent/keystore";
    public static final String VALID_KEYSTORE_NAME = "keystore.jks";
    public static final String VALID_KEYSTORE_PASSWORD = "secret";
    public static final String INVALID_KEYSTORE_PASSWORD = "wrong_secret";
    public static final String INVALID_TRUSTSTORE_PATH = "nonexistent/truststore";
    public static final String VALID_TRUSTSTORE_PASSWORD = "secret";
    public static final String INVALID_TRUSTSTORE_PASSWORD = "wrong_secret";
    public static final String VALID_TRUSTSTORE_NAME = "truststore.jks";
    @Mock
    private EnvsForTls envsForTls;

    @Test
    public void shouldThrowExceptionWhenKeystorePathEnvIsMissing() {
        // Given
        when(envsForTls.getKeystorePath()).thenReturn(Optional.empty());
        SSLContextFactory sslContextFactory = new SSLContextFactory(envsForTls);

        // When, Then
        Exception exception = assertThrows(
                ClientConfigurationException.class, sslContextFactory::create
        );
        assertThat(exception.getMessage()).contains("KEYSTORE_PATH");
    }

    @Test
    public void shouldThrowExceptionWhenKeystorePasswordEnvIsMissing() {
        // Given
        when(envsForTls.getKeystorePath()).thenReturn(Optional.of("keystore"));
        when(envsForTls.getKeystorePassword()).thenReturn(Optional.empty());
        SSLContextFactory sslContextFactory = new SSLContextFactory(envsForTls);

        // When, Then
        Exception exception = assertThrows(
                ClientConfigurationException.class, sslContextFactory::create
        );
        assertThat(exception.getMessage()).contains("KEYSTORE_PASSWORD");
    }

    @Test
    public void shouldThrowExceptionWhenTruststorePathEnvIsMissing() {
        // Given
        when(envsForTls.getKeystorePath()).thenReturn(Optional.of("keystore"));
        when(envsForTls.getKeystorePassword()).thenReturn(Optional.of("password"));
        when(envsForTls.getTruststorePath()).thenReturn(Optional.empty());
        SSLContextFactory sslContextFactory = new SSLContextFactory(envsForTls);

        // When, Then
        Exception exception = assertThrows(
                ClientConfigurationException.class, sslContextFactory::create
        );
        assertThat(exception.getMessage()).contains("TRUSTSTORE_PATH");
    }

    @Test
    public void shouldThrowExceptionWhenTruststorePasswordEnvIsMissing() {
        // Given
        when(envsForTls.getKeystorePath()).thenReturn(Optional.of("keystore"));
        when(envsForTls.getKeystorePassword()).thenReturn(Optional.of("password"));
        when(envsForTls.getTruststorePath()).thenReturn(Optional.of("truststore"));
        when(envsForTls.getTruststorePassword()).thenReturn(Optional.empty());
        SSLContextFactory sslContextFactory = new SSLContextFactory(envsForTls);

        // When, Then
        Exception exception = assertThrows(
                ClientConfigurationException.class, sslContextFactory::create
        );
        assertThat(exception.getMessage()).contains("TRUSTSTORE_PASSWORD");
    }

    @Test
    public void shouldThrowExceptionWhenKeystoreIsMissing() {
        // Given
        when(envsForTls.getKeystorePath()).thenReturn(Optional.of(INVALID_KEYSTORE_PATH));
        when(envsForTls.getKeystorePassword()).thenReturn(Optional.of("secret"));
        when(envsForTls.getTruststorePath()).thenReturn(Optional.of("truststore.jks"));
        when(envsForTls.getTruststorePassword()).thenReturn(Optional.of("secret"));
        SSLContextFactory sslContextFactory = new SSLContextFactory(envsForTls);

        // When, Then
        assertThrows(
                ClientConfigurationException.class, sslContextFactory::create
        );
    }

    @Test
    public void shouldThrowExceptionWhenKeystorePasswordIsWrong() {
        // Given
        String keystorePath = getResourcePath(VALID_KEYSTORE_NAME);
        when(envsForTls.getKeystorePath()).thenReturn(Optional.of(keystorePath));
        when(envsForTls.getKeystorePassword()).thenReturn(Optional.of(INVALID_KEYSTORE_PASSWORD));
        when(envsForTls.getTruststorePath()).thenReturn(Optional.of(VALID_TRUSTSTORE_NAME));
        when(envsForTls.getTruststorePassword()).thenReturn(Optional.of(VALID_TRUSTSTORE_PASSWORD));
        SSLContextFactory sslContextFactory = new SSLContextFactory(envsForTls);

        // When, Then
        assertThrows(
                ClientConfigurationException.class, sslContextFactory::create
        );
    }

    @Test
    public void shouldThrowExceptionWhenTruststoreIsMissing() {
        // Given
        String keystorePath = getResourcePath(VALID_KEYSTORE_NAME);
        when(envsForTls.getKeystorePath()).thenReturn(Optional.of(keystorePath));
        when(envsForTls.getKeystorePassword()).thenReturn(Optional.of(VALID_KEYSTORE_PASSWORD));
        when(envsForTls.getTruststorePath()).thenReturn(Optional.of(INVALID_TRUSTSTORE_PATH));
        when(envsForTls.getTruststorePassword()).thenReturn(Optional.of(VALID_TRUSTSTORE_PASSWORD));
        SSLContextFactory sslContextFactory = new SSLContextFactory(envsForTls);

        // When, Then
        assertThrows(
                ClientConfigurationException.class, sslContextFactory::create
        );
    }

    @Test
    public void shouldThrowExceptionWhenTruststorePasswordIsWrong() {
        // Given
        String keystorePath = getResourcePath(VALID_KEYSTORE_NAME);
        String truststorePath = getResourcePath(VALID_TRUSTSTORE_NAME);
        when(envsForTls.getKeystorePath()).thenReturn(Optional.of(keystorePath));
        when(envsForTls.getKeystorePassword()).thenReturn(Optional.of(VALID_KEYSTORE_PASSWORD));
        when(envsForTls.getTruststorePath()).thenReturn(Optional.of(truststorePath));
        when(envsForTls.getTruststorePassword()).thenReturn(Optional.of(INVALID_TRUSTSTORE_PASSWORD));
        SSLContextFactory sslContextFactory = new SSLContextFactory(envsForTls);

        // When, Then
        assertThrows(
                ClientConfigurationException.class, sslContextFactory::create
        );
    }

    @Test
    public void shouldReturnSSLContext() throws ClientConfigurationException {
        // Given
        String keystorePath = getResourcePath(VALID_KEYSTORE_NAME);
        String truststorePath = getResourcePath(VALID_TRUSTSTORE_NAME);
        when(envsForTls.getKeystorePath()).thenReturn(Optional.of(keystorePath));
        when(envsForTls.getKeystorePassword()).thenReturn(Optional.of(VALID_KEYSTORE_PASSWORD));
        when(envsForTls.getTruststorePath()).thenReturn(Optional.of(truststorePath));
        when(envsForTls.getTruststorePassword()).thenReturn(Optional.of(VALID_TRUSTSTORE_PASSWORD));
        SSLContextFactory sslContextFactory = new SSLContextFactory(envsForTls);

        // When
        SSLContext sslContext = sslContextFactory.create();

        // Then
        assertNotNull(sslContext);
    }

    private String getResourcePath(String resource) {
        return getClass().getClassLoader().getResource(resource).getFile();
    }
}

