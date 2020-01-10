package com.Shinkson47.JGEL.BackEnd.Operation.Startup;

import java.nio.file.Paths;

import com.Shinkson47.JGEL.BackEnd.Configuration.Configuration;
import com.Shinkson47.JGEL.BackEnd.General.GeneralTools;
import com.Shinkson47.JGEL.BackEnd.Operation.ErrorManagement.Hypervisor;
import com.Shinkson47.JGEL.BackEnd.Resources.ResourceLoader;
import com.Shinkson47.JGEL.BackEnd.Updating.HookUpdater;

public class InternalStartScript implements JGEStartupScript {

	/**
	 * 	Defines extra methods to run upon the startup of JGEL.
	 * 
	 *  This is ran after the game client has started
	 */
	@Override
	public void run() {
		
	}

	/**
	 * Defines extra functions to run upon the startup of JGEL.
	 * 
	 * This is ran before the game client starts.
	 */
	@Override
	public void JGEStartup() {
		//ResourceLoader.LoadResourceFolder(Paths.get(Configuration.ResourceFolder)); //This is ran after client start to allow the client to define a different resource folder if it so desires.
		CreateHookUpdater();
	}

	private void CreateHookUpdater() {
		Thread HookUpdaterThread = Hypervisor.HookUpdaterThread;
		HookUpdaterThread = new Thread(new HookUpdater());		//Create a new thread to handle game updates
		HookUpdaterThread.setName("Hook Updater Thread");
		HookUpdaterThread.setUncaughtExceptionHandler(Hypervisor.ExceptionHandler);
		Configuration.StartTime = System.currentTimeMillis();	//Log time of thread start
		HookUpdaterThread.start();								//and start it.
	}
}
