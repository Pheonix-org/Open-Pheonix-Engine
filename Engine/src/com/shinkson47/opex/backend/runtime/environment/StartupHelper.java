package com.shinkson47.opex.backend.runtime.environment;

import com.shinkson47.opex.backend.runtime.errormanagement.EMSHelper;
import com.shinkson47.opex.backend.runtime.console.Console;
import com.shinkson47.opex.backend.runtime.errormanagement.exceptions.OPEXStartFailure;
import com.shinkson47.opex.backend.runtime.hooking.OPEXBootHook;
import com.shinkson47.opex.backend.runtime.invokation.AutoInvoke;
import com.shinkson47.opex.backend.runtime.threading.ThreadManager;
import com.shinkson47.opex.frontend.window.prefabs.Splash;

import java.util.Set;

/**
 * This class is intended for use only by OPEX. A container for organised and
 * fragmentation of OPEX's startup routine.
 *
 * Subroutines are written in source following the order of expected correct
 * execution.
 *
 * @author gordie
 */
public final class StartupHelper {

	/**
	 * Pre engine startup routine
	 */
	protected static void preStart(){
	}

	/**
	 * Post engine startup routine
	 */
	protected static void postStart(){
		try {
			ThreadManager.createThread(OPEX.getGameSuper(), "OPEXPayloadThread"); // Startup game.
			Console.internalLog("Payload thread started.");
		} catch (Exception e) {
			EMSHelper.warn("An error occurred within the game's startup routine, OPEX will now shutdown.");
			EMSHelper.handleException(e);
			OPEX.getEngineSuper().stop();
		}
	}

	/**
	 * Invokes subroutines
	 */
	protected static void runStartupSubroutines() throws OPEXStartFailure {
		startRunnables();
		new Splash().dispatch();																						// Dispatch as async.
	}


	/**
	 * Runnables subroutine
	 *
	 * Uses the thread manager to execute OPEX's internal runables, causing them to
	 * run thier own startup routines and begin to idle.
	 *
	 * This stage should be called once all resources the executables need have been
	 * loaded, and they've been configured correctly.
	 */
	private static void startRunnables() throws OPEXStartFailure {
		Set<Class<? extends OPEXBootHook>> hooks = AutoInvoke.findAllSubclasses("", OPEXBootHook.class);

		for (Class<? extends OPEXBootHook> hook : hooks) {
			try {
				hook.newInstance().dispatch();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new OPEXStartFailure(e);
			}
		}
	}
}
