package backend.runtime.console.instructions;

import backend.runtime.console.JGELConsole;
import backend.runtime.console.JGELConsoleInstruction;

public final class INSTHelp implements JGELConsoleInstruction {

	@Override
	public void parse() {
		JGELConsoleInstruction inst = JGELConsole.getInstruction(JGELConsole.getParamString("name of the instruction to get help for"));
		JGELConsole.Write(inst.name() + ": " + inst.briefHelp());
		inst.help();
	}

	@Override
	public String name() {
		return "help";
	}

	@Override
	public void help() {
		JGELConsole.Write("Arguments:");
		JGELConsole.Write("		name - name of the instruction to get help for.");
	}

	@Override
	public String briefHelp() {
		return "displays any help information written in an instruction.";
	}

}
