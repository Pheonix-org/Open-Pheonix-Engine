package com.shinkson47.OPEX.backend.runtime.threading;

public interface IOPEXRunnable extends Runnable {
	/**
	 * OPEX API request for the thread to finish and close itself.
	 */
	public abstract void stop();

}
