package backend.runtime.engine;
import backend.runtime.engine.StartupRountine;
import backend.errormanagement.EMSHelper;
import backend.runtime.console.JGELConsole;
import backend.runtime.threading.JGELGame;
import backend.runtime.threading.JGELRunnable;
import backend.runtime.threading.JGELThreadManager;
/**
 * Main executable entry point for starting JGEL's internal systems.
 * 
 * @author gordie
 */
public class JGEL implements JGELRunnable {
	private boolean isRunning = false;
	private JGELGame GameSuper = null;
	
	/**
	 * Provides a method to start this runnable inside of JGEL's runtime environment.
	 * Intended start point of jgel.
	 */
	public JGEL(JGELGame game) {
		if (GameSuper != null) { //JGEL can only run one game class at a time.
			EMSHelper.warn("Attempted to start JGEL, but a game was already assigned. Ignoring startup call.");
			return;	//ignore
		}
		
		if (isRunning) {
			EMSHelper.warn("Attempted to start JGEL, but it's already running. Ignoring startup call.");
			return;
		}
		
		GameSuper = game;
		JGELConsole.internalLog("Valid startup call for JGEL backend framework was issued, starting up. Won't be a jiffy!");
		JGELThreadManager.createThread(this, "JGELRuntimeEnvironment"); //Startup JGEL using the startup routine in this runnable.
	}

	/**
	 * Internal startup routine for JGEL. Starts self managing threads, and calls smaller startup subroutines.
	 * 
	 * JGEL internal runnable has started inside the JGELRuntime thread. This run routine calls subroutines to invoke the framework to start. 
	 */
	@Override
	public void run() {
		JGELConsole.internalLog("Runtime thread started.");
		StartupRountine.addConsoleInstructions();
		StartupRountine.startRunnables();
		isRunning = true; //JGEL has invoked startup.
		
		/*
		 * at this point, background threads may still be starting, or processing startup routines. 
		 */
		
		JGELConsole.internalLog("Internal Framework startup routine has finished invoking subroutines. Subroutines may still be processing. Executing game payload.");
		try {
			JGELThreadManager.createThread(GameSuper, "JGELPayloadThread");	    //Startup game.
			JGELConsole.internalLog("Payload thread started.");
		} catch (Exception e) {
			EMSHelper.warn("An error occoured within the game's startup routine, JGEL will now shutdown.");
			EMSHelper.handleException(e);
			stop();
		}
		JGELConsole.internalLog("JGEL startup routine has completed. Take it away, game startup routine!");
	}

	@Override
	public void stop() {
		isRunning = false;
	}

	
}
