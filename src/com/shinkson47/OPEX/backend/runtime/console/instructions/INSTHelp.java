package com.shinkson47.OPEX.backend.runtime.console.instructions;

import com.shinkson47.OPEX.backend.runtime.console.OPEXConsole;
import com.shinkson47.OPEX.backend.runtime.console.OPEXConsoleInstruction;

public final class INSTHelp implements OPEXConsoleInstruction {

	@Override
	public void parse() {
		OPEXConsoleInstruction inst = OPEXConsole
				.getInstruction(OPEXConsole.getParamString("name of the instruction to get help for"));
		OPEXConsole.Write(inst.name() + ": " + inst.briefHelp());
		inst.help();
	}

	@Override
	public String name() {
		return "help";
	}

	@Override
	public void help() {
		OPEXConsole.Write("Arguments:");
		OPEXConsole.Write("		name - name of the instruction to get help for.");
	}

	@Override
	public String briefHelp() {
		return "displays any help information written in an instruction.";
	}

}
