package backend.runtime.console.instructions;

import backend.runtime.console.JGELConsole;
import backend.runtime.console.JGELConsoleInstruction;
import backend.runtime.threading.JGELThread;
import backend.runtime.threading.JGELThreadManager;
/**
* Controls the thread manager
* 
* @author JGELConmmandBuilder
**/ 
public final class INSTThread implements JGELConsoleInstruction {
public enum ConsoleOptions {
list,
kill,
}

@Override
public void parse(){
switch (JGELConsole.getParamString("Switch to perform", ConsoleOptions.class)) {
case "list":
		for (JGELThread thread : JGELThreadManager.getAllThreads()) {
				System.out.println("[JGELConsole] " + "[" + thread.getID() + "] " + thread.getThread().getName());
			}
	
break;
case "kill":
	JGELConsole.Write("[JGELThread] Thread killed: " + JGELThreadManager.forceDisposeThread(JGELThreadManager.getThread(JGELConsole.getParamString("Name of thread to kill"))));
		
break;

default:
	JGELConsole.notifyUnknownSwitch();
	break;
}}
@Override
public String name(){
return "thread";
}
@Override
public String briefHelp() {
return "Controlls the thread manager";
}

@Override
public void help() {
JGELConsole.Write("Arguments:");
JGELConsole.Write("	list - lists all threads");
JGELConsole.Write("	kill - kills a thread by name");

}
}