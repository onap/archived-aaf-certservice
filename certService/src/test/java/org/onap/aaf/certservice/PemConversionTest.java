package org.onap.aaf.certservice;

import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.io.pem.PemObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.onap.aaf.certservice.certification.CsrModelFactory;
import org.onap.aaf.certservice.certification.PemObjectFactory;
import org.onap.aaf.certservice.certification.exception.DecryptionException;
import org.onap.aaf.certservice.certification.model.CsrModel;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import static org.assertj.core.api.Assertions.assertThat;
import static org.onap.aaf.certservice.certification.TestData.TEST_CSR;
import static org.onap.aaf.certservice.certification.TestData.TEST_PK;

public class PemConversionTest {

    private PemObjectFactory pemObjectFactory;
    private CsrModelFactory csrModelFactory;

    @BeforeEach
    void setUp() {
        pemObjectFactory = new PemObjectFactory();
        csrModelFactory = new CsrModelFactory();
    }

    @Test
    void convertingPemPrivateKeyToJavaSecurityPrivateKey() throws DecryptionException, NoSuchAlgorithmException, InvalidKeySpecException {
        PemObject pemPrivateKey = pemObjectFactory.createPemObject(TEST_PK).orElseThrow(
                () -> new DecryptionException("Pem decryption failed")
        );

        KeyFactory factory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pemPrivateKey.getContent());
        PrivateKey privateKey = factory.generatePrivate(keySpec);

        assertThat(privateKey.getEncoded()).contains(pemPrivateKey.getContent());
    }

    @Test
    void convertingPemPublicKeyToJavaSecurityPublicKey() throws DecryptionException, NoSuchAlgorithmException, InvalidKeySpecException {

        CsrModel csrModel = GenerateCsrForTests();
        PemObject pemPublicKey = csrModel.getPublicKey();

        KeyFactory factory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pemPublicKey.getContent());
        PublicKey publicKey = factory.generatePublic(keySpec);

        assertThat(publicKey.getEncoded()).contains(pemPublicKey.getContent());
    }

    private CsrModel GenerateCsrForTests() throws DecryptionException {
        String encoderPK = new String(Base64.encode(TEST_PK.getBytes()));
        String encodedCsr = new String(Base64.encode(TEST_CSR.getBytes()));
        return csrModelFactory.createCsrModel(
                new CsrModelFactory.StringBase64(encodedCsr),
                new CsrModelFactory.StringBase64(encoderPK)
        );
    }

}
