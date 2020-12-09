package com.shinkson47.opex.backend.runtime.threading;

import com.shinkson47.opex.frontend.rendering.DisplayMode;

/**
 * <h1>A non-persistant thread which performs an async task once dispatched</h1>
 * uses {@link IOPEXRunnable} to execute runnable tasks in an async thread.
 * <br>
 * Thread pool is located in {@link ThreadManager#getAsyncPool}.
 * <br>
 * Can by dispatched locally using {@link OPEXDispatchableEvent#dispatch()}.
 *
 * @author <a href="https://www.shinkson47.in">Jordan T. Gray on 09/12/2020</a>
 * @version 1
 * @since v1
 */
public abstract class OPEXDispatchableEvent implements IOPEXRunnable {

    /**
     * <h2>Dispathes this {@link OPEXDispatchableEvent} to the asyncPool queue.</h2>
     * When a pooled thread becomes available, the task in this event will be invoked.
     */
    public void dispatch() {
        Dispatch(this);
    }

    /**
     * <h2>Dispatches the provided thread.</h2>
     * @param dispatchableEvent The event to dispatch
     */
    public static void Dispatch(OPEXDispatchableEvent dispatchableEvent) {
        ThreadManager.DispatchEvent(dispatchableEvent);
    }
}
