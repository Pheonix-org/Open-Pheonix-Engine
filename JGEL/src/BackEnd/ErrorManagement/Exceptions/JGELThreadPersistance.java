package BackEnd.ErrorManagement.Exceptions;

public class JGELThreadPersistance extends Exception {

	public JGELThreadPersistance(Runnable target) {
		super("The thread running " + target.getClass().getSimpleName() + " cannot be disposed of because it failed to close.");
	}

}
