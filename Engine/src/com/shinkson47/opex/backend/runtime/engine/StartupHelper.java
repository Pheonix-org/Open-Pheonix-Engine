package com.shinkson47.opex.backend.runtime.engine;

import com.shinkson47.opex.backend.errormanagement.EMSHelper;
import com.shinkson47.opex.backend.runtime.console.OPEXConsole;
import com.shinkson47.OPEX.backend.runtime.console.instructions.*;
import com.shinkson47.opex.backend.runtime.hooking.HookKey;
import com.shinkson47.opex.backend.runtime.hooking.OPEXHookUpdater;
import com.shinkson47.opex.backend.runtime.threading.OPEXThreadManager;
import com.shinkson47.opex.frontend.window.prefab.Splash;
import com.shinkson47.opex.backend.runtime.console.instructions.*;

/**
 * This class is intended for use only by OPEX. A container for organised and
 * fragmentation of OPEX's startup routine.
 *
 * Subroutines are written in source following the order of expected correct
 * execution.
 *
 * @author gordie
 */
final class StartupHelper {

	/**
	 * Pre engine startup
	 */
	protected static void preStart(){
		OPEXThreadManager.createThread(new Splash(), "OPEXStartSplash");											//Open splash screen in background.
	}

	/**
	 * Post engine startup
	 */
	protected static void postStart(){
		try {
			OPEXThreadManager.createThread(OPEX.getGameSuper(), "OPEXPayloadThread"); // Startup game.
			OPEXConsole.internalLog("Payload thread started.");
		} catch (Exception e) {
			EMSHelper.warn("An error occurred within the game's startup routine, OPEX will now shutdown.");
			EMSHelper.handleException(e);
			OPEX.getEngineSuper().stop();
		}
	}

	protected static void runStartupSubroutines(){
		startRunnables();
		addConsoleInstructions();
		attachThreadlessHooks();
	}


	/**
	 * Adds OPEX's default internal console instructions to the console.
	 */
	private static void addConsoleInstructions() {
		OPEXConsole.addInstruction(new INSTClear());
		OPEXConsole.addInstruction(new INSTHelp());
		OPEXConsole.addInstruction(new INSTList());
		OPEXConsole.addInstruction(new INSTThread());
		OPEXConsole.addInstruction(new INSTEngine());
	}

	/**
	 * Uses the thread manager to execute OPEX's internal runables, causing them to
	 * run thier own startup routines and begin to idle.
	 *
	 * This stage should be called once all resources the executables need have been
	 * loaded, and they've been configured correctly.
	 */
	private static void startRunnables() {
		OPEXThreadManager.createThread(new OPEXHookUpdater(), "OPEXHookUpdater");
		OPEXThreadManager.createThread(new OPEXConsole(), "OPEXConsole");
	}

	private static void attachThreadlessHooks(){
		OPEXHookUpdater.registerUpdateHook(new OPEXThreadManager(new HookKey()), "OPEXThreadManager");
	}

}
