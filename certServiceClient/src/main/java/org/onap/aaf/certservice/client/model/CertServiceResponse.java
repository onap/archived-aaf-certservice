package org.onap.aaf.certservice.client.model;

import java.util.Collections;
import java.util.List;

public class CertServiceResponse {

    private final List<String> certificateChain;
    private final List<String> trustedCertificates;

    public CertServiceResponse(List<String> certificateChain, List<String> trustedCertificates) {
        this.certificateChain = certificateChain;
        this.trustedCertificates = trustedCertificates;
    }

    public List<String> getCertificateChain() {
        return Collections.unmodifiableList(certificateChain);
    }

    public List<String> getTrustedCertificates() {
        return Collections.unmodifiableList(trustedCertificates);
    }

}
