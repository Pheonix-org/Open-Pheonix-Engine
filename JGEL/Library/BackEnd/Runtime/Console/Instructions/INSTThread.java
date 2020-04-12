package BackEnd.Runtime.Console.Instructions;

import BackEnd.Runtime.Console.JGELConsole;
import BackEnd.Runtime.Console.JGELConsoleInstruction;
import BackEnd.Runtime.Threading.JGELThread;
import BackEnd.Runtime.Threading.JGELThreadManager;
/**
* Controlls the thread manager
* 
* @author JGELConmmandBuilder
**/ 
public final class INSTThread implements JGELConsoleInstruction {
public enum ConsoleOptions {
list,
kill,
}

@Override
public void Parse(){
switch (JGELConsole.GetParamString("Switch to perform", ConsoleOptions.class)) {
case "list":
		for (JGELThread thread : JGELThreadManager.GetAllThreads()) {
				System.out.println("[JGELConsole] " + "[" + thread.getID() + "] " + thread.getThread().getName());
			}
	
break;case "kill":
	JGELConsole.Write("[JGELThread] Thread killed: " + JGELThreadManager.ForceDisposeThread(JGELThreadManager.GetThread(JGELConsole.GetParamString("Name of thread to kill"))));
		
break;
}
}
@Override
public String Name(){
return "thread";
}
@Override
public String BriefHelp() {
return "Controlls the thread manager";
}

@Override
public void Help() {
JGELConsole.Write("Arguments:");
JGELConsole.Write("	list - lists all threads");
JGELConsole.Write("	kill - kills a thread by name");

}
}