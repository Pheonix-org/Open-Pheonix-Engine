package BackEnd.Events.Hooking;

import java.util.ArrayList;
import java.util.List;

import BackEnd.ErrorManagement.JGELEMS;
import BackEnd.Runtime.Threading.JGELRunnable;

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
			JGELEMS.Warn("JGELHookUpdater was attempting to start, but it's already indicated to be running!");
			return;
		}
		
		IsRunning = true;
		Clear();
		StartUpdating();
	}

	private void StartUpdating() {
		while(IsRunning) {
			for (JGELRegisteredHook i: Hooks) {
				i.getHook().UpdateEvent();
			}
		}
	}

	/**
	 * Removes all hooks from memory, effecively unregistering them all.
	 */
	private void Clear() {
		Hooks.clear();
	}
	
	@Override
	public void stop() {
		IsRunning = false;
		Clear();
	}
	
	/**
	 * Add a new update hook to the HookUpdater's jurisdiction.
	 * 
	 * This will issue it a new ID, and cause it to be updated automatically.
	 * @return the issued ID.
	 */
	public static int RegisterUpdateHook(JGELHook hook, String name) {
		JGELRegisteredHook regHook = new JGELRegisteredHook(hook, name, GetUnusedId());
		return 0;
	}
	
	/**
	 * Remove an update hook. This will remove it from the HookUpdater's memory,
	 * release the ID, and stop the thread from being updated in the future.
	 * 
	 * If there's no matching hook, the call is ignored and a warning is issued.
	 */
	public static void DeRegisterUpdateHook(int i) {
		
	}
	
	/**
	 * Searches through all registered hooks to find the lowest next available ID.
	 * ID's are incremental indexes, but do not represent a position in a set or order they were registered.
	 * @return
	 */
	public static int GetUnusedId() {
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
	}
	
	/**
	 * Searches for a registered hook with the specified id.
	 * 
	 * @param id - The id to search for
	 * @return the hook registered with a matching id.
	 * @return null if there's no matches.
	 */
	public static JGELRegisteredHook GetHookByID(int id) {
		for (JGELRegisteredHook i : Hooks) {
			if (i.ID == id) {return i;}
		}
		
		return null;
	}
	
	public static int GetIDByName(String name) {
		return GetHookByName(name).ID;
	}
	
	
	

	
}
