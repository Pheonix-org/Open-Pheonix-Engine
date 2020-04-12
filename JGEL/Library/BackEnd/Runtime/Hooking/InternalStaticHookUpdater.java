package BackEnd.Runtime.Hooking;

import BackEnd.Runtime.Threading.JGELThreadManager;

/**
 * This class acts as a wrapper between the instance based event update and static classes that wish to be updated.
 * 
 * Static classes using this MUST implement an instantiator with a hook key for updating.
 * @author gordie
 * TODO honestly not sure how i feel about this ngl
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
