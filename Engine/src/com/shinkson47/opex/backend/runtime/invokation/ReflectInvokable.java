package com.shinkson47.opex.backend.runtime.invokation;

import com.shinkson47.opex.backend.runtime.threading.OPEXDispatchableEvent;

/**
 * <h1></h1>
 * <br>
 * <p>
 *
 * </p>
 *
 * @author <a href="https://www.shinkson47.in">Jordan T. Gray on 09/12/2020</a>
 * @version 1
 * @since v1
 */
public abstract class ReflectInvokable extends OPEXDispatchableEvent {

    /**
     * @deprecated This is ambiguous with runnables. Use Boot hook, and another runnable.
     */
    @Override
    @Deprecated
    public void run() {
        invoke();
    }

    public abstract void invoke();

}

