package com.Shinkson47.JGEL.BackEnd.Operation.ErrorManagement;

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
	};
	
	/**
	 * Gets the error message and displays it, and then closes the JRE.
	 * 
	 * @param errorCode
	 */
	public static void Error(int errorCode) {
		if (errorCode -1 > ErrorMessages.length) {
			System.out.println("The error code parsed was not valid.");
			Error(5);
		} else {
			System.out.println(ErrorMessages[errorCode]);
		}
		
				Runtime.getRuntime().halt(errorCode);
	}

}
