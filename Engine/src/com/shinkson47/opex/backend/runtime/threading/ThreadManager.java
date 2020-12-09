package com.shinkson47.opex.backend.runtime.threading;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.concurrent.*;

import com.shinkson47.opex.backend.resources.pools.Pool;
import com.shinkson47.opex.backend.runtime.environment.OPEX;
import com.shinkson47.opex.backend.runtime.errormanagement.EMSHelper;
import com.shinkson47.opex.backend.runtime.errormanagement.exceptions.OPEXDisambiguationException;
import com.shinkson47.opex.backend.runtime.errormanagement.exceptions.OPEXThreadPersistance;
import com.shinkson47.opex.backend.runtime.hooking.OPEXBootHook;
import com.shinkson47.opex.backend.runtime.hooking.OPEXHook;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * <h1>Main thread management helper for OPEX.</h1>
 * Responsible for creating, executing, storing and
 * disposing of sync and async {@link OPEXThread}'s and {@link OPEXDispatchableEvent}'s.
 *
 * @author gordie
 * @version 1.2 since V9.12.2020.A - Implements async threaded events.
 */
public class ThreadManager extends OPEXBootHook implements OPEXHook {

	/**
	 * <h1>This class is static.</h1>
	 *
	 * @deprecated This instantiator is only intended for registering OPEXHooks.
	 */
	@Deprecated
	public ThreadManager() {}

	// Properties
	/**
	 * <h2>A Pool containing background {@link OPEXThread}s</h2>
	 */
	private static final Pool<OPEXThread> persistentThreads = new Pool<>("Threads");

	/**
	 * <h2>A thread pool containing async {@link OPEXDispatchableEvent}s.</h2>
	 */
	private static final ThreadPoolExecutor asyncPool = new ThreadPoolExecutor(
			10, 20, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
	static { asyncPool.prestartAllCoreThreads(); }


	// Methods

	/**
	 * Returns a copy of the list of threads. This was intended for DevTools use
	 * only, threads should never be directly edited.
	 */
	public synchronized static ArrayList<OPEXThread> getAllThreads() {
		return persistentThreads.valuesAsArrayList();
	}

	/**
	 * Create, stores and executes thread with the parameters given.
	 *
	 * @param runnable - the class to run
	 * @param Name     - custom identifyable name of the thread.
	 * @return The OPEXThread container created. null if a thread with the same name, or same runnable already
	 * exists.
	 */
	public synchronized static OPEXThread createThread(IOPEXRunnable runnable, String Name) throws OPEXDisambiguationException {
		if (getThread(runnable) != null)
			throw new OPEXDisambiguationException("Tried to create a duplicate thread with a runnable that already exists!");


		if (getThread(Name) != null)
			throw new OPEXDisambiguationException("Tried to create a duplicate thread with a name that's already in use!");

		OPEXThread container = new OPEXThread(new Thread(runnable), runnable, generateID(), Name);
		persistentThreads.put(container);
		container.getThread().start();
		return container;
	}

	/**
	 * Gets the next sequential ID that should be used.
	 * 
	 * @return thread count + 1.
	 */
	private static Long generateID() {
		return persistentThreads.size() + 1L;
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
	public synchronized static OPEXThread getThread(IOPEXRunnable Runnable) {
		for (OPEXThread thd : persistentThreads.values()) {
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

	public synchronized static OPEXThread getThread(String name) {
		for (OPEXThread thd : persistentThreads.values()) {
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
		for (OPEXThread thd : persistentThreads.values()) {
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
			persistentThreads.remove(thread.SupplyKey());
			return true;
		}
	}

	//TODO this has no docs
	private static void update() {
		try {
			for (OPEXThread thd : persistentThreads.valuesAsArrayList())
				if (thd.getThread() != null && !thd.getThread().isAlive())
					persistentThreads.remove(thd.SupplyKey());

		} catch (ConcurrentModificationException e) { 																	// Threads list was modified during this update, skip.
			EMSHelper.handleException(e, true);																			// Handle silently, not fatal.
		}
	}

	/**
	 * Calls Java's Thread.Wait() on all threads, effectively pausing all threads
	 * under the thread manager's control until they're interrupted or notified.
	 */
	public static void waitAllThreads() throws NotImplementedException {
		throw new NotImplementedException();
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

	/**
	 * <h2>Dispatches the provided {@link OPEXDispatchableEvent} to the asyncPool queue.</h2>
	 * The event will be invoked when a pooled thread becomes available.
	 * @param dispatchableEvent to dispatch.
	 */
	public synchronized static void DispatchEvent(OPEXDispatchableEvent dispatchableEvent) {
		asyncPool.submit(dispatchableEvent);
	}

	/**
	 * @return The working instance of {@link ThreadManager#asyncPool}
	 * @deprecated Async pool should not be modified externally.
	 */
	@Deprecated
	public static ThreadPoolExecutor getAsyncPool() {
		return asyncPool;
	}

	@Override
	public void updateEvent() {
		ThreadManager.update();
	}

	@Override
	public void exitUpdateEvent() {

	}

	@Override
	public void enterUpdateEvent() {

	}

	/**
	 * OPEX API request for the thread to finish and close itself.
	 */
	@Override
	public void stop() {

	}

	/**
	 * Registers self as a hook on boot.
	 * @see Thread#run()
	 */
	@Override
	public void run() {
		OPEX.getHookUpdater().registerUpdateHook(new ThreadManager(), "OPEXThreadManager");
	}
}
