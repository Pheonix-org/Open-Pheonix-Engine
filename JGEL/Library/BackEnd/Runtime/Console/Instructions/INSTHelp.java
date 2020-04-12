package BackEnd.Runtime.Console.Instructions;

import BackEnd.Runtime.Console.JGELConsole;
import BackEnd.Runtime.Console.JGELConsoleInstruction;

public final class INSTHelp implements JGELConsoleInstruction {

	@Override
	public void Parse() {
		JGELConsoleInstruction inst = JGELConsole.GetInstruction(JGELConsole.GetParamString("name of the instruction to get help for"));
		JGELConsole.Write(inst.Name() + ": " + inst.BriefHelp());
		inst.Help();
	}

	@Override
	public String Name() {
		return "help";
	}

	@Override
	public void Help() {
		JGELConsole.Write("Arguments:");
		JGELConsole.Write("		name - name of the instruction to get help for.");
	}

	@Override
	public String BriefHelp() {
		return "displays any help information written in an instruction.";
	}

}
