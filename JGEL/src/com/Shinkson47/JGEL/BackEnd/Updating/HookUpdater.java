package com.Shinkson47.JGEL.BackEnd.Updating;

import java.util.ArrayList;
import java.util.List;

import com.Shinkson47.JGEL.BackEnd.Operation.ErrorManagement.ErrorManager;

public class HookUpdater {
	private static List<EventHook> Hooks = new ArrayList<EventHook>();	
	public static boolean DoAutoUpdate = false; 
	
	public static void UpdateAll() {
		for(EventHook ClassToUpdate : Hooks) {
			try {
				ClassToUpdate.UpdateEvent();
			} catch(Exception e){
				ErrorManager.Error(11);
			}
		}
	}
	
	public static void AutoUpdateAll() {
		DoAutoUpdate = true;
		while(DoAutoUpdate) {
			UpdateAll();
		}
	}

	public static void RegisterNewHook(EventHook Hook) {
		Hooks.add(Hook);
	}
}
