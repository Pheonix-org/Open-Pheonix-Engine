package com.shinkson47.opex.backend.runtime.environment;

import com.shinkson47.opex.backend.runtime.errormanagement.EMSHelper;
import com.shinkson47.opex.backend.toolbox.HaltCodes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Utility class for Java and OPEX runtime.
 *
 * @since 2020.7.11.A
 * @version 1.1
 * @author Jordan Gray
 */
public class RuntimeHelper {

	/**
	 * Begins actively shutting down the engine and runtime.
	 *
	 * @param code Runtime halt code.
	 */
	public static void shutdown(HaltCodes code){
		shutdown(code, "No halt message provided.");
	}

	/**
	 * Begins actively shutting down the engine and runtime.
	 *
	 * @param code Runtime halt code.
	 * @param Message [Optional overload] Halt message.
	 */
	@SuppressWarnings("")
	public static void shutdown(HaltCodes code, String Message){
		EMSHelper.warn("Engine is shutting down. [" + code.name() + "] " + Message);
		halt(code);
	}

	/**
	 * Preemptively halts the java runtime.
	 * No data is saved.
	 *
	 * @param code Runtime halt code
	 * @deprecated Inherently unsafe. Data is not saved, user is not warned.
	 * @see this.shutdown()
	 */
	@Deprecated()
	private static void halt(HaltCodes code) {
		Runtime.getRuntime().halt(code.ordinal());
	}

	/**
	 * Finds a matching method within a class, and invokes it.
	 *
	 * @param cls Class instance to search for method to invoke.
	 * @param MethodName Name of method to invoke
	 * @param ParameterTypes List of parameter types to match
	 * @param parameters Object of parameters to parse
	 * @param caller Object calling invocation
	 *
	 * @throws NoSuchMethodException if no matching method exists
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public static void invokeMethod(Class<?> cls, String MethodName, Class<?>[] ParameterTypes, Object parameters, Object caller) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		invokeMethod(cls.getMethod(MethodName, ParameterTypes), caller, parameters);
	}

	/**
	 * Finds a matching method within a class, and invokes it.
	 *
	 * @param parameters Object of parameters to parse
	 * @param caller Object calling invocation
	 * @param mtd method instance to invoke
	 *
	 * @throws NoSuchMethodException if no matching method exists
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public static void invokeMethod(Method mtd, Object caller, Object parameters) throws InvocationTargetException, IllegalAccessException {
		mtd.invoke(caller, parameters);
	}


}
