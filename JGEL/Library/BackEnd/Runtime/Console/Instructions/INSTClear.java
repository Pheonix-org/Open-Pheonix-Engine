package backend.runtime.console.instructions;

import backend.runtime.console.JGELConsole;
import backend.runtime.console.JGELConsoleInstruction;

public class INSTClear implements JGELConsoleInstruction {

	@Override
	public void parse() {
		JGELConsole.clear();
	}

	@Override
	public String name() {
		return "clear";
	}

	@Override
	public void help() {
		
	}

	@Override
	public String briefHelp() {
		return ("Writes 100 blank lines to the console window.");
	}

}
