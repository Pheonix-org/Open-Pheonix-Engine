package backend.runtime.threading;

/**
 * Container for a JRE thread and JGEL meta data for identification and external invokation by the API.
 * 
 * All threads controlled by the thread manager use this container.
 * 
 * @author gordie
 * @since V2
 * @see Thread Management
 */
public class JGELThread {
	private Thread thread;
	private JGELRunnable runnable;
	private Long ID;
	
	
	/**
	 * Instantiates a new thread meta container.
	 * 
	 * @param thd - The thread that is being used to execute the runnable.
	 * @param rnble - the EXACT INSTANCE of JGELRunnable class being executed
	 * @apiNote Calls to the runnable class from outside of the thread will not operate in the thread if the instance parsed is not the same instance as is in the thread.
	 * @param indentifyer - Automatically generated ID long.
	 * @param nm - User given name of the thread.
	 */
	public JGELThread(Thread thd, JGELRunnable rnble, Long indentifyer, String nm) {
		thread = thd;
		thread.setName(nm);
		runnable = rnble;
		ID = indentifyer;
	}
	
	/**
	 * @return the thread
	 */
	public Thread getThread() {
		return thread;
	}
	
	/**
	 * @return the runnable
	 */
	public JGELRunnable getRunnable() {
		return runnable;
	}
	
	/**
	 * @return the iD
	 */
	public Long getID() {
		return ID;
	}
}
