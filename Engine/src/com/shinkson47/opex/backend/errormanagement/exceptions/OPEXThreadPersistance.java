package com.shinkson47.opex.backend.errormanagement.exceptions;

import com.shinkson47.opex.backend.runtime.threading.OPEXThread;

public class OPEXThreadPersistance extends Exception {
	private static final long serialVersionUID = 3496245044240557409L;

	public OPEXThreadPersistance(OPEXThread target) {
		super("The thread '" + target.getThread().getName() + "' cannot be disposed of because it failed to close.");
	}

	public OPEXThreadPersistance(OPEXThread target, String reason) {
		super("The thread '" + target.getThread().getName() + "' cannot be closed because " + reason);
	}

}
