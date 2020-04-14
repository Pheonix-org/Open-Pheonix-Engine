package backend.runtime.console.instructions;

import backend.runtime.JGELEnvironmentUtils;
import backend.runtime.console.JGELConsole;
import backend.runtime.console.JGELConsoleInstruction;

public class INSTHalt implements JGELConsoleInstruction {

	@Override
	public void parse() {
		JGELEnvironmentUtils.shutdown();
	}

	@Override
	public String name() {
		return "halt";
	}

	@Override
	public void help() {
		JGELConsole.Write("Instruction contains no switches.");
	}

	@Override
	public String briefHelp() {
		return "Halts the JGEL runtime via the runtime environment.";
	}

}