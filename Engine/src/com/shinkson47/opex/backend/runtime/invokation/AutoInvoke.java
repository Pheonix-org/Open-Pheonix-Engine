package com.shinkson47.opex.backend.runtime.invokation;

import com.shinkson47.opex.backend.runtime.console.instruction.Instruction;
import com.shinkson47.opex.backend.runtime.console.instruction.instructions.INSTpool;
import com.shinkson47.opex.backend.runtime.errormanagement.EMSHelper;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Set;

/**
 * <h1>Abstract for a class that is automatically invoked upon boot.</h1>
 * <br>
 * <p>
 *
 * </p>
 *
 * @author <a href="https://www.shinkson47.in">Jordan T. Gray on 30/11/2020</a>
 * @version 1
 * @since v1
 */
public abstract class AutoInvoke {

    public AutoInvoke(){
        AutoInvoke();
    }


    /**
     * Finds all sub classes in the provided scope that extend the specified type.
     * @param scopePrefix Prefix that defines the search scope, i.e com.shinkson47.opex
     * @param type The class type that will be searched for.
     * @return All  subclasses of <b>type</b> in the <b>scope</b>
     */
    public static <T> Set<Class<? extends T>> findAllSubclasses(String scopePrefix, T type){
         return new Reflections(scopePrefix).getSubTypesOf((Class<T>) type.getClass());
    }
    
    public abstract void AutoInvoke();
}
