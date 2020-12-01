package com.shinkson47.opex.backend.runtime.errormanagement.exceptions;

/**
 * OPEX is a static library. This exception is for when non static access
 * attempts are made, such as a class instantiation attempt.
 *
 * @author gordie
 */
public class OPEXStaticException extends Exception {
    public OPEXStaticException(String ErrorMessage) {
            super(ErrorMessage + ". This class can only be used statically.");
    }

    public OPEXStaticException() {
            super("A non static access attempt to a static class was issued.");
    }
}