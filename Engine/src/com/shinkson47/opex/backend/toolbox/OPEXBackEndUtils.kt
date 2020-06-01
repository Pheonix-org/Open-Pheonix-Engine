package com.shinkson47.opex.backend.toolbox

import com.shinkson47.opex.backend.errormanagement.EMSHelper
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.io.UnsupportedEncodingException
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * Container for general tool methods and functions
 * //TODO these should have thier own classes.
 */

@Throws(IllegalAccessException::class, IllegalArgumentException::class, InvocationTargetException::class, NoSuchMethodException::class, SecurityException::class)
fun getAndInvokeMethod(cls: Class<*>, MethodName: String?, ParameterTypes: Class<*>?, parameters: Any?, caller: Any?) {
    invokeMethod(cls.getMethod(MethodName, ParameterTypes), caller, parameters)
        }

@Throws(IllegalAccessException::class, IllegalArgumentException::class, InvocationTargetException::class)
fun invokeMethod(mtd: Method, caller: Any?, parameters: Any?) {
    mtd.invoke(caller, parameters)
}

fun ParseToSystemIn(data: String) {
    val old = System.`in`
    try {
        val testInput: InputStream = ByteArrayInputStream(data.toByteArray(charset("UTF-8")))
        System.setIn(testInput)
    } catch (e: UnsupportedEncodingException) {
        EMSHelper.handleException(e)
    } finally {
        System.setIn(old)
    }
}

