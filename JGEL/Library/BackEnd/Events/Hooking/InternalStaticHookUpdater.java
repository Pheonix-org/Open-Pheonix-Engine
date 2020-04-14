package BackEnd.Events.Hooking;

import BackEnd.Runtime.Threading.JGELThreadManager;

/**
 * This class acts as a wrapper between the instance based event update and static classes that wish to be updated.
 * 
 * Static classes using this MUST have a seperate instantiator with a hook key.
 * @author gordie
 *
 */
public class InternalStaticHookUpdater implements JGELHook {

	private HookKey Key = new HookKey();
	
	@Override
	public void EnterUpdateEvent() {
		new JGELThreadManager(Key).EnterUpdateEvent();	
	}

	@Override
	public void UpdateEvent() {
		new JGELThreadManager(Key).UpdateEvent();	
	}

	@Override
	public void ExitUpdateEvent() {
		new JGELThreadManager(Key).ExitUpdateEvent();
	}
	
}
