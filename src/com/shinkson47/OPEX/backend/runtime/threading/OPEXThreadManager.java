package com.shinkson47.OPEX.backend.runtime.threading;

import java.util.ArrayList;
import java.util.List;

import com.shinkson47.OPEX.backend.errormanagement.EMSHelper;
import com.shinkson47.OPEX.backend.errormanagement.exceptions.OPEXNotImplementedException;
import com.shinkson47.OPEX.backend.errormanagement.exceptions.OPEXStaticException;
import com.shinkson47.OPEX.backend.errormanagement.exceptions.OPEXThreadPersistance;
import com.shinkson47.OPEX.backend.runtime.hooking.HookKey;
import com.shinkson47.OPEX.backend.runtime.hooking.OPEXHook;

/**
 * Main thread management tool for OPEX. Creates, executes, stores, manages and
 * disposes of threads.
 *
 * TODO Every aspect of this class has not yet been tested.
 *
 * @author gordie
 * @since V2
 * @see ThreadManagement
 */
public class OPEXThreadManager implements OPEXHook {

	/**
	 * Instantiator event hook use only.
	 *
	 * @param key - parameter for unlocking instantiation for event hooking. This is
	 *            trivial, but is an further attempt to enforce instantiation only
	 *            for in valid areas.
	 * @deprecated NOT FOR TYPICAL USE.
	 */
	@Deprecated
	public OPEXThreadManager(HookKey key) {
	}

	/**
	 * Hidden instantiator. This class is not instantiable.
	 */
	@SuppressWarnings("unused")
	private OPEXThreadManager() throws OPEXStaticException {
		throw new OPEXStaticException("OPEXThreadManager is not instantiable");
	}

	// Properties
	private static List<OPEXThread> threads = new ArrayList<OPEXThread>();
	private static long threadcount = 0;

	// Methods

	/**
	 * Returns a copy of the list of threads. This was intended for DevTools use
	 * only, threads should never be directly edited.
	 */

	public static List<OPEXThread> getAllThreads() {
		List<OPEXThread> copy = new ArrayList<>();
		copy.addAll(threads);
		return copy;
	}

	/**
	 * Create, stores and executes thread with the parameters given.
	 *
	 * @param runnable - the class to run
	 * @param Name     - custom identifyable name of the thread.
	 * @return null if a thread with the same name, or same runnable already
	 *         exsists.
	 * @return The OPEXThread container created.
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
	 * Thread ID's are sequential.
	 * 
	 * @return
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
	 * @return null if no threads match
	 * @return the OPEXThread container of a matching thread.
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
	 * @param Name - name of the thread
	 * @return null if no threads match
	 * @return the OPEXThread container of a matching thread.
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
	 * @return null if no threads match
	 * @return the OPEXThread container of a matching thread.
	 */
	public static OPEXThread getThread(Long ID) {
		for (OPEXThread thd : threads) {
			if (thd.getID() == ID) {
				return thd;
			}
		}
		return null;
	}

	/**
	 * Search for a thread using container
	 *
	 * @param container - the OPEXThread container of the thread
	 * @return null if no threads match
	 * @return the OPEXThread container of a matching thread.
	 * @deprecated This shit takes a container and returns a container lmao what the
	 *             fuuuuuuck.
	 */
	@Deprecated
	public static OPEXThread getThread(OPEXThread container) {
		for (OPEXThread thd : threads) {
			if (thd == container) {
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
	 * @see Thread Management, Disposing a thread, requesting a thread to close.
	 * @param thread
	 */
	public static void invokeThreadStop(OPEXThread thread) throws OPEXThreadPersistance {
		try {
			thread.getRunnable().stop();
		} catch (Exception e) {
			EMSHelper.handleException(
					new OPEXThreadPersistance(thread, "an exception was thrown in the thread's stop method."));
		}
	}

	/**
	 * Invokes OPEXThread.Stop() on the specified thread.
	 * Retrieves thread by name, using OPEXThreadManager.getThread(Name);
	 *
	 * @see invokeThreadStop(OPEXThread)
	 * @param OPEXStartSplash
	 * @throws OPEXThreadPersistance
	 */
	public static void invokeThreadStop(String OPEXStartSplash) throws OPEXThreadPersistance {
		invokeThreadStop(getThread(OPEXStartSplash));
	}

	/**
	 * Uses OPEXThreadManager.forceDisposeThread(OPEXThread) to kill a thread.
	 * Retrieves thread by name, using OPEXThreadManager.getThread(Name);
	 *
	 * @deprecated Thread killing deprecated by java since v1.2.
	 * @param name
	 */
	@Deprecated
	public static void forceDisposeThread(String name) {
		forceDisposeThread(getThread(name));
	}

	/**
	 * This method is more forceful in halting and removing threads.
	 *
	 *
	 * @param thread Thread container to close and remove.
	 * @deprecated Thread killing deprecated by java since v1.2.
	 * @see Thread.stop();
	 * @return false if failed to close, dispose and remove thread sucessfully.
	 * @return true if thread already does not exsist, or was successfully removed.
	 */
	@Deprecated
	public static boolean forceDisposeThread(OPEXThread thread) {
		if (getThread(thread) == null) {
			EMSHelper.warn("Attempted to kill a null thread. Ignoring Thread Dispose call.");
			return true;
		}
		//TODO create reference to thread, stop calling getThread
		try { invokeThreadStop(thread); } catch (OPEXThreadPersistance e) {}

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

	private static void update() {
		for (OPEXThread thread : threads) {
			if (!thread.getThread().isAlive()) {
				threads.remove(thread);
			}
		}
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
	 * When interrupted, a thread is paused again. There is no check for recurrsion.
	 * TODO
	 *
	 * @param thread the OPEXThread container to pause.
	 */
	public static void waitThread(OPEXThread thread) throws OPEXNotImplementedException {
		throw new OPEXNotImplementedException("WaitThread");
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
