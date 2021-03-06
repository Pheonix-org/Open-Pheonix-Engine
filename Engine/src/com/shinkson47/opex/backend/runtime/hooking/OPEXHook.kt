package com.shinkson47.opex.backend.runtime.hooking

/**
 * EventHooks are used to make OPEX update a class.
 *
 * Implement this interface, then add the instance to the HookUpdater with the
 * API Call RegisterUpdateHook(class, RegisterName);
 *
 * Event hooks are used in OPEX components such as OPEXWindows, and are intended
 * to be used for game update triggers, as such the client should not have an
 * update loop of any kind.
 *
 * @author gordie
 */
interface OPEXHook {
    /**
     * Called when a eventHook is first registered, before it is updated.
     */
    fun enterUpdateEvent()

    /**
     * Called as often as the hook updater can allow to simulate an update loop.
     */
    fun updateEvent()

    /**
     * Called when the eventHook is removed from the hook updater
     */
    fun exitUpdateEvent()

    companion object {
        /*
	 * A user friendly name that makes it easier for developers to find thier hooks
	 * by naming them.
	 */
        const val Name = "untitledHook"
    }
}