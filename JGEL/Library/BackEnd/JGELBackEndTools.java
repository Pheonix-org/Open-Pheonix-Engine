package BackEnd;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import BackEnd.ErrorManagement.Exceptions.JGELGenericException;
import BackEnd.ErrorManagement.Exceptions.JGELStaticException;

public class JGELBackEndTools {

	private void JGELBeckEndTools() throws JGELStaticException {
		throw new JGELStaticException("Back End Tools is not instantiable!");
	}
	
	public static void GetAndInvokeMethod(Class<?> cls, String MethodName, Class<?> ParameterTypes, Object parameters, Object caller) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		InvokeMethod(cls.getMethod(MethodName,ParameterTypes), caller, parameters);
	}
	
	public static void InvokeMethod(Method mtd, Object caller, Object parameters) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException  {
			mtd.invoke(caller, parameters);
	}
	
}
