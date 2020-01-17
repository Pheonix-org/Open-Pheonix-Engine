package BackEnd.ErrorManagement;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import BackEnd.ErrorManagement.Exceptions.JGELStaticException;
import BackEnd.Runtime.EnvironmentManager;
import FrontEnd.JGELWindowManager;

/**
 * The main Error Management System for JGEL.
 * 
 * @author gordie
 *
 */
public class JGELEMS {
	
	/**
	 * Class is static.
	 * @throws JGELStaticException
	 */
	private JGELEMS() throws JGELStaticException
	{
		throw new JGELStaticException("JGELEMS cannot be instantiated.");
	}

	
	
	
	
	
	//========================================
	//Properties
	
	private static int ErrorTollerance = 5, CascadeCount = 0, CascadeTollerance = 3;
	private static long LastErrorMillis = 0, MillisTollerance = 3000;
	private static boolean AllowEIS = true, AllowErrNotif = true, AllowCascadeDetection = true;

	/**
	 * List of all exceptions caught
	 */
	private static ArrayList<Throwable> CollectedThrowables = new ArrayList<Throwable>();

	/**
	 * Sets the EMS's tollerance for multiple errors.
	 * @param val
	 */
	
	public static void SetErrorTollerance(int val) {
		ErrorTollerance = val;	
	}
	

	public static int getErrorTollerance() {
		return ErrorTollerance;
	}
	
	/**
	 * @return the millisTollerance
	 */
	public static long getMillisTollerance() {
		return MillisTollerance;
	}


	/**
	 * @param millistollerance2 the millisTollerance to set
	 */
	public static void setMillisTollerance(long millistollerance2) {
		MillisTollerance = millistollerance2;
	}


	/**
	 * @return the cascadeTollerance
	 */
	public static int getCascadeTollerance() {
		return CascadeTollerance;
	}


	/**
	 * @param cascadeTollerance the cascadeTollerance to set
	 */
	public static void setCascadeTollerance(int cascadeTollerance) {
		CascadeTollerance = cascadeTollerance;
	}


	/**
	 * @return the lastErrorMillis
	 */
	public static long getLastErrorMillis() {
		return LastErrorMillis;
	}


	/**
	 * @return the cascadeCount
	 */
	public static int getCascadeCount() {
		return CascadeCount;
	}
	
	public static void SetAllowEIS(boolean val) {
		AllowEIS = val;
	}
	
	public static boolean getAllowEIS() {
		return AllowEIS;
	}	
	
	public static void SetAllowErrNofif(boolean val) {
		AllowErrNotif = val;
	}
	
	public static boolean getErrNotif() {
		return AllowErrNotif;
	}
	
	public static void SetAllowCascadeDetection(boolean val) {
		AllowCascadeDetection = val;
	}
	
	/**
	 * Main exception handler for JGEL.
	 * All internal threads use this handler.
	 * This should be the main way for the JGELEMS to be called in an error state.
	 */
	private static UncaughtExceptionHandler ExceptionHandler = new UncaughtExceptionHandler() {
		@Override
		public void uncaughtException(Thread t, Throwable e) {
			JGELEMS.HandleException(e);
		}
	};
	
	public static UncaughtExceptionHandler GetHandler() {
		return ExceptionHandler;
	}
	
	
	
	
	
	
	//=====================================
	//Error management

	
	/**
	 * Main exception handling method.
	 * @see Error Management
	 * 
	 * @param thread - Thread thrown from.
	 * @param e - Thrown exception.
	 */
	public static void HandleException(Throwable e) {
		CollectedThrowables.add(e);								//Add throwable to memory ArrayList.
		
		//TODO Waiting for data handler Data.Backup();			//Take a backup of game save data.
		
		JGELLogger.log(Thread.currentThread().getClass().getSimpleName() + 		//Create a log of the error.
				"'s thead threw an exception: " + 
				e.getMessage() + 
				" - caused by " + 
				e.getCause());
		
		if (AllowErrNotif) {									//If notification is on
	//TODO ThreadManager.WaitAll(); Waiting for thread manager //Pause all threads
																//Show notifiction
			JOptionPane.showMessageDialog(JGELWindowManager.SwingParent, "JGEL has encountered an error. Data will be backed up. Further problems may occour.");
			
			//ThreadManager.NotifyAll();						//Unpause all threads
		}
		
		if (CollectedThrowables.size() > ErrorTollerance) {		//If throwables tollerance is expended, invoke EIS.
				InvokeEIS();
		}
	}
	
	/**
	 * Uses time signatures to automatically detect error cascades.
	 * 
	 * @see Error Management, Cascade auto detect
	 */
	private static void DetectCascase(Throwable e) {
		if (!AllowCascadeDetection) {	
			return;															//Return if not enabled
		}
		
		if (LastErrorMillis == 0) {
			LastErrorMillis = System.currentTimeMillis(); 					//If there's been no detections yet there's no cascade.
			return;															//Log time and return
		}
		
		if (System.currentTimeMillis() - LastErrorMillis < MillisTollerance) { //Error is within time tollerance
			CascadeCount++;													 //Accept as cascade
		} else {
			CascadeCount = 1;												//Else time frame has passed, reset cascade count (including current error)
		}
		
		if (CascadeCount > CascadeTollerance) {								//If cascade tollerance is exceeded
			if (AllowEIS) {													//Check EIS perms
				InvokeEIS();												//Invoke EIS
			} else {
				JOptionPane.showMessageDialog(JGELWindowManager.SwingParent, "A cascade of errors has been detected, but JGEL has been instructed to not shutdown.");
				JOptionPane.showMessageDialog(JGELWindowManager.SwingParent, "It's recommended that you save and restart ASAP.");
			}
		}
	}
	
	
	/**
	 * Warns user of non-crucial failure.
	 * 
	 * @see Error Management, # Warns
	 * @param t - Thread that the warn occurred in
	 * @param s - String value of the warning.
	 */
	public static void Warn(String s) {
		JGELLogger.log("[@Warn] " + Thread.currentThread().getClass().getSimpleName() + " says " + s);
	}
	
	public static void InvokeEIS() {
		JOptionPane.showMessageDialog(JGELWindowManager.SwingParent, "JGEL Runtime is experiencing problems and is closing.");
		JOptionPane.showMessageDialog(JGELWindowManager.SwingParent, "User data will be saved.");
		JGELLogger.CrashDump();
		EnvironmentManager.Shutdown();
	}

	/**
	 * Clears all collected throwables from store.
	 */
	public static void ClearEMS() {
		CollectedThrowables.clear();
	}

}
