package com.shinkson47.OPEX.backend.errormanagement.exceptions;

/**
 * OPEX is a static library. This exception is for when non static access
 * attempts are made, such as a class instantiation attempt.
 * 
 * @author gordie
 *
 */
public class OPEXStaticException extends Exception {
	private static final long serialVersionUID = 1L;

	public OPEXStaticException(String ErrorMessage) {
		super(ErrorMessage + ". OPEX can only be used statically.");
	}

	public OPEXStaticException() {
		super("A non static access attempt to OPEX was made. OPEX can only be used statically.");
	}
}