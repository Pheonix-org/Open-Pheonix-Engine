# [HELPDOCS] Getting Started with JGEL
```
1. Create a JGEStartupScript class.

2. Create your java runpoint, 'main()'.
3. Import the hypervisor to your main.
4. Statically invoke Hypervisor.Start(new JGEStartupScript.)
```

From this point onwards, use of the main method is depricated. An ideal implementation of JGEL should have nothing besides this API Start Call.

# JGEL Startup Routine

Once invoked, the API call executes the following startup routine.

```
1.	Create temp logging data
2. 	Log startup with library version
3. 	assign startup script subroutine
4.	> Check for an already assigned state
5.  > Ensure there is a script to assign
6. 	> Assign the Hypervisor it's startup script.
7.	ReadyToRun subroutine
8.  > Check JGEL isn't already running
9.  > Ensure the hypervisor has been assigned a startup script
10. > Ensure the gamethread isn't already alive
11. execute the internal startup subroutine
12. > //Load and import resources
13. > Start Hook Upater Thread
14. Invoke client prestart subroutine
15. Log prestart completed
16. Invoke Client start
17. Invoke internal post start sub routine
18. > //
```
