package com.shinkson47.opex.backend.runtime.errormanagement.exceptions;

public class OPEXThreadAlive extends Exception  {
        public OPEXThreadAlive(Class<?> c) {
                super("The thread running " + c.getSimpleName()
                        + " cannot be disposed of because it's still alive.") ;
        }
}