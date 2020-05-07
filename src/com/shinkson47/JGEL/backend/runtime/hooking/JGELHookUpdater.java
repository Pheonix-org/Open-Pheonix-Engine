package backend.runtime.hooking;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import backend.errormanagement.EMSHelper;
import backend.runtime.threading.JGELRunnable;

/**
 * JGELHookUpdater. Manager of JGEL Hooks.
 *
 * This class is responsible for dispatching update events to all registered
 * members.
 *
 * JGELHookUpdater is static, but is mildly instantiable for executing in a
 * JGELThread. It's used internally.
 *
 * @see [JGELDOCS] JGELHookUpdater.
 * @author gordie
 *
 */
public class JGELHookUpdater implements JGELRunnable  {
	private static EventHooker hookUpdater = new EventHooker();

	/*
	 * Determines if a JGELHookUpdater has indicated itself to be currently running.
	 * Also used to keep the HookUpdater alive.
	 */
	private static Boolean IsRunning = false;

	public static void deregisterUpdateHook(String name) {
		hookUpdater.deregisterUpdateHook(name);
	}

	@Override
	public void run() {
		if (IsRunning) {
			EMSHelper.warn("JGELHookUpdater was attempting to start, but it's already indicated to be running!");
			return;
		}
		IsRunning = true;
		while (IsRunning) {
			hookUpdater.triggerUpdate();
		}
	}

	@Override
	public void stop() {
		IsRunning = false;
		hookUpdater.clear();
	}

	public static void registerUpdateHook(JGELHook hook, String name){
		hookUpdater.registerUpdateHook(hook, name);
	}
}
