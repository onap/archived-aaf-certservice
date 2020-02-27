package org.onap.aaf.certservice.certification;

import org.onap.aaf.certservice.certification.adapter.Cmpv2ClientAdapter;
import org.onap.aaf.certservice.certification.configuration.model.Cmpv2Server;
import org.onap.aaf.certservice.certification.model.CertificationModel;
import org.onap.aaf.certservice.certification.model.CsrModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CertificationProvider {

    private final Cmpv2ClientAdapter cmpv2ClientAdapter;

    @Autowired
    public  CertificationProvider(Cmpv2ClientAdapter cmpv2ClientAdapter) {
        this.cmpv2ClientAdapter = cmpv2ClientAdapter;
    }

    CertificationModel signCsr(CsrModel csrModel, Cmpv2Server server, String caName) {

        return cmpv2ClientAdapter.calCmpv2Client(csrModel, server, caName);
    }

}
