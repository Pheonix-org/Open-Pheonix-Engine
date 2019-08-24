package TestProject.Main;

import com.Shinkson47.JGEL.BackEnd.Operation.Startup.JGEStartupScript;

public class TestStartup implements JGEStartupScript {

	/**
	 * This script is ran first, directly inside the hypervisor and should be used for initalising your game.
	 */
	public void JGEStartup() {
		System.out.println("Startup script has excecuted");
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
		System.out.println("Application thread has been executed.");
		while(true) {
			//Application execution loop

			break; //return to quit.
		}
	}
}
