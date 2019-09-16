package com.Shinkson47.JGEL.BackEnd.Operation.ErrorManagement;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import javax.swing.JOptionPane;

import com.Shinkson47.JGEL.BackEnd.Operation.Diagnostics.Logger;
import com.Shinkson47.JGEL.BackEnd.Updating.HookUpdater;
import com.Shinkson47.JGEL.FrontEnd.Window.WindowManager;

public class ErrorManager {
	public static String ErrorMessages[] = {
	/*0*/	"A generic error has occoured", 									
	/*1*/	"An unknown error has occoured",									
	/*2*/	"An unknwon error has occoured within the game",										//Exception in the game 'run'			
	/*3*/	"[JGEL] An unknown error has occoured within JGEL",										//Unknown caught exception in JGEL		
	/*4*/	"An error occoured within the game's startup sequence!",								//Exception in the game 'Startup'			
	/*5*/	"An error was parsed, but the error code was not valid.",										
	/*6*/	"[JGEL] The startup script within the hypervisor cannot be changed once set!", 			//an attempt to reassign the hypervisor was made
	/*7*/	"[JGEL] The hypervisor cannot start without a startup script!",				   
	/*8*/	"[JGEL] The hypervisor cannot start, the game is already running!",						//Hypervisor attempted to start, but was already running.
	/*9*/	"[JGEL] The hypervisor cannot monitor the game thread, there is no thread to monitor!",	//Hypervisor attempted to monitor a thread, but there wasn't one.
	/*10*/	"[JGEL] The hypervisor cannot monitor the game thread, the thread is not alive!",		//Hypervisor attempted to monitor a thread, but it wasn't alive.
	/*11*/	"[JGEL] An error occoured within an game update hook!",									//The HookUpdater triggered a hook, and there was an error within the hook.
	/*12*/	"[JGEL] An error occoured within the HookUpdater!",										//The HookUpdater encountered an error, check for clashing of multiple threads (concurrent exception);
	/*13*/	"[JGEL] The game attempted to register a new Update Hook, but JGEL failed to register it in a timely manor.",
	/*14*/	"[JGEL] The game attempted to DEregister an Update Hook, but JGEL failed to remove it in a timely manor."
	
	};
	
	/**
	 * Gets the error message and displays it, and then closes the JRE.
	 * 
	 * @param errorCode
	 * @param e
	 */
	public static void Error(int errorCode, Exception e) {
		
		//CHECK CODE
		if (errorCode -1 > ErrorMessages.length) {						//Is the code out of index?
			Error(5, e);												//Throw dual error to continue handling, and parse existing exception
		}
			
		//HANDLE ERROR
		HookUpdater.Halt();									//Stop update thread from updating the game
		WindowManager.CloseAll();							//Close all windows that are under the WindowManager's control.
		
		
		//GET STACK TRACE
		String StackTrace = "";
		ByteArrayOutputStream StackBuffer = new ByteArrayOutputStream();
		PrintStream BufferWriter = new PrintStream(StackBuffer);
		if (e == null) {
			Logger.log("[JGEl] No exception was parsed, getting stack trace.");
			
			try {
				new Exception("[Error " + errorCode + "] " + ErrorMessages[errorCode]).printStackTrace(BufferWriter);
				StackTrace = StackBuffer.toString("UTF8");
				
			} catch (UnsupportedEncodingException e1) {
				StackTrace = "Unable to get stack trace :(";
			}
		} else {
			Logger.log("[JGEl] An exception was parsed, extracting stack trace.");
			e.printStackTrace(BufferWriter);
			StackTrace = StackBuffer.toString();
		}
		
		Logger.log(StackTrace);
		Logger.CreateCrashLog();
		
		JOptionPane.showMessageDialog(WindowManager.SwingParent, "JGEL has detected a problem, and has closed :(. Reffer to the log for more details.");
		System.exit(errorCode);

	}
}
