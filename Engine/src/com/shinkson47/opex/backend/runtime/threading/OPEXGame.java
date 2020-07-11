package com.shinkson47.opex.backend.runtime.threading;

import com.shinkson47.opex.backend.toolbox.Versionable;

/**
 * Abstract for user's class. Indicates class as a valid game main for
 * OPEX.
 *
 * @author Jordan Gray
 * @version 2
 * @since 2020.7.11.A
 */
public abstract class OPEXGame extends Versionable implements IOPEXRunnable {}