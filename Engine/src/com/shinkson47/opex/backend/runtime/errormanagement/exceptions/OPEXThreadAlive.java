package com.shinkson47.opex.backend.runtime.errormanagement.exceptions;

public class OPEXThreadAlive extends Exception {

        public OPEXThreadAlive(Runnable target) {
                super("The thread running " + target.getClass() + " cannot be disposed of because it's still alive."); }
}