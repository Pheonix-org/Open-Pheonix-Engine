package backend.errormanagement.exceptions;

import backend.runtime.threading.JGELThread;

public class JGELThreadPersistance extends Exception {
	private static final long serialVersionUID = 3496245044240557409L;

	public JGELThreadPersistance(JGELThread target) {
		super("The thread '" + target.getThread().getName() + "' cannot be disposed of because it failed to close.");
	}

	public JGELThreadPersistance(JGELThread target, String reason) {
		super("The thread '" + target.getThread().getName() + "' cannot be closed because " + reason);
	}

}
