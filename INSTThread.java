package 1;

import BackEnd.Runtime.Console.JGELConsoleInstruction;
import BackEnd.Runtime.Console.JGELConsole;
import pgk;
/**
* i wanna fuckin die
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

break;
case "kill":
idk
break;

}
}
@Override
public String Name(){
return "thread";
}
@Override
public String BriefHelp() {
return "i wanna fuckin die";
}

@Override
public void Help() {
JGELConsole.Write("Arguments:");
JGELConsole.Write("	list - ");
JGELConsole.Write("	kill - kills all");

}
}Console.Write("	list - ");

}
}