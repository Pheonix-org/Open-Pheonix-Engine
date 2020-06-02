package com.shinkson47.opex.backend.io.input;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.shinkson47.opex.backend.runtime.errormanagement.EMSHelper;

/*
 * Defines a single key bind,
 * binding a key to a method.
 * 
 * Keypress events do not support parsing arguments to methods.
 */
public class OPEXKeyBind {
	public int keyCode;
	public Method method; 
	public Object methodSuper;
	
	OPEXKeyBind(int KeyCode, Method Method, Object MethodSuper){
		keyCode = KeyCode;
		method = Method;
		methodSuper = MethodSuper; 
	}
	
	public void Invoke() {
		try {
			method.invoke(methodSuper, null);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			EMSHelper.handleException(e);
		}
	}
}
