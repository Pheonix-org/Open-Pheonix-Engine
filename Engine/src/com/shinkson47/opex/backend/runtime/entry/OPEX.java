package com.shinkson47.opex.backend.runtime.entry;

import com.shinkson47.opex.backend.runtime.errormanagement.EMSHelper;
import com.shinkson47.opex.backend.runtime.errormanagement.exceptions.OPEXStartFailure;
import com.shinkson47.opex.backend.runtime.IOPEXVersionable;
import com.shinkson47.opex.backend.runtime.environment.OPEXEnvironmentUtils;
import com.shinkson47.opex.backend.runtime.console.Console;
import com.shinkson47.opex.backend.runtime.environment.ShutdownCauses;
import com.shinkson47.opex.backend.runtime.threading.OPEXGame;
import com.shinkson47.opex.backend.runtime.threading.IOPEXRunnable;
import com.shinkson47.opex.backend.runtime.threading.OPEXThreadManager;
import com.shinkson47.opex.backend.toolbox.Version;
import com.shinkson47.opex.frontend.window.prefab.Splash;

/**
 * Main executable entry point for starting OPEX.
 * Is instantiable, but is static. Creating an instance
 * of this class is the intended entry point;
 * new OPEX(OPEXGame game);
 *
 * @author gordie
 */
public class OPEX implements IOPEXRunnable, IOPEXVersionable {

	/**
	 * @interface IOPEXVersionable
	 * @Return the build of the engine in the format
	 * year.month.day.dailybuild,
	 * i.e 2020.21.31.C
	 *
	 * where dailybuild represents the quantity of builds that day,
	 * A = first build of the day,
	 * B = second build of the day,
	 * etc.
	 * @return
	 */
	@Override
	public Version VERSION(){
		return new Version(2020,5,12,"A");
	}

	/**
	 * Contains a static global reference to the engine's main class.
	 * Once started, this will not change.
	 */
	private static OPEX EngineSuper = null;

	/**
	 * @return static reference to the engine's main class.
	 */
	public static OPEX getEngineSuper(){
		return EngineSuper;
	}



	/**
	 * Creates a static reference to the game client's main class.
	 */
	private static OPEXGame GameSuper = null;

	/**
	 * @return a static reference to the registered game main class.
	 */
	public static OPEXGame getGameSuper() {
		return GameSuper;
	}

	/**
	 * Displays weather or not the engine is started, running and ready for use.
	 */
	private static boolean isRunning = false;

	/**
	 * Is the engine currently starting. Used to prevent concurrent startup requests.
	 */
	private static boolean isStarting = false;

	/**
	 * Used by instantiator to hang whilst engine is starting.
	 */
	private static boolean waitForStartup = false;

	/**
	 * Invoking this method will cause the engine to hang when instantiated, only returning from
	 * 'new OPEX(OPEXGame)' when the engine is indicated to have started.
	 *
	 * Default false, instantiation calls for engine startup and returns.
	 */
	public static void waitForStartup() {
		setWaitForStartup(true);
	}

	/**
	 * Sets the value of waitForStartup.
	 *
	 * @see this.waitForStartup.
	 * @param value
	 */
	public static void setWaitForStartup(boolean value) {
		waitForStartup = value;
	}

	public static boolean isWaitingForStartup(){
		if (isRunning()) setWaitForStartup(false);
		return waitForStartup;
	}

	/**
	 * @return true if BOTH game and engine super class static references are not set.
	 */
	private static boolean areSupersNull(){
		return (getGameSuper() == null && getEngineSuper() == null);
	}

	private static void assignSupers(OPEXGame gameSuper, OPEX engineSuper) throws IllegalAccessException{
		/*
		 	Validate supers have not been set before
		 */
		if (!areSupersNull()) {
			throw new IllegalAccessException("Attempted to assign super classes to OPEX, but the super's are already set.");
		}

		/*
			Supers are assignable, assign them.
		 */
		GameSuper = gameSuper;
		EngineSuper = engineSuper;
	}

	/**
	 * @return state of the engine;
	 * true - Engine has started, and is ready for use.
	 * false - Engine has not completely started in a valid manor.
	 */
	public static Boolean isRunning() {
		return isRunning;
	}

	public static Boolean isRunnable() { return (areSupersNull() && !isRunning() && !isStarting);}



	/**
	 * Intended start point of OPEX. Provides a method to start this runnable inside of OPEX's runtime
	 * environment.
	 */
	public OPEX(OPEXGame game) throws OPEXStartFailure {
		try {
			/*
				Engine startup call.
				Validate engine is ready to start.
			 */
			if (!isRunnable()) {																						//Test if engine has been started, or is running.
				EMSHelper.handleException(new IllegalAccessException("Attempted to start OPEX, but the engine has been started before."));
				return;																									//Reject startup call/
			}

			/*
				Startup call accepted, assign supers and begin engine startup.
			 */
			isStarting = true;
			Console.internalLog("Valid startup call for OPEX backend framework was issued, starting up. Won't be a jiffy!");//Log valid startup call
			assignSupers(game, this);																		//Assign supers
			StartupHelper.preStart();																					//Invoke pre start
			OPEXThreadManager.createThread(this, "OPEXRuntimeEnvironment"); 								//Startup OPEX using this runnable
		} catch (Exception e){
			isStarting = false;
			throw new OPEXStartFailure(e);
		}

		while (isWaitingForStartup()){}																					//If requested, hang here until the engine has started.
		isStarting = false;
	}

	/**
	 * Internal startup routine for OPEX. Starts self managing threads, and calls
	 * smaller startup subroutines.
	 *
	 * OPEX internal runnable has started inside the OPEXRuntime thread. This run
	 * routine calls subroutines to invoke the framework to start.
	 */
	@Override
	public void run() {

		/*
		 	A valid startup call has invoked the engine to start, start.
		 */
		Console.internalLog("Runtime thread started.");																//Log successful launch of OPEX startup thread.
		StartupHelper.runStartupSubroutines();																			//Start Engine's systems using the StartupHelper

		/*
		 * at this point, background threads may still be starting, or processing
		 * startup routines; but the engine is starting up.
		 */
		Console.internalLog("Internal Framework startup routine has finished invoking subroutines. Subroutines may still be processing. ");


		//TODO monitor each subroutine for success. Wait for all subroutines to report success before marking the engine as running, and invoking post start.

		/*
		 * Engine startup has completed.
		 */
		isRunning = true;																								//Mark engine as started.
		Console.internalLog("OPEX startup routine has completed, waiting for splash screen to close.");				//Log completion, and wait
		while (Splash.isSplashVisible()){
			try {Thread.sleep(100);
			} catch (InterruptedException e) {}}
		waitForStartup = false;
		Console.internalLog("Done, executing post start and game payload.");										//Log switch to game runnable.
		StartupHelper.postStart();																						//invoke post start
	}

	/**
	 * @Interface IOPEXRunnable.
	 *
	 * Invoking the engine's main thread to close triggers a shutdown.
	 */
	@Override
	public void stop() {
		OPEXEnvironmentUtils.shutdown(ShutdownCauses.ENGINE_SHUTDOWN_REQUEST);
	}
}
