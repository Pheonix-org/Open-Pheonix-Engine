package backend.runtime.console.instructions;

import backend.runtime.console.JGELConsole;
import backend.runtime.console.JGELConsoleInstruction;

public class INSTList implements JGELConsoleInstruction {

	@Override
	public void parse() {
		for (JGELConsoleInstruction inst : JGELConsole.getInstructions()) {
			JGELConsole.Write(inst.name() + ": " + inst.briefHelp());
		}
	}

	@Override
	public String name() {
		return "list";
	}

	@Override
	public void help() {
		JGELConsole.Write("Instructions visible are only those registered within the console using JGELConsole.AddInstruction()");
	}

	@Override
	public String briefHelp() {
		return "Lists all instructions currently available in the console.";
	}

}
