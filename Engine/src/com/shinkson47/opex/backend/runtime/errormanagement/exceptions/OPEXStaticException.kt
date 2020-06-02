package com.shinkson47.opex.backend.runtime.errormanagement.exceptions

/**
 * OPEX is a static library. This exception is for when non static access
 * attempts are made, such as a class instantiation attempt.
 *
 * @author gordie
 */
class OPEXStaticException : Exception {
    constructor(ErrorMessage: String) : super("$ErrorMessage. OPEX can only be used statically.") {}
    constructor() : super("A non static access attempt to OPEX was made. OPEX can only be used statically.") {}

    companion object {
        private const val serialVersionUID = 1L
    }
}