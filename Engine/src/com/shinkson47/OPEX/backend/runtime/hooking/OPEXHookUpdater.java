package com.shinkson47.OPEX.backend.runtime.hooking;

import com.shinkson47.OPEX.backend.errormanagement.EMSHelper;
import com.shinkson47.OPEX.backend.runtime.threading.IOPEXRunnable;

import java.util.ConcurrentModificationException;

/**
 * OPEXHookUpdater. Manager of OPEX Hooks.
 *
 * This class is responsible for dispatching update events to all registered
 * members.
 *
 * OPEXHookUpdater is static, but is mildly instantiable for executing in a
 * OPEXThread. It's used internally.
 *
 * @see [OPEXDOCS] OPEXHookUpdater.
 * @author gordie
 *
 */
public class OPEXHookUpdater implements IOPEXRunnable {
	private static EventHooker hookUpdater = new EventHooker();

	/*
	 * Determines if a OPEXHookUpdater has indicated itself to be currently running.
	 * Also used to keep the HookUpdater alive.
	 */
	private static Boolean IsRunning = false;

	public static void deregisterUpdateHook(String name) {
		hookUpdater.deregisterUpdateHook(name);
	}

	@Override
	public void run() {
		if (IsRunning) {
			EMSHelper.warn("OPEXHookUpdater was attempting to start, but it's already indicated to be running!");
			return;
		}
		IsRunning = true;
		while (IsRunning) {
			try {
				hookUpdater.triggerUpdate();
			} catch(ConcurrentModificationException e) {continue;}														//Hook list was modified during trigger, skip update and start again.

		}
	}

	@Override
	public void stop() {
		IsRunning = false;
		hookUpdater.clear();
	}

	public static void registerUpdateHook(OPEXHook hook, String name){
		hookUpdater.registerUpdateHook(hook, name);
	}
}
