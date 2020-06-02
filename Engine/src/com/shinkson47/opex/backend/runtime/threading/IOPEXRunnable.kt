package com.shinkson47.opex.backend.runtime.threading

interface IOPEXRunnable : Runnable {
    /**
     * OPEX API request for the thread to finish and close itself.
     */
    fun stop()
}