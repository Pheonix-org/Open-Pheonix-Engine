package com.shinkson47.opex.backend.runtime.errormanagement.exceptions

class OPEXNotImplementedException : Exception {
    constructor() : super("This feature is not yet implemented.") {}
    constructor(s: String) : super("This feature is not yet implemented. $s") {}
}