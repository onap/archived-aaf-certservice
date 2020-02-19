package org.onap.aaf.certservice.client;

import org.onap.aaf.certservice.client.api.ExitableException;

class DummyExitableException extends ExitableException {
    private static final int EXIT_CODE = 888;

    DummyExitableException() {
        super("This is Test Exitable Exception");
    }

    @Override
    public int applicationExitCode() {
        return EXIT_CODE;
    }

}
