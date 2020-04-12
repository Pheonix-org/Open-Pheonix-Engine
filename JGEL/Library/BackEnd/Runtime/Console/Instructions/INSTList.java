package BackEnd.Runtime.Console.Instructions;

import BackEnd.Runtime.Console.JGELConsole;
import BackEnd.Runtime.Console.JGELConsoleInstruction;

public class INSTList implements JGELConsoleInstruction {

	@Override
	public void Parse() {
		for (JGELConsoleInstruction inst : JGELConsole.getInstructions()) {
			JGELConsole.Write(inst.Name() + ": " + inst.BriefHelp());
		}
	}

	@Override
	public String Name() {
		return "list";
	}

	@Override
	public void Help() {
		JGELConsole.Write("Instructions visible are only those registered within the console using JGELConsole.AddInstruction()");
	}

	@Override
	public String BriefHelp() {
		return "Lists all instructions currently available in the console.";
	}

}
