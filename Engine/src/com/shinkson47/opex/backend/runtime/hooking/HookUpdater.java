package com.shinkson47.opex.backend.runtime.hooking;

import com.shinkson47.opex.backend.runtime.errormanagement.EMSHelper;
import com.shinkson47.opex.backend.runtime.errormanagement.exceptions.OPEXDisambiguationException;
import com.shinkson47.opex.backend.runtime.threading.IOPEXRunnable;
import com.shinkson47.opex.backend.runtime.threading.OPEXThreadManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;

/**
 * HookUpdater is an executable thread to cause updates to registered threads.
 *
 * This class is responsible for dispatching update events to all registered members, and managing hook registrations.
 * Upon instantiation, Hook Updater automatically starts execution in a thread.
 *
 *
 * @apiNote if all hooks are removed, the is thread automatically removed and this instance is disposed.
 * @see [OPEXDOCS] OPEXHookUpdater.
 * @author gordie
 */
public class HookUpdater implements IOPEXRunnable {

	/**
	 * Create a new hookUpdater thread with no hooks;
	 * For headed use.
	 *
	 * @throws OPEXDisambiguationException if an updater thread with the same name or runnable instance already exists.
	 */
	public HookUpdater(String Name) throws OPEXDisambiguationException {
		name = Name;
		start();
	}

	/**
	 * Create a new hookUpdater thread with a list of hooks;
	 *
	 * @apiNote Hooks registered this way will be given a generated name.
	 * @param hooks array of lists to instantiate with.
	 * @throws OPEXDisambiguationException if an updater thread with the same name or runnable instance already exists.
	 */
	public HookUpdater(OPEXHook[] hooks, String Name) throws OPEXDisambiguationException {
		this(Arrays.asList(hooks), Name);
	}

	/**
	 * Create a new hook updater thread, then register all hooks provided.
	 *
	 * @apiNote Hooks registered this way will have no name.
	 * @param hooks array list of hooks to add.
	 */
	public HookUpdater(List<OPEXHook> hooks, String name) throws OPEXDisambiguationException {
		this(name);
		for (OPEXHook newHook : hooks){
			registerUpdateHook(newHook, "");
		}
	}

	/**
	 * Used on instantiation to start the hook updater in a thread with the name provided.
	 */
	private void start() throws OPEXDisambiguationException {
		OPEXThreadManager.createThread(this, name);
	}

	/**
	 * Main threaded update loop.
	 */
	@Override
	public void run() {
		if (isRunning) {
			EMSHelper.warn("OPEXHookUpdater was attempting to start, but it's already indicated to be running!");
			return;
		}
		isRunning = true;
		while (isRunning) {
			try {
				triggerUpdate();
			} catch(ConcurrentModificationException e) {continue;}														//Hook list was modified during trigger, skip update and start again.
		}
	}

	/**
	 * Thread stop.
	 */
	@Override
	public void stop() {
		isRunning = false;
	}

	/**
	 * The name of this hook updater, as specified by the client on instantiation
	 */
	private String name = "";

	/**
	 * Used do determine if this updater is running.
	 */
	private boolean isRunning = false;

	/**
	 * Store of all hooks managed by this updater.
	 */
	public List<OPEXRegisteredHook> Hooks = new ArrayList<OPEXRegisteredHook>();

	/**
	 * Main update method, triggers a call of OPEXHook.updateEvent() in all hooks registered.
	 *
	 * @Throws ConcurrentModificationException if registered hooks list was modified during method call.
	 */
	public void triggerUpdate() throws ConcurrentModificationException {
		for (OPEXRegisteredHook i : Hooks)																				//For all hooks,
			try {
				i.getHook().updateEvent();                                                                              //Trigger Update.
			} catch(Exception e) {																						//Handle generic uncaught exceptions thrown inside hooks.
				EMSHelper.handleException(e);
			}
	}

	/**
	 * Add a new update hook to the HookUpdater's jurisdiction.
	 * This will issue it a new ID, and cause it to be updated automatically.
	 *
	 * @return the issued ID.
	 */
	public int registerUpdateHook(OPEXHook hook, String name) {
		OPEXRegisteredHook regHook = new OPEXRegisteredHook(hook, name, getUnusedId());
		Hooks.add(regHook);
		hook.enterUpdateEvent();
		return regHook.getID();
	}

	/**
	 * Remove an update hook by ID This will remove it from the HookUpdater's memory,
	 * release the ID, and stop the thread from being updated in the future.
	 *
	 * If there's no matching hook, the call is ignored.
	 */
	public void deregisterUpdateHook(int id) {
		removeHook(getIndexByID(id));
	}


	/**
	 * Deregisters an update hook by name If there's no matching hook, the call is
	 * ignored and a warning is issued.
	 */
	public void deregisterUpdateHook(String name) {
		int Index = getIndexByName(name);
		if (Index == -1)
			EMSHelper.warn("Failed to remove hook, there's no hook with that name.");
		else
			removeHook(Index);
	}

	/**
	 * Finds the index of a hook in the array list by name.
	 * @param name to locate
	 * @return index of hook, -1 if no matches or name is "".
	 */
	public int getIndexByName(String name) {
		if (name.equals("")) return -1;
		OPEXRegisteredHook hook = getHookByName(name);
		if (hook != null)
			return getIndexByID(hook.ID);

		EMSHelper.warn("Could not find a registered hook named '" + name + "'. Returning an index of -1");
		return -1;
	}

	/**
	 * Finds the index of a hook in the array list by ID.
	 * @param id to locate.
	 * @return index within Hooks  array list, -1 if no hook is found.
	 */
	public int getIndexByID(int id) {
		int index = 0;
		for (OPEXRegisteredHook hook : Hooks) {
			if (hook.ID == id)
				return index;
			index++;
		}
		return -1;
	}

	/**
	 *
	 * @param name
	 * @return
	 */
	public OPEXRegisteredHook getHookByName(String name) {
		int index = getIndexByName(name);
		if (index == -1) {
			EMSHelper.warn("Could not find a registered hook named '" + name + "'. Returning null.");
			return null;
		}
		return Hooks.get(index);
	}

	/**
	 * Searches for a registered hook with the specified id.
	 *
	 * @param id - The id to search for
	 * @return the hook registered with a matching id.
	 * @return null if there's no matches.
	 */
	public OPEXRegisteredHook getHookByID(int id) {
		return Hooks.get(getIndexByID(id));
	}


	/**
	 * Used by deregister to remove hooks from list at the specified index.
	 * @param index
	 */
	private void removeHook(int index){
		Hooks.get(index).getHook().exitUpdateEvent();
		Hooks.remove(index);
		checkEmpty();
	}


	/**
	 * Deregisters every hook in this updater.
	 */
	public void clear() {
		for (int index = 0; index <= Hooks.size() -1; index++){
			removeHook(index);
		}
	}


	/**
	 * Searches through all registered hooks to find the lowest next available ID.
	 * ID's are incremental indexes, but do not represent a position in a set or
	 * order they were registered.
	 *
	 * @return
	 */
	public int getUnusedId() {
		int NextID = 0;

		if (Hooks.size() == 0) {
			return NextID;
		}

		for (OPEXRegisteredHook i : Hooks) {
			if (i.ID != NextID) {
				return NextID;
			} else {
				NextID++;
				continue;
			}
		}
		return 0;
	}


	/**
	 * If Hooks.size() == 0, updater is stopped and disposed.
	 */
	private void checkEmpty() {
		this.stop();
	}
}
