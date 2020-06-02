package com.shinkson47.opex.backend.runtime.errormanagement.exceptions

class OPEXThreadAlive(target: Runnable) : Exception("The thread running " + target.javaClass.simpleName
        + " cannot be disposed of because it's still alive.") {
    companion object {
        private const val serialVersionUID = -2653373212777127871L
    }
}