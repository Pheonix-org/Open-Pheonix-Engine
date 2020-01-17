package BackEnd.Runtime.Threading;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import BackEnd.ErrorManagement.JGELEMS;
import BackEnd.ErrorManagement.Exceptions.JGELThreadAlive;
import BackEnd.ErrorManagement.Exceptions.JGELThreadPersistance;

public class JGELThreadManager {

	//thread store
	private static List<Thread> Threads = new ArrayList<Thread>();

	
	
	
	
	
	/**
	 * Creates, starts and stores a new thread with the name and runnable params
	 * 
	 * @see Thread Management, Create thread
	 * 

	 * @warns if a thread with that runnable already exists
	 * @apiNote Runnable's are compared by simplified class name.
	 * 
	 * @apiNote Concurrent instances of the same target is disallowed using this thread manager.
	 * For implementations of multithreaded runnables (Such as multi thread loading classes) a custom solution
	 * is required for your purpose. 
	 * 
	 * Ensure to always use JGEL's unhandled exception handler in your custom threads.
	 * 
	 * @see Error management, Unhandled Exception Handler
	 * 
	 * @throws NullPointerException if there is no target to run.
	 * 
	 * @param target - the runnable class to execute in the thread.
	 */
	public static void CreateThread(JGELThread target) throws NullPointerException {
		if (GetThread(target) != null){
			JGELEMS.Warn("Thread could not be created because a thread with that name already exsists");
		}
		
		if (target == null) {
			throw new NullPointerException("Thread could not be created because there was no target to run");
		}
		
		Thread thd = new Thread(target, target.getClass().getSimpleName());
		thd.setUncaughtExceptionHandler(JGELEMS.GetHandler());
		Threads.add(thd);	
		thd.start();
	}
	

	/**
	 * Search for thread running the provided target.
	 * 
	 * @apiNote Targets are compared by simplified class name.
	 * 
	 * @param target - runnable to search for
	 * @return The thread running the provided target class.
	 * @return null if there is no match
	 * @warns if there is no such thread.
	 */
	public static Thread GetThread(JGELThread target) {
		for (Thread thd : Threads) {
			if (thd.getName() == target.getClass().getSimpleName()){
				return thd;
			}
		}	
		return null;
	}
	
	/**
	 * Invokes Thread.Wait() on a specific thead.
	 * Thread is collected using GetThread()
	 * 
	 * @see GetThread()
	 * @param target - runnable being executed by the thread to wait.
	 */
	public static void WaitThread(JGELThread target){
		try {
			GetThread(target).wait();
		} catch (Exception e) {
			JGELEMS.Warn("A manually paused thread was interrupted.");
		}
	}
	
	/**
	 * Invokes the Thread.Wait() method on all threads under the Thread Manager's control.
	 * 
	 * @throws warn if Thread.Wait() was interrupted
	 */
	public static void WaitAll() {
		try {
			for (Thread thd : Threads) {
				thd.wait();
			}
		} catch (Exception e) {
			JGELEMS.Warn("A paused thread was interrupted.");
		}
	}
	
	/**
	 * Invokes the Thread.Notify() method on all threads under the Thread Manager's control
	 */
	public static void NotifyAll() {
			for (Thread thd : Threads) {
				thd.notify();
			}
	}
	
	/**
	 * Invoke Thread.Notify() on a specific thread.
	 * 
	 * Thread is collected using GetThread().
	 * @see NotifyThread
	 * @param target - runnable being executed by the thread to wait.
	 */
	public static void NotifyThread(JGELThread target) {
		GetThread(target).notify();
	}
	
	/**
	 * Disposes of an existing but dead thread.
	 * 
	 * @deprecated since APIV2.
	 * 
	 * @apiNote The JGEL Thread Manager will automatically remove dead threads when
	 * updated by the Hook thread. This method is only realistically
	 * @see Update Hook
	 * 
	 * @throws JGELThreadAlive if the matching thread is still alive.
	 */
	public static void DisposeThread(Thread target) throws JGELThreadAlive {
		Threads.remove(target);
		target = null;
	}
	
	/**
	 * Wrapper for DisposeThread(Thread target)
	 * 
	 * @deprecated
	 * @see DisposeThread(Thread target)
	 */
	public static void DisposeThread(JGELThread target) throws JGELThreadAlive {
		Thread thd = GetThread(target);
		
		if (thd.isAlive()) {
			throw new JGELThreadAlive(target);
		}

		DisposeThread(thd);
	}
	
	
	
	/**
	 * 
	 * 
	 * @apiNote This call uses Thread.stop() to kill a thread, if it's still running.
	 * Halting threads in this manor was deprecated in Java 1.2, and is marked as inherently unsafe.
	 * 
	 * Threads should close naturally, or already be dead to be disposed of.
	 * 
	 * @param target - runnable of the thread to kill.
	 * @throws JGELThreadPersistance - Thread.stop failed to kill the thread
	 * @throws JGELThreadAlive - Should not be thrown. Would only occour here if the thread started again as it was being dumped.
	 */
	public static void ForceDisposeThread(JGELThread target) throws JGELThreadPersistance, JGELThreadAlive {
		ForceDispose(target);	
	}
	
	/**
	 * 
	 * 
	 * @apiNote This call uses Thread.stop() to kill a thread, if it's still running.
	 * Halting threads in this manor was deprecated in Java 1.2, and is marked as inherently unsafe.
	 * 
	 * Threads should close naturally, or already be dead to be disposed of.
	 * 
	 * @param target - target thread to kill
	 * @throws JGELThreadPersistance - Thread.stop failed to kill the thread
	 * @throws JGELThreadAlive - Should not be thrown. Would only occour here if the thread started again as it was being dumped.
	 */
	@SuppressWarnings("deprecation")
	public static void ForceDispose(Thread target) throws JGELThreadAlive, JGELThreadPersistance {
		if (target.isAlive()) {
			Class<JGELThread> threadclass = (Class<JGELThread>) target.getClass();
			try {
				BackEnd.JGELBackEndTools.GetAndInvokeMethod(threadclass, "stop", null, null, null);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				JGELEMS.Warn("Failed to invoke thread stop request on method. Thread will be forcefully closed.");
			}
			target.stop();
		}
		
		if (target.isAlive()) {
			throw new JGELThreadPersistance(target);
		}
		
		DisposeThread(target);
	}
	
	/**
	 * Wrapper for ForceDispose(Thread target)
	 * 
	 * @see ForceDispose(Thread target)
	 */
	public static void ForceDispose(JGELThread thd) throws JGELThreadAlive, JGELThreadPersistance {
		ForceDispose(GetThread(thd));
	}
	
	/**
	 * Attempts to forcefully kill and remove all
	 * 
	 * @throws JGELThreadAlive
	 * @throws JGELThreadPersistance
	 * 
	 * @deprecated by Java API since 1.2
	 * @see Thread.stop().
	 * 
	 * @apiNote this method is intended to preemptively close threads, which is inherintly
	 * unsafe. Threads should be removed  
	 */
	public static void ForceDisposeAll() throws JGELThreadAlive, JGELThreadPersistance {
		for (Thread thd : Threads) {
			ForceDispose(thd);
		}
	}
}
