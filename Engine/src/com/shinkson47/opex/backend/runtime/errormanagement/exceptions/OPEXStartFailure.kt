package com.shinkson47.opex.backend.runtime.errormanagement.exceptions

class OPEXStartFailure(exception: Exception?) : Exception("A startup call was rejected due to the following exception; ", exception) 