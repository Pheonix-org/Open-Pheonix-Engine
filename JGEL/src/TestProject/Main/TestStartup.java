package TestProject.Main;

import javax.swing.JFileChooser;

import com.Shinkson47.JGEL.BackEnd.Configuration.Configuration;
import com.Shinkson47.JGEL.BackEnd.Operation.Startup.JGEStartupScript;
import com.Shinkson47.JGEL.BackEnd.Updating.HookUpdater;
import com.Shinkson47.JGEL.FrontEnd.Window.GameWindow;
import com.Shinkson47.JGEL.FrontEnd.Window.Rendering.JGELWindow;
import com.Shinkson47.JGEL.FrontEnd.Window.Rendering.RenderLayer;

public class TestStartup implements JGEStartupScript {

	/**
	 * This script is ran first, directly inside the hypervisor and should be used for initalising your game.
	 */
	public void JGEStartup() {
		//Creating a new window to display our game in can be done with one line.
		//This will create a window conforming to the default configuration, and will automatically hook itself into the hookupdater for frame updating.
		GameWindow MainWindow = new GameWindow();
		JGELWindow Window = new JGELWindow(Configuration.DefaultResolutionX, Configuration.DefaultResolutionY);
		MainWindow.SetWindow(Window);
		
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
		//For game updates, the HookUpdater should be used.
		//
		//When creating a class which you want to be updated, implement the 'EventHook' interface from BackEnd.Updating, 
		//then create scripts in the 'UpdateEvent' method.
		//the class can then be added to the event hook queue with 'HookUpdater.RegisterNewHook(this);'.
		
		//For the hook updater to trigger these update events, you can cause them manually like such:
		
		/**
		 * while(true) {
		 * 	HookUpdater.UpdateAll();
		 * 	break;
		 * }
		 */
		 
		//However, if you have no plans to use event loop and would rather just use event hooks, you can use:
		
		HookUpdater.AutoUpdateAll();
		
		//This will continue to update all hooks untill 'HookUpdater.DoAutoUpdate' is set to false.
		
		return; //return from this method to quit game.
	}
}
