package com.Shinkson47.JGEL.BackEnd.Updating;

/**
 * EventHooks are used to make JGEL update a class.
 * 
 * Implement this interface, then add the instance to the HookUpdater
 * using HookUpdater.RegisterNewHook();
 * 
 * Event hooks are used in JGEL components such as GameWindows,
 * and are intended to be used for game update triggers, as such the client should not have an update loop of any kind.
 * 
 * @author gordie
 *
 */
public interface EventHook {
	/**
	 * Called when a eventHook is registered, before it is updated.
	 */
	public void EnterUpdateEvent();
	
	/**
	 * Called as often as the hook updater can allow to simulate an update loop.
	 */
	public void UpdateEvent();
	
	/**
	 * Called when the eventHook is removed from the hook updater
	 */
	public void ExitUpdateEvent();
}
