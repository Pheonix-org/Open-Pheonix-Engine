package com.shinkson47.OPEX.backend.errormanagement.exceptions;

public class OPEXStartFailure extends Exception {
    public OPEXStartFailure(Exception exception){
        super("A startup call was rejected due to the following exception; ", exception);
    }
}
