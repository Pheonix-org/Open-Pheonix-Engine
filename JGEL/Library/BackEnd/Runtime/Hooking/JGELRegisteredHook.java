package BackEnd.Runtime.Hooking;

/**
 * Container for a registered update hook, for the purpose of adding meta data for identifying the hook.
 * 
 * @author gordie
 *
 */
public class JGELRegisteredHook{

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

	/**
	 * @return the iD
	 */
	public int getID() {
		return ID;
	}

	public JGELHook Hook;
	public String Name;
	public int ID;
	
	public JGELRegisteredHook(JGELHook hook, String name, int id) {
		Hook = hook;
		Name = name;
		ID = id;
	}
	
}
