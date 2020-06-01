package com.shinkson47.opex.backend.runtime.console.instructions;

import com.shinkson47.opex.backend.runtime.console.OPEXConsole;
import com.shinkson47.opex.backend.runtime.console.OPEXConsoleInstruction;

public class INSTClear implements OPEXConsoleInstruction {

	@Override
	public void parse() {
		OPEXConsole.clear();
	}

	@Override
	public String name() {
		return "clear";
	}

	@Override
	public void help() {
		OPEXConsole.Write("Instruction contains no switches.");
	}

	@Override
	public String briefHelp() {
		return ("Writes 100 blank lines to the console window.");
	}

}
