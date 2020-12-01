package com.shinkson47.opex.backend.runtime.hooking;

import com.shinkson47.opex.backend.runtime.console.instruction.Instruction;
import com.shinkson47.opex.backend.runtime.console.instruction.instructions.INSTpool;
import com.shinkson47.opex.backend.runtime.errormanagement.EMSHelper;
import org.reflections.Reflections;

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
public abstract class AutoInvokable {

    public AutoInvokable(){
        AutoInvoke();
    }

    public static void InvokeAll() {
        Reflections reflections = new Reflections("");
        Set<Class<? extends AutoInvokable>> classes = reflections.getSubTypesOf(AutoInvokable.class);
        for (Class<? extends AutoInvokable> i : classes){
            try {
                i.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                EMSHelper.handleException(e);
            }
        }
    }

    public abstract void AutoInvoke();
}
