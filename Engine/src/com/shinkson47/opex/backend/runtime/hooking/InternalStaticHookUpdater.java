
package com.shinkson47.opex.backend.runtime.hooking;

import com.shinkson47.opex.backend.runtime.threading.OPEXThreadManager;

/**
 * This class acts as a wrapper between the instance based event update and
 * static classes that wish to be updated.
 *
 * Static classes using this MUST have a seperate instantiator with a hook key.
 * 
 * @author gordie
 *
 */
public class InternalStaticHookUpdater implements OPEXHook {

	private HookKey Key = new HookKey();

	@Override
	public void enterUpdateEvent() {
		new OPEXThreadManager(Key).enterUpdateEvent();
	}

	@Override
	public void updateEvent() {
		new OPEXThreadManager(Key).updateEvent();
	}

	@Override
	public void exitUpdateEvent() {
		new OPEXThreadManager(Key).exitUpdateEvent();
	}

}
