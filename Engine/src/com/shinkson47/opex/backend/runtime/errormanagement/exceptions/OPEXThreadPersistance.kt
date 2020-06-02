package com.shinkson47.opex.backend.runtime.errormanagement.exceptions

import com.shinkson47.opex.backend.runtime.threading.OPEXThread

class OPEXThreadPersistance : Exception {
    constructor(target: OPEXThread) : super("The thread '" + target.thread.name + "' cannot be disposed of because it failed to close.") {}
    constructor(target: OPEXThread, reason: String) : super("The thread '" + target.thread.name + "' cannot be closed because " + reason) {}

    companion object {
        private const val serialVersionUID = 3496245044240557409L
    }
}