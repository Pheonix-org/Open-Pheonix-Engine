package BackEnd.ErrorManagement.Exceptions;

public class JGELThreadAlive extends Exception {
	
	public JGELThreadAlive(Runnable target) {
		super("The thread running " + target.getClass().getSimpleName() + " cannot be disposed of because it's still alive.");
	}

}
