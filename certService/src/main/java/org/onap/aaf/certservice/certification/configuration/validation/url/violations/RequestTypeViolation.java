package org.onap.aaf.certservice.certification.configuration.validation.url.violations;

import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class RequestTypeViolation implements URLServerViolation {

    private final static List<String> validRequests = Arrays.asList("http");

    @Override
    public boolean validate(String serverUrl) {
        try {
            AtomicBoolean isValid = new AtomicBoolean(false);
            String protocol = new URL(serverUrl).getProtocol();
            validRequests.forEach(requestType -> {
                if (protocol.equals(requestType)) {
                    isValid.set(true);
                }
            });
            return isValid.get();
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
