package backend.runtime.threading;

import java.util.ArrayList;
import java.util.List;
import backend.errormanagement.EMSHelper;
import backend.errormanagement.exceptions.JGELNotImplementedException;
import backend.errormanagement.exceptions.JGELStaticException;
import backend.errormanagement.exceptions.JGELThreadPersistance;
import backend.runtime.hooking.HookKey;
import backend.runtime.hooking.JGELHook;

/**
 * Main thread management tool for JGEL.
 * Creates, executes, stores, manages and disposes of threads.
 * 
 * TODO Every aspect of this class has not yet been tested.
 * 
 * @author gordie
 * @since V2
 * @see ThreadManagement
 */
public class JGELThreadManager implements JGELHook{
	
	/**
	 * Instantiator event hook use only.
	 * 
	 * @param key - parameter for unlocking instantiation for event hooking.
	 * This is trivial, but is an further attempt to enforce instantiation only for in valid areas.
	 * @deprecated NOT FOR TYPICAL USE.
	 */
	public JGELThreadManager(HookKey key) {}

	/**
	 * Hidden instantiator.
	 * This class is not instantiable.
	 */
	@SuppressWarnings("unused")
	private JGELThreadManager() throws JGELStaticException {
		throw new JGELStaticException("JGELThreadManager is not instantiable");
	}

	//Properties
	private static List<JGELThread> threads = new ArrayList<JGELThread>();
	private static long threadcount = 0;
	
	//Methods
	
	
	/**
	 * Returns a copy of the list of threads.
	 * This was intended for DevTools use only, threads should never be directly edited.
	 */

	public static List<JGELThread> getAllThreads(){
		return List.copyOf(threads);
	}
	
	/**
	 * Create, stores and executes thread with the parameters given.
	 * 
	 * @param runnable - the class to run
	 * @param Name - custom identifyable name of the thread.
	 * @return null if a thread with the same name, or same runnable already exsists.
	 * @return The JGELThread container created.
	 */
	public static JGELThread createThread(JGELRunnable runnable, String Name){
		if (getThread(runnable) != null) {
			EMSHelper.warn("Tried to create a dublicate thread with a runnable that already exsists! Call will be ignored.");
			return null;
		}
		
		if (getThread(Name) != null) {
			EMSHelper.warn("Tried to create a thread with a thread name that's already in use! Call will be ignored.");
			return null;
		}
		
		JGELThread container = new JGELThread(new Thread(runnable), runnable, generateID(), Name);
		threads.add(container);
		threadcount++;
		
		container.getThread().start();
		
		return container;
	}
	
	/**
	 * Thread ID's are sequential.
	 * @return
	 */
	private static Long generateID() {
		return threadcount + 1;
	}
	
	/**
	 * Search for a thread using runnable
	 *
	 * TODO test for ambiguity in comparisions between different instances of the same runnable. Do different instances return the same thread?
	 * 
	 * @param Runnable - the runnable in use
	 * @return null if no threads match
	 * @return the JGELThread container of a matching thread.
	 */
	public static JGELThread getThread(JGELRunnable Runnable) {
		for (JGELThread thd : threads) {
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
	 * @return the JGELThread container of a matching thread.
	 */

	public static JGELThread getThread(String name) {
		for (JGELThread thd : threads) {
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
	 * @return the JGELThread container of a matching thread.
	 */
	public static JGELThread getThread(Long ID) {
		for (JGELThread thd : threads) {
			if (thd.getID() == ID) {
				return thd;
			}
		}
		return null;
	}
	
	/**
	 * Search for a thread using container
	 *
	 * @param container - the JGELThread container of the thread
	 * @return null if no threads match
	 * @return the JGELThread container of a matching thread.
	 * @deprecated This shit takes a container and returns a container lmao what the fuuuuuuck.
	 */
	public static JGELThread getThread(JGELThread container) {
		for (JGELThread thd : threads) {
			if (thd == container) {
				return thd;
			}
		}
		return null;
	}
	
	/**
	 * Invokes the JGELRunnable ThreadStop method on a thread.
	 * This method requests the thread to finish up and close itself. It is not forceful, and does not check for completion of a closure.
	 * 
	 * @see Thread Management, Disposing a thread, requesting a thread to close.
	 * @param thread
	 */
	public static void invokeThreadStop(JGELThread thread) throws JGELThreadPersistance {
		try {
			thread.getRunnable().stop();
		} catch (Exception e) {
			EMSHelper.handleException(new JGELThreadPersistance(thread, "an exception was thrown in the thread's stop method."));
		}
	}
	
	/**
	 * This method is more forceful in halting and removing threads.
	 * 
	 * 
	 * @param thread Thread container to close and remove.
	 * @deprecated by java since v1.2.
	 * @see Thread.stop();
	 * @return false if failed to close, dispose and remove thread sucessfully.
	 * @return true if thread already does not exsist, or was successfully removed.
	 */
	public static boolean forceDisposeThread(JGELThread thread){
		if (getThread(thread) == null) {
			EMSHelper.warn("Attempted to kill a null thread. Ignoring Thread Dispose call.");
			return true;
		}
		
		try {
			invokeThreadStop(thread);
		} catch (JGELThreadPersistance e) {}
	
		if (thread.getThread().isAlive()) {
			thread.getThread().interrupt();
		}
		
		if (thread.getThread().isAlive()) {
			thread.getThread().stop();
		}
		
		if (thread.getThread().isAlive()) {
			EMSHelper.handleException(new JGELThreadPersistance(thread, "Java failed to force close the thread."));
			return false;
		} else {
			threads.remove(thread);
			return true;
		}
	}

	private static void update() {
		for (JGELThread thread : threads) {
			if (!thread.getThread().isAlive()) {
				threads.remove(thread);
			}
		}
	}

	/**
	 * Calls Java's Thread.Wait() on all threads, effectively pausing all threads under the thread manager's controll until they're interrupted or notified.
	 * 
	 * 
	 */
	public static void waitAllThreads() throws JGELNotImplementedException {
		throw new JGELNotImplementedException("WaitAllThreads");
	}
	
	/**
	 * Uses Thread.Wait() to pause java threads.
	 * 
	 * When interrupted, a thread is paused again. There is no check for recurrsion. TODO
	 * 
	 * @param thread the JGELThread container to pause.
	 */
	public static void waitThread(JGELThread thread) throws JGELNotImplementedException  {
		throw new JGELNotImplementedException("WaitThread");
	}
	
	@Override
	public void updateEvent() {
		JGELThreadManager.update();
	}

	@Override
	public void exitUpdateEvent() {
		
	}	
	
	@Override
	public void enterUpdateEvent() {
	
	}

}
