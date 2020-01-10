package com.Shinkson47.JGEL.BackEnd.General;

import java.lang.reflect.Method;

import com.Shinkson47.JGEL.BackEnd.Operation.ErrorManagement.ErrorManager;
import com.Shinkson47.JGEL.BackEnd.Updating.HookUpdater;

/**
 * 
 * @author gordie
 *
 */
public class GeneralTools {	
	/**
	 * Holds the calling thread untill the parameter is null.
	 * 
	 * Warns and returns after 1000 update loops, if the value is still not null.
	 * 
	 * @param NullableObject
	 */
	public static void WaitForNull(Object NullableObject) {
		int WaitCount = 0;
		while (NullableObject != null) {
			WaitCount++;
			HookUpdater.WaitForNextLoop();
			if (WaitCount > 1000) {
				ErrorManager.Warn(6);
			}
		}
	}

	public static void NOP() {

	}
	
	public static boolean InRange(int Value, int Min, int Max) {
		return (Value >= Min && Value <= Max);
	}
		
	/**
	 * Invokes the parsed method with no arguments.
	 * 
	 * @param m The method to invoke
	 * @throws Error 0: Generic exception
	 */
	public static void InvokeMethod(Method m, Object obj) {
		if (m == null) return;
		
		try {
			m.invoke(obj);
		} catch (Exception e) {
			ErrorManager.Error(0, e);
		}
	}
	
	public static Method getMethod(Class<?> cls, String name, Class<?>... parameterTypes) {
		try {
			return cls.getClass().getMethod(name, parameterTypes);
		} catch (NoSuchMethodException | SecurityException e) {
			ErrorManager.PreWarn("Class: " + cls.getSimpleName() + " , Method: " + name + " arg types: " + parameterTypes.toString());
			ErrorManager.Warn(9);
		}
		return null;
	}



}
