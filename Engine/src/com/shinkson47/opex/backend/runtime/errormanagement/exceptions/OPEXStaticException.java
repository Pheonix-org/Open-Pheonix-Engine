package com.shinkson47.opex.backend.runtime.errormanagement.exceptions;

/**
 * OPEX is a static library. This exception is for when non static access
 * attempts are made, such as a class instantiation attempt.
 *
 * @author gordie
 */

public class OPEXStaticException extends Exception  {
        public OPEXStaticException(Object sender) {
                super(sender.getClass().getSimpleName() + ": Class refused to instantiate. It may only be used statically.") ;
        }
}