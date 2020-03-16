package BackEnd.ErrorManagement.Exceptions;

public class JGELGenericException extends Exception {
	private static final long serialVersionUID = 1L;

	public JGELGenericException() {
		super("An unspecified generic error was indicated.");
	}
	
	public JGELGenericException(String s) {
		super("A generic error occoured: " + s);
	}
}
