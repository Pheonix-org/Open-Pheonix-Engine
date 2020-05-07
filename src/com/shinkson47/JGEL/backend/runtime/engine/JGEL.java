package backend.runtime.engine;

import backend.errormanagement.EMSHelper;
import backend.runtime.JGELVersionable;
import backend.runtime.console.JGELConsole;
import backend.runtime.threading.JGELGame;
import backend.runtime.threading.JGELRunnable;
import backend.runtime.threading.JGELThreadManager;

/**
 * Main executable entry point for starting JGEL's internal systems.
 *
 * @author gordie
 */
public class JGEL implements JGELRunnable, JGELVersionable {
	private static boolean isRunning = false;

	@Override
	public String VERSION(){
		return "2020.5.7.B";
	}

	public static JGEL getEngineSuper(){
		return EngineSuper;
	}

	public static JGELGame getGameSuper() {
		return GameSuper;
	}

	private static JGEL EngineSuper = null;
	private static JGELGame GameSuper = null;

	/**
	 * Provides a method to start this runnable inside of JGEL's runtime
	 * environment. Intended start point of jgel.
	 */
	public JGEL(JGELGame game) {
		if (GameSuper != null) { // JGEL can only run one game class at a time.
			EMSHelper.handleException(new IllegalAccessException("Attempted to start JGEL, but a game was already assigned. Ignoring startup call."));
			return; // ignore
		}

		if (EngineSuper != null){ //JGEL can only be ran once.
			EMSHelper.handleException(new IllegalAccessException("Attempted to start JGEL, the engine was already instantiated. Ignoring startup call."));
			return; //ignore
		}

		if (isRunning) {
			EMSHelper.handleException(new IllegalAccessException("Attempted to start JGEL, but it's already running. Ignoring startup call."));
			return;
		}


		//Accept startup call.
		JGELConsole.internalLog("Valid startup call for JGEL backend framework was issued, starting up. Won't be a jiffy!");//Log valid startup
		JGELStartupHelper.preStart();																					//Invoke pre start
		GameSuper = game;																								//Assign game super.
		EngineSuper = this;																								//Assign engine super.
		JGELThreadManager.createThread(this, "JGELRuntimeEnvironment"); 									// Startup JGEL using the startup routine in this runnable
	}

	/**
	 * Internal startup routine for JGEL. Starts self managing threads, and calls
	 * smaller startup subroutines.
	 *
	 * JGEL internal runnable has started inside the JGELRuntime thread. This run
	 * routine calls subroutines to invoke the framework to start.
	 */
	@Override
	public void run() {

		JGELConsole.internalLog("Runtime thread started.");
		JGELStartupHelper.runStartupSubroutines();

		/*
		 * at this point, background threads may still be starting, or processing
		 * startup routines.
		 */

		JGELConsole.internalLog("Internal Framework startup routine has finished invoking subroutines. Subroutines may still be processing. ");

		/**
		 * Engine startup has completed.
		 */
		isRunning = true;																								//Mark engine as started.
		JGELConsole.internalLog("JGEL startup routine has completed, waiting for splash screen to close.");				//Log completion, and wait
		while (Splash.isSplashVisible()){																				//Wait for splash to close
			try {Thread.sleep(100);
			} catch (InterruptedException e) {}}

		JGELStartupHelper.postStart();																					//invoke post start
	}

	@Override
	public void stop() {
		isRunning = false;
	}

	public static Boolean isRunning() {
		return isRunning;
	}


}
