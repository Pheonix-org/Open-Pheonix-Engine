package backend.runtime.engine;

import backend.errormanagement.EMSHelper;
import backend.errormanagement.exceptions.JGELThreadPersistance;
import backend.runtime.console.JGELConsole;
import backend.runtime.console.instructions.*;
import backend.runtime.hooking.JGELHookUpdater;
import backend.runtime.threading.JGELThreadManager;

import java.awt.*;

/**
 * This class is intended for use only by JGEl. A container for organised and
 * fragmentation of JGEL's startup routine.
 *
 * Subroutines are written in source following the order of expected correct
 * execution.
 *
 * @author gordie
 */
final class JGELStartupHelper {

	/**
	 * Pre engine startup
	 */
	protected static void preStart(){
		JGELThreadManager.createThread(new Splash(), "JGELStartSplash");											//Open splash screen in background.
	}

	/**
	 * Post engine startup
	 */
	protected static void postStart(){
		try {
			JGELThreadManager.createThread(JGEL.getGameSuper(), "JGELPayloadThread"); // Startup game.
			JGELConsole.internalLog("Payload thread started.");
		} catch (Exception e) {
			EMSHelper.warn("An error occurred within the game's startup routine, JGEL will now shutdown.");
			EMSHelper.handleException(e);
			JGEL.getEngineSuper().stop();
		}
	}

	protected static void runStartupSubroutines(){
		JGELStartupHelper.startRunnables();
		JGELStartupHelper.addConsoleInstructions();
	}


	/**
	 * Adds jgel's default internal console instructions to the console.
	 */
	private static void addConsoleInstructions() {
		JGELConsole.addInstruction(new INSTClear());
		JGELConsole.addInstruction(new INSTHelp());
		JGELConsole.addInstruction(new INSTList());
		JGELConsole.addInstruction(new INSTThread());
		JGELConsole.addInstruction(new INSTEngine());
	}

	/**
	 * Uses the thread manager to execute JGEL's internal runables, causing them to
	 * run thier own startup routines and begin to idle.
	 *
	 * This stage should be called once all resources the executables need have been
	 * loaded, and they've been configured correctly.
	 */
	private static void startRunnables() {
		JGELThreadManager.createThread(new JGELHookUpdater(), "JGELHookUpdater");
		JGELThreadManager.createThread(new JGELConsole(), "JGELConsole");
	}

}
