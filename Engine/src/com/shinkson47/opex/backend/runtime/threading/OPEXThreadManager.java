package com.shinkson47.opex.backend.runtime.threading;

import java.util.ArrayList;
import java.util.List;

import com.shinkson47.opex.backend.runtime.errormanagement.EMSHelper;
import com.shinkson47.opex.backend.runtime.errormanagement.exceptions.OPEXNotImplementedException;
import com.shinkson47.opex.backend.runtime.errormanagement.exceptions.OPEXStaticException;
import com.shinkson47.opex.backend.runtime.errormanagement.exceptions.OPEXThreadPersistance;
import com.shinkson47.opex.backend.runtime.hooking.HookKey;
import com.shinkson47.opex.backend.runtime.hooking.OPEXHook;

/**
 * Main thread management tool for OPEX. Creates, executes, stores, manages and
 * disposes of threads.
 *
 * TODO Every aspect of this class has not yet been tested.
 *
 * @author gordie
 */
public class OPEXThreadManager implements OPEXHook {

	/**
	 * Hidden instantiator. This class is not instantiable.
	 */
	@SuppressWarnings("unused")
	private OPEXThreadManager() throws OPEXStaticException {
		throw new OPEXStaticException("OPEXThreadManager is not instantiable");
	}

	// Properties
	private static List<OPEXThread> threads = new ArrayList<>();
	private static long threadcount = 0;

	// Methods

	/**
	 * Returns a copy of the list of threads. This was intended for DevTools use
	 * only, threads should never be directly edited.
	 */
	public static List<OPEXThread> getAllThreads() {
		return new ArrayList<>(threads);
	}

	/**
	 * Create, stores and executes thread with the parameters given.
	 *
	 * @param runnable - the class to run
	 * @param Name     - custom identifyable name of the thread.
	 * @return The OPEXThread container created. null if a thread with the same name, or same runnable already
	 * exists.
	 */
	public static OPEXThread createThread(IOPEXRunnable runnable, String Name) {
		if (getThread(runnable) != null) {
			EMSHelper.warn(
					"Tried to create a dublicate thread with a runnable that already exsists! Call will be ignored.");
			return null;
		}

		if (getThread(Name) != null) {
			EMSHelper.warn("Tried to create a thread with a thread name that's already in use! Call will be ignored.");
			return null;
		}

		OPEXThread container = new OPEXThread(new Thread(runnable), runnable, generateID(), Name);
		threads.add(container);
		threadcount++;

		container.getThread().start();

		return container;
	}

	/**
	 * Gets the next sequential ID that should be used.
	 * 
	 * @return thread count + 1.
	 */
	private static Long generateID() {
		return threadcount + 1;
	}

	/**
	 * Search for a thread using runnable
	 *
	 * TODO test for ambiguity in comparisions between different instances of the
	 * same runnable. Do different instances return the same thread?
	 *
	 * @param Runnable - the runnable in use
	 * @return the OPEXThread container of a matching thread. null if no threads match
	 */
	public static OPEXThread getThread(IOPEXRunnable Runnable) {
		for (OPEXThread thd : threads) {
			if (thd.getRunnable() == Runnable) {
				return thd;
			}
		}
		return null;
	}

	/**
	 * Search for a thread using name
	 *
	 * @param name - name of the thread
	 * @return OPEXThread container of a matching thread. null if no threads match.
	 */

	public static OPEXThread getThread(String name) {
		for (OPEXThread thd : threads) {
			if (thd.getThread().getName().equals(name)) {
				return thd;
			}
		}
		return null;
	}

	/**
	 * Search for a thread using ID
	 *
	 * @param ID - The generated ID of the thread.
	 * @return OPEXThread container of a matching thread. null if no threads match
	 */
	public static OPEXThread getThread(Long ID) {
		for (OPEXThread thd : threads) {
			if (thd.getID().equals(ID)) {
				return thd;
			}
		}
		return null;
	}

	/**
	 * Invokes the OPEXRunnable ThreadStop method on a thread. This method requests
	 * the thread to finish up and close itself. It is not forceful, and does not
	 * check for completion of a closure.
	 *
	 * @param thread to invoke upon.
	 */
	public static void invokeThreadStop(OPEXThread thread) {
		try {
			thread.getRunnable().stop();
		} catch (Exception e) {
			EMSHelper.handleException(new OPEXThreadPersistance(thread, "an exception was thrown in the thread's stop method."));
		}
	}

	/**
	 * Invokes OPEXThread.Stop() on the specified thread.
	 * Retrieves thread by name, using OPEXThreadManager.getThread(Name);
	 * @param name of the name to kill.
	 * @throws OPEXThreadPersistance if the thread could not be killed.
	 */
	public static void invokeThreadStop(String name) throws OPEXThreadPersistance {
		invokeThreadStop(getThread(name));
	}

	/**
	 * Uses OPEXThreadManager.forceDisposeThread(OPEXThread) to kill a thread.
	 * Retrieves thread by name, using OPEXThreadManager.getThread(Name);
	 *
	 * @deprecated Thread killing deprecated by java since v1.2.
	 * @param name of the thread to dispose of.
	 */
	@Deprecated
	public static void forceDisposeThread(String name) {
		forceDisposeThread(getThread(name));
	}

	/**
	 * This method is more forceful in halting and removing threads.
	 *
	 * @param thread Thread container to close and remove.
	 * @deprecated Thread killing deprecated by java since v1.2.
	 * @return true if thread already does not exsist, or was successfully removed. false if failed to close, dispose and remove thread sucessfully.
	 */
	public static boolean forceDisposeThread(OPEXThread thread) {
		if (thread == null) {
			EMSHelper.warn("Attempted to kill a null thread. Ignoring Thread Dispose call.");
			return true;
		}
		//TODO create reference to thread, stop calling getThread
		invokeThreadStop(thread);

		if (thread.getThread().isAlive()) {
			thread.getThread().interrupt();
		}

		if (thread.getThread().isAlive()) {
			thread.getThread().stop();
		}

		if (thread.getThread().isAlive()) {
			EMSHelper.handleException(new OPEXThreadPersistance(thread, "Java failed to force close the thread."));
			return false;
		} else {
			threads.remove(thread);
			return true;
		}
	}

	//TODO this has no docs
	private static void update() {
		threads.removeIf(thread -> !thread.getThread().isAlive());
	}

	/**
	 * Calls Java's Thread.Wait() on all threads, effectively pausing all threads
	 * under the thread manager's controll until they're interrupted or notified.
	 *
	 *
	 */
	public static void waitAllThreads() throws OPEXNotImplementedException {
		throw new OPEXNotImplementedException("WaitAllThreads");
	}

	/**
	 * Uses Thread.Wait() to pause java threads.
	 *
	 * @param thread the OPEXThread container to pause.
	 * @throws InterruptedException if the thread wait is intterupted.
	 */
	public static void waitThread(OPEXThread thread) throws InterruptedException {
		thread.getThread().wait();
	}

	@Override
	public void updateEvent() {
		OPEXThreadManager.update();
	}

	@Override
	public void exitUpdateEvent() {

	}

	@Override
	public void enterUpdateEvent() {

	}

}
