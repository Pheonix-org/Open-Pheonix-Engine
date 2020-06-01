package com.shinkson47.opex.backend.errormanagement.exceptions;

public class OPEXStartFailure extends Exception {
    public OPEXStartFailure(Exception exception){
        super("A startup call was rejected due to the following exception; ", exception);
    }
}
