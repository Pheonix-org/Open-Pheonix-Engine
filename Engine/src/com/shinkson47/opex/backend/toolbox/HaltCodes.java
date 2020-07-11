package com.shinkson47.opex.backend.toolbox;

public enum HaltCodes {
    GENERIC_OR_UNKNOWN,                                                                                                 // Overlap with default jre value, a missing value would match this standard of generic or unknown.
    ENGINE_SHUTDOWN_REQUEST,                                                                                            // Shutdown request parsed to the engine
    ENGINE_EXCEPTION,                                                                                                   // Fatal or uncaught exception within the engine
    CLIENT_EXCEPTION,                                                                                                   // Fatal or uncaught exception the game client
    EMS_CASCADE,                                                                                                        // EMS triggered EIS due to an error cascade.
    USER_SHUTDOWN_REQUEST,                                                                                              // Direct shutdown call from a client.
    ENGINE_FATAL_EXCEPTION                                                                                              // Unrecoverable or fatal engine exception
}
