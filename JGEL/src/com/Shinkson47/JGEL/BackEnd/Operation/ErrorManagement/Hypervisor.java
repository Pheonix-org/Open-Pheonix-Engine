package com.Shinkson47.JGEL.BackEnd.Operation.ErrorManagement;

import java.lang.Thread.UncaughtExceptionHandler;

import com.Shinkson47.JGEL.BackEnd.Operation.Diagnostics.Logger;
import com.Shinkson47.JGEL.BackEnd.Operation.Startup.JGEStartupScript;

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
	private boolean HasStartScript = false;
	
	/**
	 * Stores the startup script of the user's game.
	 */
	private JGEStartupScript StartScript;
	
	/**
	 * Thread used for the game's excecution.
	 */
	private Thread GameThread = new Thread();
	
	/**
	 * ExceptionHandler used to catch exceptions thrown within the user's game.
	 */
	private static UncaughtExceptionHandler ExceptionHandler = new UncaughtExceptionHandler() {
		@Override
		public void uncaughtException(Thread t, Throwable e) {
			ErrorManager.Error(2);			
		}
	};
	
	
	
	/**
	 * Create an instance of the hypervisor and assign the user's game at the same time.
	 * @param StartupScript
	 */
	public Hypervisor(JGEStartupScript StartupScript) {
		Logger.log("A new JGEL Hypervisor has been created, and will automatically start the game.");
		if (HasStartScript) ErrorManager.Error(6); 		//Already assigned error
		
		StartScript = StartupScript;					//else, Assign
		HasStartScript = true;
		Start();										//Auto start
	}
	
	/**
	 * Create an unassigned hypervisor for the application to be assigned to later.
	 */
	public Hypervisor() {
		HasStartScript = false;
		Logger.log("A new JGEL Hypervisor has been created, but has not yet been assigned a startup script");
	}
	
	public void Assign(JGEStartupScript script) {
		Logger.log("JGEL has been assigned a startup script and is ready to start.");
		if (HasStartScript) ErrorManager.Error(6);		//Already assigned error
		StartScript = script;							//else, Assign
		HasStartScript = true;
	}

	@SuppressWarnings("static-access")
	public void Start() {
		Logger.log("JGEL is preparing to start the game; Checking the operating thread..");
		if (!HasStartScript) ErrorManager.Error(7);		//Not assigned error.
		
		if (GameThread.isAlive()) ErrorManager.Error(8);//Already started error.
		
		Logger.log("JGEL appears ready to start, excecuting the prestart script");
		try {
			StartScript.JGEStartup();					//Trigger the prestart script
		} catch (Exception e) {
			ErrorManager.Error(4);
		}
		
		Logger.log("Prestart completed without fault, assigning and starting game thread");
		GameThread = new Thread(StartScript);			//Start the game
		GameThread.setDefaultUncaughtExceptionHandler(ExceptionHandler);
		GameThread.start();
		MonitorThread();								//Monitor the game.
	}

	public void MonitorThread() {
		if (!HasStartScript) ErrorManager.Error(9);		//No thread to monitor.
		
		if (!GameThread.isAlive()) ErrorManager.Error(10);//Thread not active.
		
		Logger.log("The hypervisor is monitoring a game thread.");
		while (GameThread.isAlive()) {
			
		}
	}
	
}
