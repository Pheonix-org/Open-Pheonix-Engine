package com.shinkson47.opex.backend.runtime.threading

import com.shinkson47.opex.backend.runtime.IOPEXVersionable

/**
 * Super interface for user's class. Indicates class as a valid game main for
 * OPEX.
 *
 * @author gordie
 */
interface OPEXGame : IOPEXRunnable, IOPEXVersionable 