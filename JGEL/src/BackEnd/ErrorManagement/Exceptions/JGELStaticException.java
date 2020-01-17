package BackEnd.ErrorManagement.Exceptions;

/**
 * Jgel is a static library. This exception is for when non static access attempts are made,
 * such as a class instantiation attempt.
 * @author gordie
 *
 */
public class JGELStaticException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public JGELStaticException(String ErrorMessage) {
		super(ErrorMessage + " JGEL can only be used statically.");
	}
	
	public JGELStaticException() {
		super("A non static access attempt to JGEL was made. JGEL can only be used statically.");
	}
}
