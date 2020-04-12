# Known Issues

JGELThreadPersistance exception takes the name of Thread class, not runnable as is intended

Wrappers and docs for thread killing needs organising

thread stop method is not invoked

Thead stop method failing should not cause killing to fail

Thread's runnable is not readable. The developed GetThread method will not work.
There is no way to know what runnable is running on a thread -- which is the basis of how thread match detecection operates on the thread manager.

Thread manager needs rewriting.