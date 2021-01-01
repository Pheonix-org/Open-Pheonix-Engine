package com.shinkson47.opex.backend.runtime.hooking;

import com.shinkson47.opex.backend.runtime.invokation.ReflectInvokable;

/**
 * <h1>{@link ReflectInvokable} that will be found and invoked when a new config is loaded</h1>
 * @author <a href="https://www.shinkson47.in">Jordan T. Gray on 26/12/2020</a>
 * @version 1
 * @since v1
 */
public abstract class ConfigHook extends ReflectInvokable {

    @Override
    public void invoke() {
        ConfigHook();
    }

    public abstract void ConfigHook();
}

