package org.onap.aaf.certservice.client.certification.conversion;

import org.onap.aaf.certservice.client.certification.writer.CertFileWriter;

public class ConvertedArtifactsCreatorFactory {

    private ConvertedArtifactsCreatorFactory() { }

    public static ConvertedArtifactsCreator createPKCS12Converter(String destPath, String conversionName) {
        final String conversionTarget = "PKCS12";
        return new ConvertedArtifactsCreator(
                new CertFileWriter(destPath),
                new RandomPasswordGenerator(),
                new PemConverter(conversionTarget),
                getExtension(conversionName));
    }

    public static ConvertedArtifactsCreator createJKSConverter(String destPath, String conversionName) {
        return new ConvertedArtifactsCreator(
                new CertFileWriter(destPath),
                new RandomPasswordGenerator(),
                new PemConverter(conversionName),
                getExtension(conversionName));
    }

    private static String getExtension(String conversionName) {
        return "." + conversionName.toLowerCase();
    }

}
