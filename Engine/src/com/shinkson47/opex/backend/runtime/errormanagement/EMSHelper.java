package com.shinkson47.opex.backend.runtime.errormanagement;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.shinkson47.opex.backend.runtime.console.Console;
import com.shinkson47.opex.backend.runtime.errormanagement.exceptions.OPEXStaticException;
import com.shinkson47.opex.backend.runtime.environment.RuntimeHelper;
import com.shinkson47.opex.backend.toolbox.HaltCodes;
import com.shinkson47.opex.frontend.window.OPEXWindowHelper;

/**
 * The main Error Management System for OPEX.
 *
 * @version 2020.7.6.A
 * @author gordie
 */
public class EMSHelper {

	//#region tollerances
	/**
	 *
	 */
	private static byte ErrorTollerance = 5;

	/**
	 *
	 */
	private static byte CascadeTollerance = 3;

	/**
	 *
	 */
	private static long MillisTollerance = 3000;
	//#endregion

	//#region values
	/**
	 *
	 */
	private static short CascadeCount = 0;

	/**
	 *
	 */
	private static long LastErrorMillis = 0;

	/**
	 * List of all exceptions caught
	 */
	private static ArrayList<Throwable> CollectedThrowables = new ArrayList<>();
	//#endregion

	//#region Settings
	/**
	 *
	 */
	private static boolean AllowEIS = true;

	private static boolean DumpStack = true;

	/**
	 *
	 */
	private static boolean AllowErrNotif = true;

	/**
	 *
	 */
	private static boolean AllowCascadeDetection = true;
	//#endregion


	/**
	 * Class is static. Private instantiator prevents instantiation.
	 * @throws OPEXStaticException - class is static.
	 */
	private EMSHelper() throws OPEXStaticException {throw new OPEXStaticException(this);}

	/**
	 * @param val - tolerance for multiple errors
	 */
	public static void setErrorTollerance(byte val) {
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
	public static void setCascadeTollerance(byte cascadeTollerance) {
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
	 * Main exception handler for OPEX. All internal threads use this handler. This
	 * should be the main way for the OPEXEMS to be called in an error state.
	 */
	private static UncaughtExceptionHandler ExceptionHandler = (t, e) -> EMSHelper.handleException(e);

	public static UncaughtExceptionHandler getHandler() {
		return ExceptionHandler;
	}

	// =====================================
	// Error management

	/**
	 * Main exception handling method.
	 * 
	 * @see Error Management
	 *
	 * @param Silent - Determines if the exception is logged. If silent, exception is handled but not logged.
	 * @param e      - Thrown exception.
	 */
	public static void handleException(Throwable e, Boolean Silent) {
		CollectedThrowables.add(e); // Add throwable to memory ArrayList.

		// TODO Waiting for data handler Data.Backup(); //Take a backup of game save
		// data.
		StackTraceElement Thrower = e.getStackTrace()[0];
		logEMS("Caught exception: [" + e.getClass().getSimpleName() + "] from '" + Thrower.getClassName() + "' in '" +  Thrower.getMethodName() + "' (Line: "+ Thrower.getLineNumber() +")", Silent);
		if (DumpStack && !Silent) e.printStackTrace();

		if (AllowErrNotif) { // If notification is on
			if (Silent) return; // Don't continue to save, message, casecade or eis.

			// TODO ThreadManager.WaitAll(); Waiting for thread manager //Pause all threads
			// Show notifiction
			JOptionPane.showMessageDialog(OPEXWindowHelper.getSwingParent(),
					"OPEX has encountered an error. Data will be backed up. Further problems may occour.");


			CollectedThrowables.add(e); // Add throwable to memory ArrayList

			// ThreadManager.NotifyAll(); //Unpause all threads

			if (CollectedThrowables.size() > ErrorTollerance) { // If throwables tollerance is expended, invoke EIS.
				invokeEIS();
			}
			detectCascade();
		}
	}

	/**
	 * Main exception handling method.
	 * @param e - Thrown exception.
	 */
	public static void handleException(Throwable e) {
		handleException(e, false);
	}

	/**
	 * Uses time signatures to automatically detect error cascades. Uses each call
	 * to this method as reference.
	 *
	 * @see Error Management, Cascade auto detect
	 */
	private static void detectCascade() {
		if (!AllowCascadeDetection) {
			return; // Return if not enabled
		}

		if (LastErrorMillis == 0) {
			LastErrorMillis = System.currentTimeMillis(); // If there's been no detections yet there's no cascade.
			return; // Log time and return
		}

		if (System.currentTimeMillis() - LastErrorMillis < MillisTollerance) { // Error is within time tollerance
			CascadeCount++; // Accept as cascade
		} else {
			CascadeCount = 1; // Else time frame has passed, reset cascade count (including current error)
		}

		if (CascadeCount > CascadeTollerance) { // If cascade tollerance is exceeded
			if (AllowEIS) { // Check EIS perms
				invokeEIS(); // Invoke EIS
			} else {

				JOptionPane.showMessageDialog(OPEXWindowHelper.getSwingParent(),
						"A cascade of errors has been detected, but OPEX has been instructed to not shutdown.");
				JOptionPane.showMessageDialog(OPEXWindowHelper.getSwingParent(),
						"It's recommended that you save and restart ASAP.");
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
		LoggerUtils.log("[@Warn] " + Thread.currentThread().getClass().getSimpleName() + " says " + s);
	}

	public static void invokeEIS() {
		if (!AllowEIS) {
			warn("Attempted to init EIS, but it's disabled. Rejected invocation.");
			return;
		}
		JOptionPane.showMessageDialog(OPEXWindowHelper.getSwingParent(),
				"OPEX Runtime is experiencing problems and is closing.");
		JOptionPane.showMessageDialog(OPEXWindowHelper.getSwingParent(), "User data will be saved.");
		LoggerUtils.crashDump();
		RuntimeHelper.shutdown(HaltCodes.EMS_CASCADE);
	}

	/**
	 * Clears all collected throwables from store.
	 */
	public static void clearEMS() {
		CollectedThrowables.clear();
	}

	private static void logEMS(String message){ logEMS(message, false);}
	private static void logEMS(String message, boolean Silent) {
		String log = "[EMS]";
		log += Silent? " [SILENT] " : " ";
		log += message;
		Console.internalLog(log);
	}

}
