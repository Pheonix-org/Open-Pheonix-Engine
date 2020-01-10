package com.Shinkson47.JGEL.BackEnd.Input;

import java.lang.reflect.Method;

import com.Shinkson47.JGEL.BackEnd.General.GeneralTools;
import com.Shinkson47.JGEL.BackEnd.Operation.ErrorManagement.ErrorManager;

/**
 * Stores and manages key bindings.
 * 
 * This class allows the user to bind methods to key events which will be activated by JGEL from any
 * window with the KeyboardHooker, such as the GameWindow.
 * 
 * All events bound must not have any arguments
 * 
 * extend and override the constructor to create defaults using Bind(##, xx());
 * 
 * @author gordie
 *
 */
public class KeyInputConfiguration {
	private Method[] PressBindings = new Method[255];
	private Method[] TypeBindings = new Method[255];
	private Method[] ReleaseBindings = new Method[255];
	
	/**
	 * Binds a key id to a method. 
	 * When this config is used, that method will be called on the KeyPress event. 
	 * 
	 * @param Keycode of the key binding.
	 * @param bindAction the method to be called upon bind trigger
	 */
	public void BindPress(int KeyCode, Method bindAction) {
		if (!GeneralTools.InRange(KeyCode, 0, 255)) {	//Test address of key id
			ErrorManager.Warn(6);						//warn and return if not valid.
			return;
		}
		Bind(PressBindings, bindAction, KeyCode);
	}
	
	/**
	 * Binds a key id to a method. 
	 * When this config is used, that method will be called on the KeyTyped event. 
	 * 
	 * @param Keycode of the key binding.
	 * @param bindAction the method to be called upon bind trigger
	 */
	public void BindType(int KeyCode, Method bindAction) {
		if (!GeneralTools.InRange(KeyCode, 0, 255)) {	//Test address of key id
			ErrorManager.Warn(6);						//warn and return if not valid.
			return;
		}
		Bind(TypeBindings, bindAction, KeyCode);
	}
	
	/**
	 * Binds a key id to a method. 
	 * When this config is used, that method will be called on the KeyTyped event. 
	 * 
	 * @param Keycode of the key binding.
	 * @param bindAction the method to be called upon bind trigger
	 */
	public void BindRelease(int KeyCode, Method bindAction) {
		if (!GeneralTools.InRange(KeyCode, 0, 255)) {	//Test address of key id
			ErrorManager.Warn(6);						//warn and return if not valid.
			return;
		}
		Bind(ReleaseBindings, bindAction, KeyCode);
	}
	
	private void Bind(Method[] Array, Method m, int keyCode) {
		Array[keyCode] = m;
	}

	//TODO there are keycodes above 255.
	public void Press(int keyCode) {
		if (PressBindings[keyCode] == null) return;	
		GeneralTools.InvokeMethod(PressBindings[keyCode], PressBindings[keyCode].getDeclaringClass());
	}
	
	public void Type(int keyCode) {
		if (TypeBindings[keyCode] == null) return;
		GeneralTools.InvokeMethod(TypeBindings[keyCode], TypeBindings[keyCode].getDeclaringClass());
	}
	
	public void Release(int keyCode) {
		if (ReleaseBindings[keyCode] == null) return;
		GeneralTools.InvokeMethod(ReleaseBindings[keyCode], ReleaseBindings[keyCode].getDeclaringClass());
	}
	

	
}
