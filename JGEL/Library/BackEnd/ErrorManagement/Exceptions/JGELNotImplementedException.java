package backend.errormanagement.exceptions;

public class JGELNotImplementedException extends Exception {
	
	public JGELNotImplementedException() { 
		super("This feature is not yet implemented.");
	}
	
	public JGELNotImplementedException(String s) { 
		super("This feature is not yet implemented. " + s);
	}

}
