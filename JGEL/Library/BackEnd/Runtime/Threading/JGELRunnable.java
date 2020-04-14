package backend.runtime.threading;

public interface JGELRunnable extends Runnable{
	/**
	 * JGEL API request for the thread to finish and close itself.
	 */
	public abstract void stop();
	
}
