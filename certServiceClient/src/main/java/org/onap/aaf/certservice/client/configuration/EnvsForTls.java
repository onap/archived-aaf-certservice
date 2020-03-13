package org.onap.aaf.certservice.client.configuration;

import java.util.Optional;

public class EnvsForTls {
    private final EnvProvider envProvider = new EnvProvider();

    public Optional<String> getKeystorePath() {
        return readEnv(TlsConfigurationEnvs.KEYSTORE_PATH);
    }

    public Optional<String> getKeystorePassword() {
        return readEnv(TlsConfigurationEnvs.KEYSTORE_PASSWORD);
    }

    public Optional<String> getTruststorePath() {
        return readEnv(TlsConfigurationEnvs.TRUSTSTORE_PATH);
    }

    public Optional<String> getTruststorePassword() {
        return readEnv(TlsConfigurationEnvs.TRUSTSTORE_PASSWORD);
    }

    private Optional<String> readEnv(TlsConfigurationEnvs envName) {
        return envProvider.readEnvVariable(envName.toString());
    }
}
