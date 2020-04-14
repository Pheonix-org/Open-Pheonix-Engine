package backend.runtime.hooking;

import java.util.ArrayList;
import java.util.List;

import backend.errormanagement.EMSHelper;
import backend.runtime.threading.JGELRunnable;

/**
 * JGELHookUpdater. Manager of JGEL Hooks.
 *
 * This class is responsible for dispatching update events to all registered members.
 *
 * JGELHookUpdater is static, but is mildly instantiable for executing in a JGELThread. It's used internally.
 * 
 * @see [JGELDOCS] JGELHookUpdater.
 * @author gordie
 *
 */
public class JGELHookUpdater implements JGELRunnable{

	/*
	 * Determines if a JGELHookUpdater has indicated itself to be currently running.
	 * Also used to keep the HookUpdater alive.
	 */
	private static Boolean IsRunning = false;

	/*
	 * Store of all JGELRunnables managed by this hook updater.
	 */
	private static List<JGELRegisteredHook> Hooks = new ArrayList<JGELRegisteredHook>();
	
	@Override
	public void run() {
		if (IsRunning) {
			EMSHelper.warn("JGELHookUpdater was attempting to start, but it's already indicated to be running!");
			return;
		}
		
		IsRunning = true;
		clear();
		startUpdating();
	}

	private void startUpdating() {
		while(IsRunning) {
			for (JGELRegisteredHook i: Hooks) {
				i.getHook().updateEvent();
			}
		}
	}

	/**
	 * Removes all hooks from memory, effecively unregistering them all.
	 */
	private void clear() {
		Hooks.clear();
	}
	
	@Override
	public void stop() {
		IsRunning = false;
		clear();
	}
	
	/**
	 * Add a new update hook to the HookUpdater's jurisdiction.
	 * 
	 * This will issue it a new ID, and cause it to be updated automatically.
	 * @return the issued ID.
	 */
	public static int registerUpdateHook(JGELHook hook, String name) {
		JGELRegisteredHook regHook = new JGELRegisteredHook(hook, name, getUnusedId());
		Hooks.add(regHook);
		hook.enterUpdateEvent();
		return regHook.getID();
	}
	
	/**
	 * Remove an update hook. This will remove it from the HookUpdater's memory,
	 * release the ID, and stop the thread from being updated in the future.
	 * 
	 * If there's no matching hook, the call is ignored and a warning is issued.
	 */
	public static void deregisterUpdateHook(int i) {
		int index = 0;
		for (JGELRegisteredHook h : Hooks) {		
			if (h.getID() == i){
				Hooks.remove(index);
				return;
			}
			index++;
		}
		EMSHelper.warn("Could not deregister hook '" + i + "', ID is unused.");
	}
	
	/**
	 * Deregisters an update hook by name
	 * If there's no matching hook, the call is ignored and a warning is issued.
	 */
	public static void deregisterUpdateHook(String name) {
		int Index = getIndexByName(name);
		if (Index == -1) {
			EMSHelper.warn("Failed to remove hook, there's no hook with that name.");
		}
		
		deregisterUpdateHook(Index);
	}
	
	/**
	 * Searches through all registered hooks to find the lowest next available ID.
	 * ID's are incremental indexes, but do not represent a position in a set or order they were registered.
	 * @return
	 */
	public static int getUnusedId() {
		int NextID = 0;
		
		if (Hooks.size() == 0) {
			return NextID;
		}
		
		for (JGELRegisteredHook i : Hooks) {
			if (i.ID != NextID) {
				return NextID;
			} else {
				NextID++;
				continue;
			}
		}
		return 0;
	}
	
	public static JGELRegisteredHook GetHookByName(String name) {
		return null;

	
	public static int getIndexByName(String name) {
		int index = 0;
		for (JGELRegisteredHook i: Hooks) {
			if (i.getName() == name) {
				return index;
			}
			index++;
		}
		EMSHelper.warn("Could not find a registered hook named '" + name + "'. Returning an index of -1");
		return -1;
	}
	
	public static JGELRegisteredHook getHookByName(String name) {
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
	public static JGELRegisteredHook getHookByID(int id) {
		for (JGELRegisteredHook i : Hooks) {
			if (i.ID == id) {return i;}
		}
		return null;
	}
	
	public static int getIDByName(String name) {
		return getHookByName(name).ID;
	}
}
