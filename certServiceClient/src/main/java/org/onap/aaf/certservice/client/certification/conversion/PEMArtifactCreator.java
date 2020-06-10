package org.onap.aaf.certservice.client.certification.conversion;

import java.security.PrivateKey;
import java.util.List;
import org.onap.aaf.certservice.client.api.ExitableException;
import org.onap.aaf.certservice.client.certification.PrivateKeyToPemEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PEMArtifactCreator implements ArtifactCreator {
    private static final Logger LOGGER = LoggerFactory.getLogger(PEMArtifactCreator.class);

    private static final String KEY_PEM = "key.pem";
    private static final String KEYSTORE_PEM = "keystore.pem";
    private static final String TRUSTSTORE_PEM = "truststore.pem";

    private final FilesCreator creator;
    private final PrivateKeyToPemEncoder pkEncoder;

    public PEMArtifactCreator(FilesCreator creator, PrivateKeyToPemEncoder pkEncoder) {
        this.creator = creator;
        this.pkEncoder = pkEncoder;
    }

    @Override
    public void generateArtifacts(List<String> keystoreData, List<String> truststoreData, PrivateKey privateKey)
        throws ExitableException {
        LOGGER.debug("Attempt to create PEM private key file and saving data. File name: {}", KEY_PEM);
        creator.saveDataToLocation(pkEncoder.encodePrivateKeyToPem(privateKey).getBytes(), KEY_PEM);

        LOGGER.debug("Attempt to create PEM keystore file and saving data. File name: {}", KEYSTORE_PEM);
        creator.saveDataToLocation(String.join("\n", keystoreData).getBytes(), KEYSTORE_PEM);

        LOGGER.debug("Attempt to create PEM truststore file and saving data. File name: {}", TRUSTSTORE_PEM);
        creator.saveDataToLocation(String.join("\n", truststoreData).getBytes(), TRUSTSTORE_PEM);
    }
}
