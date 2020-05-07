
package backend.runtime.hooking;

import backend.runtime.threading.JGELThreadManager;

/**
 * This class acts as a wrapper between the instance based event update and
 * static classes that wish to be updated.
 *
 * Static classes using this MUST have a seperate instantiator with a hook key.
 * 
 * @author gordie
 *
 */
public class InternalStaticHookUpdater implements JGELHook {

	private HookKey Key = new HookKey();

	@Override
	public void enterUpdateEvent() {
		new JGELThreadManager(Key).enterUpdateEvent();
	}

	@Override
	public void updateEvent() {
		new JGELThreadManager(Key).updateEvent();
	}

	@Override
	public void exitUpdateEvent() {
		new JGELThreadManager(Key).exitUpdateEvent();
	}

}
