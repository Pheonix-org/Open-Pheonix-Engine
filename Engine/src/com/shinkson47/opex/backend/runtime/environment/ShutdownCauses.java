package com.shinkson47.opex.backend.runtime.environment;

public class ShutdownCauses {
    public static final int GENERIC_OR_UNKNOWN = 0;                                                                     //Overlap with default jre value, a missing value would match this standard of generic or unknown.
    public static final int ENGINE_SHUTDOWN_REQUEST = 1;                                                                //Shutdown request parsed to the engine
    public static final int ENGINE_EXCEPTION = 2;                                                                       //Fatal or uncaught exception within the engine
    public static final int GAME_EXCEPTION = 3;                                                                         //Fatal or uncaught exception the game client
    public static final int EMS_CASCADE = 4;                                                                            //EMS triggered EIS due to an error cascade.
    public static final int USER_SHUTDOWN_REQUEST = 5;                                                                  //Direct shutdown call from a client.
}
