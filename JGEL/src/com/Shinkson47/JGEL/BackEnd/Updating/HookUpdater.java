package com.Shinkson47.JGEL.BackEnd.Updating;

import java.util.ArrayList;
import java.util.List;

import com.Shinkson47.JGEL.BackEnd.Operation.Diagnostics.Logger;
import com.Shinkson47.JGEL.BackEnd.Operation.ErrorManagement.ErrorManager;
import com.Shinkson47.JGEL.FrontEnd.Window.GameWindow;

public class HookUpdater implements Runnable {
	private static List<EventHook> Hooks = new ArrayList<EventHook>();	
	private static EventHook queuedHook = null;
	private static EventHook removeQueue = null;
	
	public static boolean DoAutoUpdate = false, DoAutoRestart = false; 
	
	private static void UpdateAll() {
		if (queuedHook != null) {
			Hooks.add(queuedHook);
			queuedHook = null;
		}
		
		for(EventHook ClassToUpdate : Hooks) {
			
			if (!DoAutoUpdate) return;
			if (removeQueue != null) {
				if (removeQueue.equals(ClassToUpdate)) {
					Hooks.remove(ClassToUpdate);
					continue;
				}
			}
			
			
			try {
				ClassToUpdate.UpdateEvent();
			} catch(Exception e){
				ErrorManager.Error(11, null);
			}
		}
	}
	
	/**
	 * Starts automatically updating all hooks.
	 * 
	 * This was designed to be ran when the updater was started in a seperate thread, as such it is now private.
	 */
	private static void Start() {
		DoAutoUpdate = true;
		while(DoAutoUpdate) {
			UpdateAll();
			//Logger.log(String.valueOf(System.nanoTime()));
		}
	}
	
	public static void RegisterNewHook(EventHook Hook) {
		//ErrorManager.Error(13, null); //Super stupid line for testing error management in development.
		
		int WaitCount = 0;
		while (queuedHook != null) {
			WaitCount++;
			if (WaitCount > 499) {
				ErrorManager.Error(13, null);
			}
		}
		
		queuedHook = Hook;
	}
	
	public static void Halt() {
		DoAutoUpdate = false;
		DoAutoRestart = false;
	}

	public static void DeRegister(EventHook gameWindow) {
		List<EventHook> HooksCopy = List.copyOf(Hooks); //Prevent Concurrent access clashing 
		for (EventHook TestHook : HooksCopy) {
			if (TestHook.equals(gameWindow)) {
				int WaitCount = 0;
				while (removeQueue != null) {
					WaitCount++;
					if (WaitCount > 499) {
						ErrorManager.Error(14, null);
					}
				}
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
		return Hooks.size();
	}

	/**
	 * This now is how the updater is ran. It is intended to be used from within a dedicated hook updating thread.
	 */
	@Override
	public void run() {
		Start();
	}
}
