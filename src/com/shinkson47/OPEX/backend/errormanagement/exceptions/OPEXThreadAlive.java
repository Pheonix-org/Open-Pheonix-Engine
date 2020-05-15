package com.shinkson47.OPEX.backend.errormanagement.exceptions;

public class OPEXThreadAlive extends Exception {
	private static final long serialVersionUID = -2653373212777127871L;

	public OPEXThreadAlive(Runnable target) {
		super("The thread running " + target.getClass().getSimpleName()
				+ " cannot be disposed of because it's still alive.");
	}

}
