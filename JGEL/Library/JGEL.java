import BackEnd.ErrorManagement.JGELEMS;
import BackEnd.Runtime.Console.JGELConsole;
import BackEnd.Runtime.Threading.JGELGame;
import BackEnd.Runtime.Threading.JGELRunnable;
import BackEnd.Runtime.Threading.JGELThreadManager;
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
			JGELEMS.Warn("Attempted to start JGEL, but a game was already assigned. Ignoring startup call.");
			return;	//ignore
		}
		
		if (isRunning == true) {
			JGELEMS.Warn("Attempted to start JGEL, but it's already running. Ignoring startup call.");
			return;
		}
		
		GameSuper = game;
		JGELConsole.InternalLog("Valid startup call for JGEL backend framework was issued, starting up. Won't be a jiffy!");
		JGELThreadManager.CreateThread(this, "JGELRuntimeEnvironment"); //Startup JGEL using the startup routine in this runnable.
	}

	/**
	 * Internal startup routine for JGEL. Starts self managing threads, and calls smaller startup subroutines.
	 * 
	 * JGEL internal runnable has started inside the JGELRuntime thread. This run routine calls subroutines to invoke the framework to start. 
	 */
	@Override
	public void run() {
		JGELConsole.InternalLog("Runtime thread started.");
		JGELStartupRountine.AddConsoleInstructions();
		JGELStartupRountine.StartRunnables();
		isRunning = true; //JGEL has invoked startup.
		
		/*
		 * at this point, background threads may still be starting, or processing startup routines. 
		 */
		
		JGELConsole.InternalLog("Internal Framework startup routine has finished invoking subroutines. Subroutines may still be processing. Executing game payload.");
		try {
			JGELThreadManager.CreateThread(GameSuper, "JGELPayloadThread");	    //Startup game.
			JGELConsole.InternalLog("Payload thread started.");
		} catch (Exception e) {
			JGELEMS.Warn("An error occoured within the game's startup routine, JGEL will now shutdown.");
			JGELEMS.HandleException(e);
			stop();
		}
		JGELConsole.InternalLog("JGEL startup routine has completed. Take it away, game startup routine!");
	}

	@Override
	public void stop() {
		isRunning = false;
	}

	
}
