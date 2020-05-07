package backend.toolbox;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import backend.errormanagement.EMSHelper;
import backend.errormanagement.exceptions.JGELStaticException;

public class JGELBackEndUtils {

	private JGELBackEndUtils() throws JGELStaticException {
		throw new JGELStaticException("Back End Tools is not instantiable!");
	}

	public static void getAndInvokeMethod(Class<?> cls, String MethodName, Class<?> ParameterTypes, Object parameters,
			Object caller) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		invokeMethod(cls.getMethod(MethodName, ParameterTypes), caller, parameters);
	}

	public static void invokeMethod(Method mtd, Object caller, Object parameters)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		mtd.invoke(caller, parameters);
	}

	public static void ParseToSystemIn(String data){
		InputStream old = System.in;
		try {
			InputStream testInput = new ByteArrayInputStream( data.getBytes("UTF-8") );
			System.setIn(testInput);
		} catch (UnsupportedEncodingException e) {
			EMSHelper.handleException(e);
		} finally {
			System.setIn(old);
		}
	}

}
