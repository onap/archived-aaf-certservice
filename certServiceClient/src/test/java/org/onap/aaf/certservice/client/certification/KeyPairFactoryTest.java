package org.onap.aaf.certservice.client.certification;


import io.vavr.control.Either;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;
import org.onap.aaf.certservice.client.exceptions.KeyPairGenerationException;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

class KeyPairFactoryTest {

    @Test
    public void shouldProvideKeyPairGenerator_whenCreateKeyPairGeneratorCalled() {
        //  given
        KeyPairFactory keyPairFactory = new KeyPairFactory(EncryptionAlgorithmConstants.RSA_ENCRYPTION_ALGORITHM,
                EncryptionAlgorithmConstants.KEY_SIZE);
        //  when
        Either<KeyPairGenerationException, KeyPair> keyPair = Try.of(() -> keyPairFactory.createKeyPairGenerator())
    }

}