package com.shinkson47.opex.backend.runtime.hooking;

import com.shinkson47.opex.backend.runtime.errormanagement.EMSHelper;
import com.shinkson47.opex.backend.runtime.threading.IOPEXRunnable;

import java.util.ConcurrentModificationException;

/**
 * HookUpdater is an executable thread which uses an hooker to cause updates.
 *
 * This class is responsible for dispatching update events to all registered members inside of the hookUpdater.
 *
 * @see [OPEXDOCS] OPEXHookUpdater.
 * @author gordie
 */
public class HookUpdater implements IOPEXRunnable {
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
