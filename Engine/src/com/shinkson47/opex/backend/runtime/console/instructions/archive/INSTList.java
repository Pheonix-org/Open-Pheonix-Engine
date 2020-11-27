package com.shinkson47.opex.backend.runtime.console.instructions.archive;

import com.shinkson47.opex.backend.runtime.console.Console;
import com.shinkson47.opex.backend.runtime.console.instructions.IConsoleInstruction;

public class INSTList implements IConsoleInstruction {

	@Override
	public void parse() {
		for (IConsoleInstruction inst : Console.getInstructions()) {
			Console.Write(inst.name() + ": " + inst.briefHelp());
		}
	}

	@Override
	public String name() {
		return "list";
	}

	@Override
	public void help() {
		Console.Write(
				"Instructions visible are only those registered within the console using OPEXConsole.AddInstruction()");
	}

	@Override
	public String briefHelp() {
		return "Lists all instructions currently available in the console.";
	}

}
