# [JGELDOCS] Thread manager

JGEL's thread manager starts, stores and monitors the execution of threads under the JGEL runtime environment.


# JGELThread

JGELThread is used throughout the entire of JGEL's thread management. It is a thread container for active threads, thier runnables, and identafyable metadata.

# JGELRunnalble

JGELRunnable is an extention of the Java Runnable interface, adding the stop request method to the thread to safely close itself rather than forcefully closing the thread externally. This interface is used throughout the entire thread manager class.

```Java
ForceDisposeThread(JGELThread)
```


# Create thread
JGEL can create and manage threads, and the use of the JGEL Thread manager is encouraged.

A Thread can be created with the API Call

```Java
CreateThread(JEGLThread)
```

Which creates and executes a thread with the JGELrunnable provided.

The runnable class will be invoked from the standard java threading interface runpoint

```Java
run(){...}
```

When creating a thread, its recommended that you follow the provided template in the API Example project, which implements a detection for thread interruption, and naturally kills the thread when this occours. This is inherently safer.

# Disposing of a thread

- Naturally killing a thread
Threads can be removed by returning from thier runnable class, or completing all work within the class. A JRE thread will stop running naturally in this case. When this occours, the dead thread will be automatically removed from the JGEL Thread manager.

- Requesting a thread to close
The stop method added by the JGELRunnable interface can be implemented with your threads to request the thread to stop work and allow itself to close naturally.

```Java
stop()
```

- Forcefully killing a thread
Threads can be forcefully killed using the API call 

```Java
ForceDisposeThread(JGELThread thread)
```

This will attempt to call the stop method and to intterupt the thread, before finally forcefully closing a thread using java's 

```Java
Thread.stop()
```

This method is depricated, and inherently unsafe, hency why the Force Close API Call attempts to close the thread by safer means first, before moving on to more fatal methods.

# Thread wait

The API Calls 

```Java
WaitAllThreads();

WaitThread(JGELThread);
```
invoke the Java Thread methods

```Java
Thread.Wait();
```

effecively pausing the thread untill notified or interrupted.

# Thread notify

The API Calls 

```Java
NotifyThread();

NotiftAllThreads();
```

Invoke the Thread.Notify method, effectivly unpausing waiting threads.

# Manager Update

The ThreadManager update function is a JGELEventHook update that is used for one main purpose;

When updated, the thread manager checks each thread for livelyhood, and removes dead threads.
