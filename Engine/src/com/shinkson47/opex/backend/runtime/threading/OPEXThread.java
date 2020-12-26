package com.shinkson47.opex.backend.runtime.threading;

import com.shinkson47.opex.backend.resources.pools.SelfKeySupplier;
import com.shinkson47.opex.backend.runtime.errormanagement.EMSHelper;

/**
 * Container for a JRE thread and OPEX meta data for identification and external
 * invokation by the API.
 *
 * All threads controlled by the thread manager use this container.
 *
 * @author gordie
 * @since V2
 * @see Thread Management
 */
public class OPEXThread implements SelfKeySupplier<OPEXThread> {
	private Thread thread;
	private IOPEXRunnable runnable;
	private Long ID;

	@Deprecated
	public OPEXThread(Long id, String name){
		this(null, null, id, name);
	}

	/**
	 * Instantiates a new thread meta container.
	 *
	 * @param thd   - The thread that is being used to execute the runnable.
	 * @param rnble - the EXACT INSTANCE of OPEXRunnable class being executed
	 * @apiNote Calls to the runnable class from outside of the thread will not
	 *          operate in the thread if the instance parsed is not the same
	 *          instance as is in the thread.
	 * @param intensifier - Automatically generated ID long.
	 * @param nm          - User given name of the thread.
	 */
	public OPEXThread(Thread thd, IOPEXRunnable rnble, Long intensifier, String nm) {
		thread = thd;
		thread.setUncaughtExceptionHandler(EMSHelper.getHandler());
		thread.setName(nm);
		runnable = rnble;
		ID = intensifier;
	}


	protected void setRunnable(IOPEXRunnable runnable) {
		this.runnable = runnable;
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
	public IOPEXRunnable getRunnable() {
		return runnable;
	}

	/**
	 * @return the iD
	 */
	public Long getID() {
		return ID;
	}

	@Override
	public String SupplyKey() {
		return SupplyKey(this);
	}

	/**
	 * Uses a thread's name as a key.
	 *
	 * @param item
	 * @return A value that may be used as a Pool Key to represent that item.
	 */
	@Override
	public String SupplyKey(OPEXThread item) {
		return item.getThread().getName();
	}


}
