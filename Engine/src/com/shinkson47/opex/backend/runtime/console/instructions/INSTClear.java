package com.shinkson47.opex.backend.runtime.console.instructions;

import com.shinkson47.opex.backend.runtime.console.Console;

public class INSTClear implements IConsoleInstruction {

	@Override
	public void parse() {
		Console.clear();
	}

	@Override
	public String name() {
		return "clear";
	}

	@Override
	public void help() {
		Console.Write("Instruction contains no switches.");
	}

	@Override
	public String briefHelp() {
		return ("Writes 100 blank lines to the console window.");
	}

}
