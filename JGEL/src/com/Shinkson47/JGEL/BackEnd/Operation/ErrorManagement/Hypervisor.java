package com.Shinkson47.JGEL.BackEnd.Operation.ErrorManagement;

import java.lang.Thread.UncaughtExceptionHandler;

import com.Shinkson47.JGEL.BackEnd.Configuration.Configuration;
import com.Shinkson47.JGEL.BackEnd.Operation.Diagnostics.Logger;
import com.Shinkson47.JGEL.BackEnd.Operation.Startup.JGEStartupScript;
import com.Shinkson47.JGEL.BackEnd.Updating.HookUpdater;

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
	 * Stores weather or not a start script has been assigned yet.
	 */
	private static boolean HasStartScript = false;
	
	/**
	 * Stores wether or not JGEL has been started.
	 */
	private static boolean JGELHasStarted = false;
	
	/**
	 * Stores the startup script of the user's game.
	 */
	private static JGEStartupScript StartScript;
	
	/**
	 * Thread used for the game's excecution.
	 */
	private static Thread GameThread = new Thread();
	
	/**
	 * Thread used for updating the game via JGEL
	 */
	private static Thread HookUpdaterThread;
	
	/**
	 * ExceptionHandler used to catch exceptions thrown within the user's game.
	 */
	private static UncaughtExceptionHandler ExceptionHandler = new UncaughtExceptionHandler() {
		@Override
		public void uncaughtException(Thread t, Throwable e) {
			ErrorManager.Error(2, null);			
		}
	};
	

	/**
	 * Create an instance of the hypervisor and assign the user's game at the same time.
	 * @param StartupScript
	 */
	@Deprecated //Statification of the Hypervisor makes constructors redundant.
	public Hypervisor(JGEStartupScript StartupScript) {
	}
	
	/**
	 * Create an unassigned hypervisor for the application to be assigned to later.
	 */
	@Deprecated //Statification of the Hypervisor makes constructors redundant.
	public Hypervisor() {
	}
	
	@Deprecated //Simplifcation if the hypervisor's interface makes script assignments redundant.
	public static void Assign(JGEStartupScript script) {

	}

	@SuppressWarnings("static-access")
	public static void Start(JGEStartupScript script) {
		if (JGELHasStarted) {
			
		}
		
		Logger.log("======================================");
		Logger.log(" 	Created using JGE ");
		Logger.log("	Library version ");
		Logger.log("		" + Configuration.JGEL_VERSION);			//Startup log
		Logger.log("======================================");
		Logger.log("[JGEL] JGE is preparing to start.");
		
		//ASSIGN SCRIPT
		if (HasStartScript) ErrorManager.Error(6, null);		//Already assigned error
		StartScript = script;									//else, Assign
		HasStartScript = true;
		
		//CHECK IF ABLE TO RUN
		Logger.log("[JGEL] JGE is checking the operating thread..");
		if (!HasStartScript) ErrorManager.Error(7, null);		//Not assigned error.
		
		if (GameThread.isAlive()) ErrorManager.Error(8, null);	//Already started error.
		
		//PRESTART
		Logger.log("[JGEL] JGE appears ready to start, excecuting the prestart script");
		try {
			StartScript.JGEStartup();							//Trigger the prestart script
		} catch (Exception e) {
			ErrorManager.Error(4, null);
		}
		
		//START
		Logger.log("Prestart completed without fault, assigning and starting game thread");
		GameThread = new Thread(StartScript);					//Start the game
		GameThread.setName("Client Thread");
		GameThread.setDefaultUncaughtExceptionHandler(ExceptionHandler);
		GameThread.start();
		
		HookUpdaterThread = new Thread(new HookUpdater());		//Create a new thread to handle game updates
		HookUpdaterThread.setName("Hook Updater Thread");
		HookUpdaterThread.start();								//and start it.
		
		Configuration.StartTime = System.nanoTime();
		
		
		
		//MONITOR
		MonitorThread();										//Monitor the game.
	}

	private static void MonitorThread() {
		
		//CHECK
		if (!HasStartScript) ErrorManager.Error(9, null);		//No thread to monitor.
		
		if (!GameThread.isAlive()) ErrorManager.Error(10, null);//Thread not active.
		
		//MONITOR
		Logger.log("The hypervisor is monitoring a game thread.");
		while (true) {
			
		}
	}
}
