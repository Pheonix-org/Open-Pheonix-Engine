package com.shinkson47.opex.backend.runtime.errormanagement.exceptions;

import com.shinkson47.opex.backend.runtime.threading.OPEXThread;

public class OPEXThreadPersistance extends Exception {
    public OPEXThreadPersistance(OPEXThread target) {
            super("The thread '" + target.getThread().getName() + "' cannot be disposed of because it failed to close.");
    }

    public OPEXThreadPersistance(OPEXThread target, String reason) {
            super("The thread '" + target.getThread().getName() + "' cannot be closed because " + reason);
    }
}