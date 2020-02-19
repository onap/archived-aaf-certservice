package org.onap.aaf.certservice.certification.configuration.validation.url.violations;

import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;

@Service
public class PortNumberViolation implements URLServerViolation {

    @Override
    public boolean validate(String serverUrl) {
        try {
            URL url = new URL(serverUrl);
            int port = url.getPort();
            return port >= 1 && port <= 65535 || port == -1;
        } catch (MalformedURLException e) {
            return false;
        }
    }

}
