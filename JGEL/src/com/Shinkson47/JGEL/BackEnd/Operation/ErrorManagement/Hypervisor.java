package com.Shinkson47.JGEL.BackEnd.Operation.ErrorManagement;

import java.lang.Thread.UncaughtExceptionHandler;

import com.Shinkson47.JGEL.BackEnd.Configuration.Configuration;
import com.Shinkson47.JGEL.BackEnd.General.GeneralTools;
import com.Shinkson47.JGEL.BackEnd.Operation.Diagnostics.Logger;
import com.Shinkson47.JGEL.BackEnd.Operation.Startup.InternalStartScript;
import com.Shinkson47.JGEL.BackEnd.Operation.Startup.JGEStartupScript;
import com.Shinkson47.JGEL.BackEnd.Updating.HookUpdater;
import com.Shinkson47.JGEL.FrontEnd.Window.WindowManager;

/**
 * The hypervisor creates and manages a thread in which the game executes in.
 * 
 * It controls startup, close down, exception handling and [DRM?]
 * 
 * @author gordie
 *
 */
public class Hypervisor {	
	
	/**
	 * Stores wether or not JGEL has been started.
	 * The hypervisor may only start once, and that is the purpose if this bit.
	 */
	private static boolean JGELHasStarted = false;
	
	/**
	 * Stores the startup script of the user's game. The hypervisor will use this to start the client whilst starting the runtime environment.
	 */
	private static JGEStartupScript StartScript = null;
	
	/**
	 * Thread used for the client's excecution.
	 * This should never be null.
	 */
	private static Thread GameThread = new Thread();
	
	/**
	 * Thread that will hold the hook updater. This thread is responsible for udpating all aspects of the runtime outside of external event triggers.
	 * Jgel and client systems and windows windows are all updated via this thread.
	 */
	public static Thread HookUpdaterThread;
	
	/**
	 * JGEL Startup script.
	 */
	private static JGEStartupScript internalStartScript = new InternalStartScript();
	
	/**
	* ExceptionHandler used to catch runtime exceptions thrown within the client.
	*/
	public static UncaughtExceptionHandler ExceptionHandler = new UncaughtExceptionHandler() {
		@Override
		public void uncaughtException(Thread t, Throwable e) {
			ErrorManager.Error(2, new Exception(e));				//Simply parse to error manager.
		}
	};
	
	/**
	 * Create an instance of the hypervisor and assign the user's game at the same time.
	 * @param StartupScript
	 * @deprecated Statification of the Hypervisor makes constructors redundant.
	 */
	@Deprecated
	public Hypervisor(JGEStartupScript StartupScript) {
		StartScript = StartupScript;
	}	
	
	/**
	 * Private method to assign the startup script
	 * @param script
	 */
	private static void Assign(JGEStartupScript script) {
		if (StartScript != null) ErrorManager.Error(6, null);	//Assigned? Already assigned error
		if (script == null) ErrorManager.Error(7, null);		//No script to assign
		
		StartScript = script;									//else, Assign
	}

	/**
	 * Starts JGEL and the client via the param script. The fun starts here.
	 * @param script
	 * @throws Warning 9 - Library already in use. JGEL cannot start twice.
	 * @throws Error 4 	 - Runtime exception within the client's prestart.
	 * @throws Error 7	 - No startup script
	 * @throws Error 8	 - Client thread already running
	 * 
	 */
	@SuppressWarnings("static-access")						 	//hush now.
	public static void Start(JGEStartupScript script) {
	
		Logger.log("======================================");	//Startup log
		Logger.log(" 	Created using JGE ");
		Logger.log("		Library version ");
		Logger.log("		" + Configuration.JGEL_VERSION);	
		Logger.log("======================================");
		Logger.log("JGE is preparing to start.");
		
		Assign(script); 										//Assign the client script
		ReadyToRun();											//Run checks to see if jgel can start.	
		
		internalStartScript.JGEStartup(); 						//Start JGEL's internal systems.

		try {
			GeneralTools.InvokeMethod(StartScript.getClass().getMethod("JGEStartup", null), StartScript);
		} catch (NoSuchMethodException | SecurityException e) {} //invoke client prestart script
		
		Logger.log("Prestart completed without fault, assigning and starting game thread");

		InitaliseClient();										//Start the client
		
		internalStartScript.run(); 								//Invoke internal post-start scripts
	}
	
	/**
	 * Initalises and starts the client thread.
	 */
	@SuppressWarnings("static-access")
	private static void InitaliseClient() {
		GameThread = new Thread(StartScript);					//Create and configure thread.
		GameThread.setName("Client Thread");
		GameThread.setDefaultUncaughtExceptionHandler(ExceptionHandler); //Uncaught exceptions will be handled by JGEL's error manager.
		GameThread.start();										//Start will create the thread in runtime and invoke the client's run() method.

		Logger.log("Client should be active, starting update thread.");
	}

	/**
	 * Checks if the static library is ready to start.
	 * 
	 * Returns naturally if the library is ready to start.
	 * Calls the error manager otherwise.
	 * @throws Error 17: JGEL has already started
	 * @throws Error 7: The hypervisor does not have a client script that it can use to start with
	 * @throws Error 8: The game thread is somehow already alive
	 */
	private static void ReadyToRun() {	
			Logger.log("JGEL is making sure it's able to start..");
			
			if (JGELHasStarted) {ErrorManager.Error(17, null);}		//Static library already in use error
			if (StartScript == null) ErrorManager.Error(7, null);	//Not assigned error.
			if (GameThread.isAlive()) ErrorManager.Error(8, null);	//Already started error.
	}
	
	/**
	 * Closes the runtime via the error manager.
	 * 
	 * Ultimately, this method does not shutdown or notify any JGEL systems, threads or client that they will close.
	 * JGELJRE is terminated without prejudice. 
	 */
	public static void ForceExit() {
		ErrorManager.Error(15, null);		
	}

	/**
	 * Notifies then Shuts down the client, JGEL systems, and then the runtime environment.
	 * 
	 * This enforces shutdown phase one.
	 */
	public static void Shutdown() {
		ShutdownPhase1();
		ShutdownPhase2();
		Runtime.getRuntime().halt(-1);
	}
	
	/**
	 * Contains essential shutdown internal elements for JGEL
	 */
	private static void ShutdownPhase2() {
		HookUpdater.Halt();									//Stop update thread from updating the game
		WindowManager.CloseAll();							//Close all windows that are under the WindowManager's control.
	}
	
	/**
	 * Notifies and manages the client's termination.
	 */
	private static void ShutdownPhase1() {
		//TODO
	}
	
}
