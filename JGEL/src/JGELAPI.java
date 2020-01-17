import java.lang.Thread.UncaughtExceptionHandler;

import BackEnd.ErrorManagement.JGELEMS;
import BackEnd.ErrorManagement.Exceptions.JGELStaticException;
import BackEnd.ErrorManagement.Exceptions.JGELThreadAlive;
import BackEnd.ErrorManagement.Exceptions.JGELThreadPersistance;
import BackEnd.Runtime.Threading.JGELThread;
import BackEnd.Runtime.Threading.JGELThreadManager;

/**
 * Main wrapper for external use of the library. Interactions between client and api
 * pass through this class.
 * 
 * This class, to follow the rest of the library, is not instatiable.
 * JGEL is static only.
 * 
 * @author gordie
 */
public final class JGELAPI {
	
	/**
	 * Private instantiator prevents external instantiation, but 
	 * @throws JGELStaticException anyway.
	 * 
	 * JGEL is static only.
	 */
	private JGELAPI() throws JGELStaticException {
		throw new JGELStaticException();
	}

	
	
	
	
	// ======================================================
	// REGION RUNTIME ENVIRONMENT
	
	/**
	 * Main API call to start JGEL and a client with one call.
	 * 
	 * side calls API to start JGEL systems, then starts executing client.
	 */
	public static void StartClient() {
		
	}
	
	/**
	 * Executes internal startup routine.
	 * 
	 * @apiNote This call only starts internal systems, and does not start a client.
	 * @apiNote This call does not execute post start scripts. Post start requires an active client.
	 */
	public static void StartAPI() {
		
	}

	/**
	 * Unsafe, instantanious shutdown of the Java Runtime Environment.
	 * 
	 * Not recommended. 
	 * @see Shutdown
	 */
	public static void Halt() {
		Runtime.getRuntime().halt(1);
	}
	
	/**
	 * Save data, shutdown client, shutdown JGEL, halt JRE.
	 */
	public static void shutdown() {
		
	}
	
	
	
	
	
	
	// ======================================================
	//REGION ERROR MANAGEMENT
	
	/**
	 * Notifies the error management system that an exception has occoured.
	 * 
	 * @see JGEL EMS - Error Management
	 * @param e - Exception to parse
	 */
	public static void EMSHandle(Exception e) {
		JGELEMS.HandleException(e);
	}
	
	/**
	 * Discreely logs non-crucial failure.
	 * 
	 * @see Error Management, # Warns
	 * 
	 * @param s - text value of the warning.
	 */
	public static void EMSWarn(String s) {
		JGELEMS.Warn(s);
	}
	
	/**
	 * Clears the EMS' memory of all exceptions.
	 * Resets EIS tollerance.
	 */
	public static void ClearEMS() {
		JGELEMS.ClearEMS();
	}
	
	/**
	 * Get the main exception handler for threading use.
	 * 
	 * This is not encouraged, since new threads should be created via JGEL.
	 * @see ThreadManagement
	 * @return
	 */
	public static UncaughtExceptionHandler getExceptionHandler() {
		return JGELEMS.GetHandler();
	}
	
	
	
	
	
	
	// ======================================================
	//REGION THREAD MANAGEMENT 
	
	public static void CreateThread(JGELThread target){
		JGELThreadManager.CreateThread(target);
	}
	
	public static void ForceDisposeThread(JGELThread target) {
		try {
			JGELThreadManager.ForceDispose(target);
		} catch (JGELThreadAlive | JGELThreadPersistance e) {
			JGELEMS.HandleException(e);
		}
	}
	
	
	
}