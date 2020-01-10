package com.Shinkson47.JGEL.BackEnd.Updating;

import java.util.ArrayList;
import java.util.List;

import com.Shinkson47.JGEL.BackEnd.General.GeneralTools;
import com.Shinkson47.JGEL.BackEnd.Operation.Diagnostics.Logger;
import com.Shinkson47.JGEL.BackEnd.Operation.ErrorManagement.ErrorManager;

public class HookUpdater implements Runnable {
	/**
	 * Store all current active hooks.
	 * 
	 * THIS LIST IS IN CONSTANT USE BY THE UPDATER THREAD, NEVER TRY TO ACCESS THIS DIRECTLY.
	 * DOING SO WILL CAUSE A CONCURRENTMODIFICATION EXCEPTION.
	 */
	private static List<EventHook> Hooks = new ArrayList<EventHook>();
	public static List<EventHook> ArchivedHooks = new ArrayList<EventHook>();
	
	private static EventHook queuedHook = null;
	private static EventHook removeQueue = null;
	private static int LastKnownSize = 0;
	
	public static boolean DoAutoUpdate = false, DoAutoRestart = false, inloop; 
	public static long LastLoopStart = 0, PeakTime;
	
	
	private static void UpdateAll() {
		inloop = true;
		if ((System.currentTimeMillis() - LastLoopStart) > PeakTime && LastLoopStart > 0) {PeakTime = (System.currentTimeMillis() - LastLoopStart);}
		
		LastLoopStart = System.currentTimeMillis();
		if (queuedHook != null) {
			Hooks.add(queuedHook);
			queuedHook.EnterUpdateEvent();
			queuedHook = null;
		}
		try { //Concurrent Modification errors
			for(EventHook ClassToUpdate : Hooks) {
				if (!DoAutoUpdate) return;
				if (removeQueue != null) {
					if (removeQueue.equals(ClassToUpdate)) {
						ClassToUpdate.ExitUpdateEvent();
						ArchivedHooks.add(ClassToUpdate);
						Hooks.remove(ClassToUpdate);
						removeQueue = null;
						continue;
					}
				}
							
				try { //All errors inside client updates
					ClassToUpdate.UpdateEvent();
				} catch(Exception e){
					ErrorManager.PreWarn("UpdateHook class of exception origin: " + ClassToUpdate.getClass().getSimpleName());
					ErrorManager.Error(11, e);
				}
			}
		} catch(Exception e) {
			ErrorManager.Warn(3);
		}
		
		LastKnownSize = Hooks.size();
		inloop = false;
	}
	
	/**
	 * Starts automatically updating all hooks.
	 * 
	 * This was designed to be ran when the updater was started in a seperate thread, as such it is now private.
	 */
	private static void Start() {
		DoAutoUpdate = true;
		while(DoAutoUpdate) {
			Logger.log("[@AutoRemove]"); //Utter bullshit.
			UpdateAll();
			//Logger.log(String.valueOf(System.nanoTime()));
		}
	}
	
	public static void RegisterNewHook(EventHook Hook) {
		//ErrorManager.Error(13, null); //Super stupid line for testing error management in development.
		int WaitCount = 0;
		GeneralTools.WaitForNull(queuedHook);		
		queuedHook = Hook;
	}
	
	public static void Halt() {
		DoAutoUpdate = false;
		DoAutoRestart = false;
	}
	
	public static void Restore(int index) {
		if (index < 0 || index > ArchivedHooks.size()) {
			//Warn
			return;
		}
		
		RegisterNewHook(ArchivedHooks.get(index));
		ArchivedHooks.remove(index);
	}

	public static void DeRegister(EventHook gameWindow) {
		List<EventHook> HooksCopy = Hooks.subList(0, Hooks.size() - 1); //Prevent Concurrent access clashing 
		for (EventHook TestHook : HooksCopy) {
			if (TestHook.equals(gameWindow)) {
				int WaitCount = 0;
				GeneralTools.WaitForNull(removeQueue);
				removeQueue = TestHook;
			}
		}
	}

	public static String getRegisterQueueName() {
		if (queuedHook == null) {
			return "Null";
		}
		return queuedHook.getClass().getSimpleName();
	}
	
	public static int getHookCount() {
		return LastKnownSize;
	}

	/**
	 * This now is how the updater is ran. It is intended to be used from within a dedicated hook updating thread.
	 */
	@Override
	public void run() {
		Start();
	}

	public static Object[] GetAllHooks() {	
		return Hooks.toArray();
	}

	public static boolean IsQFull() {
		return removeQueue != null;
	}

	/**
	 * Routes to Deregister(EventHook)
	 * 
	 * Gets hook to remove, then calls the typical deregister method.
	 * 
	 * @param index of item to remove
	 */
	public static void DeRegister(int index) {
		if (!GeneralTools.InRange(index, 0, LastKnownSize)) {
			ErrorManager.Warn(2);
			return;
		}
		
		if (HookUpdater.IsQFull()) {
			ErrorManager.Warn(0);
			return;
		}
		List<EventHook> HooksCopy = List.copyOf(Hooks); //Prevent Concurrent access clashing 
		DeRegister(HooksCopy.get(index));
	}

	public static String getRemoveQueueName() {
		if (removeQueue == null) {
			return "Nothing to remove";
		}
		return removeQueue.getClass().getSimpleName();
	}

	public static String getQueueName() {
		if (queuedHook == null) {
			return "Nothing to add";
		}
		return queuedHook.getClass().getSimpleName();
	}

	public static void DeRegisterStatic() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Uses the name of a class which implements the EventHook interface to deregister all instances.
	 * 
	 * Indended for removing static event hook classes.
	 * 
	 * @param simpleName of the static class. use this.class.getSimpleName();
	 */
	public static void DeRegisterStatic(String simpleName) {
		boolean deregged = false;
		for (EventHook hook : List.copyOf(Hooks)) {
			if (simpleName.equals(hook.getClass().getSimpleName())) {
				DeRegister(hook);
				deregged = true;
			}
		}
		if (!deregged) {
			ErrorManager.Warn(5);
		}
	}



	/**
	 * Waits for update loop to end before returning
	 * intended for making changes whilst outside of the update loop.
	 */
	public static void WaitForNextLoop() {
		while(inloop) {
		}
	}
	
	/**
	 * Waits for update loop to end before returning
	 * intended for making changes whilst outside of the update loop.
	 * 
	 * Skipping multiple loops can be useful for allowing a register or deregister to occour before continuing.
	 * The first loop skipped may already be past the point of registration handling, and such the next loop should be outwaited too.
	 * 
	 * @param i count of update loops to wait
	 */
	public static void WaitForNextLoop(int i) {
		for (int x = 0; x <= i; i++) {
			while(inloop) {
			}
		}
	}
}
