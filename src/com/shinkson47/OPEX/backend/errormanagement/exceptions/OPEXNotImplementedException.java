package com.shinkson47.OPEX.backend.errormanagement.exceptions;

public class OPEXNotImplementedException extends Exception {

	public OPEXNotImplementedException() {
		super("This feature is not yet implemented.");
	}

	public OPEXNotImplementedException(String s) {
		super("This feature is not yet implemented. " + s);
	}

}
