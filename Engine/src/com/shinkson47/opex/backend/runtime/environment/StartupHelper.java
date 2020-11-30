package com.shinkson47.opex.backend.runtime.environment;

import com.shinkson47.opex.backend.runtime.console.instruction.Instruction;
import com.shinkson47.opex.backend.runtime.errormanagement.EMSHelper;
import com.shinkson47.opex.backend.runtime.console.Console;
import com.shinkson47.opex.backend.runtime.errormanagement.exceptions.OPEXDisambiguationException;
import com.shinkson47.opex.backend.runtime.threading.ThreadManager;
import com.shinkson47.opex.backend.toolbox.HaltCodes;
import com.shinkson47.opex.frontend.window.prefabs.Splash;
import org.reflections.Reflections;

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
		try {
			ThreadManager.createThread(new Splash(), "OPEXStartSplash");										//Open splash screen in background.
		} catch (OPEXDisambiguationException e) {}																		//A splash thread already exsist, do nothing. This should not be possible.
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
	protected static void runStartupSubroutines(){
		startRunnables();
		addConsoleInstructions();
	}

	/**
	 * Console instruction subroutine
	 *
	 * Adds OPEX's default internal console instructions to the console.
	 */
	private static void addConsoleInstructions() {
		// TODO support multiple URL's so clients can add thiers. Perhaps even find url from client object.
		Reflections reflections = new Reflections("com.shinkson47");
		Set<Class<? extends Instruction>> classes = reflections.getSubTypesOf(Instruction.class);
		classes.forEach(o -> {
			try {
				o.newInstance();
			} catch (InstantiationException | IllegalAccessException e) { e.printStackTrace(); }
		});
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
	private static void startRunnables() {
		try {
			ThreadManager.createThread(new Console(), "OPEXConsole");											// Start console thread

			//OPEXThreadManager.createThread(OPEX.getHookUpdater(), "OPEXHookUpdaterThread");						// Run OPEX's default hook updater in a new thread.
			OPEX.getHookUpdater().registerUpdateHook(new ThreadManager(), "OPEXThreadManager");				// Add the static thread manager to the default hook updater, so that it can manage threads.
		} catch (OPEXDisambiguationException e) {
			EMSHelper.handleException(e);
			RuntimeHelper.shutdown(HaltCodes.ENGINE_FATAL_EXCEPTION, "Failed to boot internal runnables.");
		} finally {
			// TODO assert runnables have started.
		}
	}
}
