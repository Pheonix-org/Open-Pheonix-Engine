package backend.runtime.hooking;

/**
 * Container for a registered update hook, for the purpose of adding meta data for identifying the hook.
 * 
 * @author gordie
 *
 */
public class JGELRegisteredHook{
	public JGELHook Hook;
	public String Name;
	public int ID;
	
	public JGELRegisteredHook(JGELHook hook, String name, int id) {
		Hook = hook;
		Name = name;
		ID = id;
	}
	
	/**
	 * @return the iD
	 */
	public int getID() {
		return ID;
	}

	/**
	 * @return the hook
	 */
	public JGELHook getHook() {
		return Hook;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return Name;
	}
}
