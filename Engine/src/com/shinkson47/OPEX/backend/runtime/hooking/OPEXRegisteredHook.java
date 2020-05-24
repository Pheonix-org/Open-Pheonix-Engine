package com.shinkson47.OPEX.backend.runtime.hooking;

/**
 * Container for a registered update hook, for the purpose of adding meta data
 * for identifying the hook.
 *
 * @author gordie
 *
 */
public class OPEXRegisteredHook {
	public OPEXHook Hook;
	public String Name;
	public int ID;

	public OPEXRegisteredHook(OPEXHook hook, String name, int id) {
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
	public OPEXHook getHook() {
		return Hook;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return Name;
	}
}
