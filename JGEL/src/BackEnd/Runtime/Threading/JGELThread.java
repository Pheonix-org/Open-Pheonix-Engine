package BackEnd.Runtime.Threading;

public interface JGELThread extends Runnable{

	/**
	 * JGEL API request for the thread to finish and close itself.
	 */
	public abstract void stop();
	
}
