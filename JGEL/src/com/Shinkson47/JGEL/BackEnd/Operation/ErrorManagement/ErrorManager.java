package com.Shinkson47.JGEL.BackEnd.Operation.ErrorManagement;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import javax.swing.JOptionPane;

import com.Shinkson47.JGEL.BackEnd.Operation.Diagnostics.Logger;
import com.Shinkson47.JGEL.BackEnd.Updating.HookUpdater;
import com.Shinkson47.JGEL.FrontEnd.Window.WindowManager;

public class ErrorManager {
	
	//an exit code of -1 is a JGEL intended safe shutdown; not an unexpected termination NOT 0
	public static String ErrorMessages[] = {
	/*0*/	"A generic error has occoured", 									
	/*1*/	"An unknown error has occoured",									
	/*2*/	"An unknwon error but was caught by the hyper visor",								//Exception parsed by the ExceptionHandler in the threads.			
	/*3*/	"An unknown error has occoured within JGEL",										//Unknown caught exception in JGEL		
	/*4*/	"An error occoured within the game's startup sequence!",							//Exception in the game 'Startup'			
	/*5*/	"An error was parsed, but the error code was not valid.",										
	/*6*/	"The startup script within the hypervisor cannot be changed once set!", 			//an attempt to reassign the hypervisor was made
	/*7*/	"The hypervisor cannot start without a startup script!",				   
	/*8*/	"The hypervisor cannot start, the game is already running!",						//Hypervisor attempted to start, but was already running.
	/*9*/	"The hypervisor cannot monitor the game thread, there is no thread to monitor!",	//Hypervisor attempted to monitor a thread, but there wasn't one.
	/*10*/	"The hypervisor cannot monitor the game thread, the thread is not alive!",			//Hypervisor attempted to monitor a thread, but it wasn't alive.
	/*11*/	"An error occoured within an game update hook!",									//The HookUpdater triggered a hook, and there was an error within the hook.
	/*12*/	"An error occoured within the HookUpdater!",										//The HookUpdater encountered an error, check for clashing of multiple threads (concurrent exception);
	/*13*/	"This error code is unused.",
	/*14*/	"This error code is unused.",
	/*15*/	"JGEL forced the runtime to exit without prejudice.",
	/*16*/	"An error occoured within a keyboard input handler.",
	/*17*/ "The hypervisor attempted to start, but the static JGEL library was already in use. JGEL may only be started once per runtime execution."
	
	};
	
	private static String WarnMessages[] = {
	/*0*/ "An attempt to deregister an event hook was made, but the remove queue was already in use. THE HOOK WAS NOT REMOVED!",
	/*1*/ "An attempt to print a warning failed because the issued warn code was not valid.",
	/*2*/ "An attempt to deregister an event hook from an index was made but failed because the index was not valid. THE HOOK WAS NOT REMOVED!",
	/*3*/ "Something interupted the HookUpdate loop. It will restart, and miss out hooks that weren't yet updated in this loop.",
	/*4*/ "After 5 attempts, JGEL failed to create a crash log, likely a concurrent modification exception.",
	/*5*/ "Static dereg was called, but there was no registered hooks that match the specified class name.",
	/*6*/ "WaitForNull timed out after 1000 game loops and returned.",
	/*7*/ "Keybind address was out of range, the key was not bound.",
	/*8*/ "The hypervisor attempted to monitor the game thread, but thread monitoring was not enabled. Use Hypervisor.SetDoMonitor(b);",
	/*9*/ "JGEL searched a class for a method to invoke, but could not find one that matched."
	};
	
	/**
	 * Determins wether the JGEL Error Manager will shutdown JGEL when parsed an error.
	 * TODO this should be in the configuration class.
	 */
	public static boolean ExceptionsHalt = true;

	/**
	 * Determins weather an ignored exception message has been shown; it should only be shown once.
	 */
	private static boolean ExceptionMessageShown;
	
	/**
	 * Intended to give information or warning of an impending crash which could be useful in debugging.
	 *
	 * @param warning
	 */
	public static void PreWarn(String warning) {
		Logger.log("[@PreWarn] Something has suggested that crash is about to occour and wants to give you a message: ");
		Logger.log("[@PreWarn] " + warning);
	}
	
	/**
	 * Used for internal minor errors or to notify of other problems which do not require JGEL or the client to be closed. 
	 * 
	 * @param WarnCode code of the warning
	 */
	public static void Warn(int WarnCode) {
		if (WarnCode > WarnMessages.length || WarnCode < 0) {	//Check warn code is valid.
			Warn(1);
			return;
		}		
		
		Logger.log("[@Warn] " + WarnMessages[WarnCode]);		//print warn message.
	}
	
	/**
	 * Gets the error message and displays it, and then closes the JGELJRE.
	 * This method does not shutdown JGEL or the client. 
	 * @param errorCode
	 * @param e
	 */
	public static void Error(int errorCode, Exception e) {
		Logger.log("[@Crash] JGEL is handling an indicated error.");
		
		//CHECK CODE
		if (errorCode -1 > ErrorMessages.length) {						//Is the code out of index?
			Error(5, e);												//Throw dual error to continue handling, and parse existing exception
		}
			
		//HANDLE ERROR
		
		
		//GET STACK TRACE
		String StackTrace = "";
		ByteArrayOutputStream StackBuffer = new ByteArrayOutputStream();
		PrintStream BufferWriter = new PrintStream(StackBuffer);
		if (e == null) {
			Logger.log("No exception was parsed, getting stack trace of error call");
			
			try {
				new Exception("[Error " + errorCode + "] " + ErrorMessages[errorCode]).printStackTrace(BufferWriter);
				StackTrace = StackBuffer.toString("UTF8");
				
			} catch (UnsupportedEncodingException e1) {
				StackTrace = "Unable to get stack trace :(";
			}
		} else {
			Logger.log("An exception was parsed, extracting stack trace.");
			e.printStackTrace(BufferWriter);
			StackTrace = StackBuffer.toString();
		}
		
		Logger.log(StackTrace);
		Logger.CreateCrashLog();
		
		if (!ExceptionsHalt) {
			if (ExceptionMessageShown) return;
			JOptionPane.showMessageDialog(WindowManager.SwingParent, "JGEL has detected a problem, but has been instructed not to shutdown.");
			JOptionPane.showMessageDialog(WindowManager.SwingParent, "It is recommended that you save before continuing, you may experience more problems.");
			ExceptionMessageShown = true;
			return;
		}
		
		JOptionPane.showMessageDialog(WindowManager.SwingParent, "JGEL has detected a problem, and will now shutdown :(. Reffer to the log for more details.");
		Hypervisor.Shutdown();
	}
}
