package com.shinkson47.opex.backend.runtime.threading;

public interface IOPEXRunnable extends Runnable {
    /**
     * OPEX API request for the thread to finish and close itself.
     */
    public void stop();
}