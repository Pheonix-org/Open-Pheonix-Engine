package backend.errormanagement;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import backend.errormanagement.exceptions.JGELStaticException;
import backend.runtime.JGELEnvironmentUtils;
import frontend.windows.JGELWindowHelper;

/**
 * The main Error Management System for JGEL.
 * 
 * @author gordie
 */
public class EMSHelper {

	//========================================
	//Properties

	private static int ErrorTollerance = 5; 
	private static int CascadeCount = 0;
	private static int CascadeTollerance = 3;
	private static long LastErrorMillis = 0;
	private static long MillisTollerance = 3000;
	
	private static boolean AllowEIS = true;
	private static boolean AllowErrNotif = true;
	private static boolean AllowCascadeDetection = true;

	/**
	 * List of all exceptions caught
	 */
	private static ArrayList<Throwable> CollectedThrowables = new ArrayList<Throwable>();


	/**
	 * Class is static. Private instantiator hides instantiation.
	 * @throws JGELStaticException
	 */
	private EMSHelper() throws JGELStaticException
	{
		throw new JGELStaticException("JGELEMS cannot be instantiated.");
	}

	/**
	 * Sets the EMS's tollerance for multiple errors.
	 * @param val
	 */
	public static void setErrorTollerance(int val) {
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

	public static void setAllowEIS(boolean val) {
		AllowEIS = val;
	}

	public static boolean getAllowEIS() {
		return AllowEIS;
	}	

	public static void setAllowErrNofif(boolean val) {
		AllowErrNotif = val;
	}

	public static boolean getErrNotif() {
		return AllowErrNotif;
	}

	public static void setAllowCascadeDetection(boolean val) {
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
			EMSHelper.handleException(e);
		}
	};

	public static UncaughtExceptionHandler getHandler() {
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
		if (Silent) {
			return; //Don't continue to save, message, casecade or eis.
		}
		
		CollectedThrowables.add(e);								//Add throwable to memory ArrayList

		if (AllowErrNotif) {				     				//If notification is on
			//TODO ThreadManager.WaitAll(); Waiting for thread manager //Pause all threads
			//Show notifiction
			JOptionPane.showMessageDialog(JGELWindowHelper.getSwingParent(), "JGEL has encountered an error. Data will be backed up. Further problems may occour.");

			//ThreadManager.NotifyAll();						//Unpause all threads
		}

		if (CollectedThrowables.size() > ErrorTollerance) {		//If throwables tollerance is expended, invoke EIS.
			invokeEIS();
		}
		detectCascade();
	}

	 * Main exception handling method.
	 * @see Error Management
	 * 
	 * @param thread - Thread thrown from.
	 * @param e - Thrown exception.
	 */
	public static void handleException(Throwable e) {
		handleException(e, false);
	}

	/**
	 * Uses time signatures to automatically detect error cascades. Uses each call to this method as reference.
	 * 
	 * @see Error Management, Cascade auto detect
	 */
	private static void detectCascade() {
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
				invokeEIS();												//Invoke EIS
			} else {

				JOptionPane.showMessageDialog(JGELWindowHelper.getSwingParent(), "A cascade of errors has been detected, but JGEL has been instructed to not shutdown.");
				JOptionPane.showMessageDialog(JGELWindowHelper.getSwingParent(), "It's recommended that you save and restart ASAP.");
			}
		}
	}


	/**
	 * Warns user of non-crucial failure.
	 * 
	 * @see Error Management, # Warns
	 * @param s - String value of the warning.
	 */
	public static void warn(String s) {
		JGELLoggerUtils.log("[@Warn] " + Thread.currentThread().getClass().getSimpleName() + " says " + s);
	}


	public static void invokeEIS() {
		JOptionPane.showMessageDialog(JGELWindowHelper.getSwingParent(), "JGEL Runtime is experiencing problems and is closing.");
		JOptionPane.showMessageDialog(JGELWindowHelper.getSwingParent(), "User data will be saved.");
		JGELLoggerUtils.crashDump();
		JGELEnvironmentUtils.shutdown();
	}

	/**
	 * Clears all collected throwables from store.
	 */
	public static void clearEMS() {
		CollectedThrowables.clear();
	}

}
