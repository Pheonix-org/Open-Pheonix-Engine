package com.shinkson47.OPEX.backend.io.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.shinkson47.OPEX.backend.errormanagement.EMSHelper;

/**
 * Invokes keybinding's using the keylistener interface.
 * 
 * TODO only supports keyPressed.
 * 
 * @author gordie
 *
 */
public class OPEXKeyBindingSpec implements KeyListener {
	private List<OPEXKeyBind> binds = new ArrayList<OPEXKeyBind>();
	
	/**
	 * Adds a keybind to this spec instance.
	 */
	public void addBind(OPEXKeyBind toAdd) {
		binds.add(toAdd);
	}

	/**
	 * Checks if the keycode is bound.
	 * 
	 * @param KeyCode
	 * @return false, failed test (key already bound)
	 * @return true, Key is available to binding in this spec.
	 */
	private boolean checkBindAvailable(int KeyCode) {
		if (bindFor(KeyCode) != null) {
			EMSHelper.warn("Tried to register a bind for keyCode '" + KeyCode +"', but one already exsists in this spec. Binding rejected.");
			return true;
		}
		return false;
	}
	
	/**
	 * Adds a keybind to this spec instance.
	 * binding will be rejected with a warning if a the provided keycode is already bound.
	 * 
	 * @param KeyCode - keycode to match with keyevent's keycode.
	 * @param Method - method to invoke upon keypress.
	 * @param methodSuper - Object instance that parents the method.
	 * 
	 * @apiNote ensure the method super is of the correct parenting instance.
	 */
	public void addBind(int KeyCode, Method Method, Object methodSuper) {
		if (checkBindAvailable(KeyCode)) {
			return;
		}
		addBind(new OPEXKeyBind(KeyCode, Method, methodSuper));
	}
	
	/**
	 * Searches spec for a binding for the provided keycode.
	 * Used to invoke.
	 * 
	 * @param KeyCode - keycode to find binding for.
	 * @return null if there's no match
	 * @return OPEXKeyBind instance from this spec.
	 */
	public OPEXKeyBind bindFor(int KeyCode) {
		for (OPEXKeyBind bind : binds) {
			if (bind.keyCode == KeyCode) {
				return bind;
			}
		}
		return null;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		try {
			bindFor(e.getKeyCode()).Invoke(); //find binding for key, and invoke it.
		} catch (NullPointerException ex) {
			//No key bind, that's okay. Reject call.
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
