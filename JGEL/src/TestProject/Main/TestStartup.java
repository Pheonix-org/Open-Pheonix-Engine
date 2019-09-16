package TestProject.Main;

import com.Shinkson47.JGEL.BackEnd.Configuration.Configuration;
import com.Shinkson47.JGEL.BackEnd.Operation.Startup.JGEStartupScript;
import com.Shinkson47.JGEL.BackEnd.Updating.HookUpdater;
import com.Shinkson47.JGEL.FrontEnd.Window.WindowManager;

public class TestStartup implements JGEStartupScript {

	/**
	 * This script is ran first, directly inside the hypervisor and should be used for initalising your game.
	 */
	public void JGEStartup() {
	 Configuration.DeveloperName = "Shinkson47";		
	 Configuration.ClientName = "JGEL Test Demo Project";
	 Configuration.ClientVersion = (float) 69.420;
	}
	
	/**
	 * This method is ran inside a new thread and should be used for you application's main loop, or to start your application.
	 * 
	 * If your game returns from this method, it will be treated as an intended shutdown.
	 * 
	 * Uncaught exceptions within this method will be caught be the hypervisor and will cause an Error Halt of the JRE.
	 */
	@Override
	public void run() {	
		WindowManager.newWindow(); //Create a window for the game
			
		//For game updates, the static HookUpdater should be used.
		//
		//When creating a class which you want to be updated, implement the 'EventHook' interface from BackEnd.Updating, 
		//then create scripts in the 'UpdateEvent' method.
		//the class can then be added to the event hook queue with 'HookUpdater.RegisterNewHook(this);'.
		
		//Updates outside of the hookupdater are disadvised.
		//Hooks should be registered in the Game's startup script.

	}
}
