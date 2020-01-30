package BackEnd.ErrorManagement.Exceptions;

import BackEnd.Runtime.Threading.JGELThread;

public class JGELThreadPersistance extends Exception {

	public JGELThreadPersistance(JGELThread target) {
		super("The thread '" + target.getThread().getName() + "' cannot be disposed of because it failed to close.");
	}
	
	public JGELThreadPersistance(JGELThread target, String reason) {
		super("The thread '" + target.getThread().getName() + "' cannot be closed because " + reason);
	}

}
