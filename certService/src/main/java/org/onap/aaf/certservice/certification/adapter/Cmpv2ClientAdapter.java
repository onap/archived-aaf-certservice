package org.onap.aaf.certservice.certification.adapter;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.onap.aaf.certservice.certification.configuration.model.Cmpv2Server;
import org.onap.aaf.certservice.certification.exception.KeyDecryptionException;
import org.onap.aaf.certservice.certification.model.CertificationModel;
import org.onap.aaf.certservice.certification.model.CsrModel;
import org.onap.aaf.certservice.cmpv2client.api.CmpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.security.cert.X509Certificate;
import java.util.List;

@Component
public class Cmpv2ClientAdapter {


    private final CmpClient cmpClient;

    @Autowired
    public Cmpv2ClientAdapter(CmpClient cmpClient) {
        this.cmpClient = cmpClient;
    }

    /**
     *
     */
    public CertificationModel calCmpv2Client(CsrModel csrModel, Cmpv2Server server, String caName) {
        // mapping from csrModel, server, caName to:
        //        String caName = caName,
        //        String profile,
        //        CSRMeta csrMeta,
        //        X509Certificate cert,
        //        Date notBefore,
        //        Date notAfter
        List<List<X509Certificate>> certificates = cmpClient.createCertificate(caName, );


        // mapping from X509Certificate to String required (pemObjectToString method)
        // or change in CertificationModel from String to X509Certificate
        return new CertificationModel(certificates.get(0), certificates.get(1));
    }


    private String pemObjectToString(PemObject pemObject) throws KeyDecryptionException {
        try (StringWriter output = new StringWriter()) {
            PemWriter pemWriter = new PemWriter(output);
            pemWriter.writeObject(pemObject);
            pemWriter.close();
            return output.getBuffer().toString();

        } catch (IOException e) {
            throw new KeyDecryptionException("Writing PAM Object to string failed", e);
        }
    }

}
