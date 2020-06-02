package com.shinkson47.opex.backend.runtime.console.instructions;

import com.shinkson47.opex.backend.runtime.console.Console;

public final class INSTHelp implements IConsoleInstruction {

	@Override
	public void parse() {
		IConsoleInstruction inst = Console
				.getInstruction(Console.getParamString("name of the instruction to get help for"));
		Console.Write(inst.name() + ": " + inst.briefHelp());
		inst.help();
	}

	@Override
	public String name() {
		return "help";
	}

	@Override
	public void help() {
		Console.Write("Arguments:");
		Console.Write("		name - name of the instruction to get help for.");
	}

	@Override
	public String briefHelp() {
		return "displays any help information written in an instruction.";
	}

}
