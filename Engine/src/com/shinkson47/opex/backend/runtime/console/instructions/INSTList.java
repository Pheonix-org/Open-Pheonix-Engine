package com.shinkson47.opex.backend.runtime.console.instructions;

import com.shinkson47.opex.backend.runtime.console.OPEXConsole;
import com.shinkson47.opex.backend.runtime.console.OPEXConsoleInstruction;

public class INSTList implements OPEXConsoleInstruction {

	@Override
	public void parse() {
		for (OPEXConsoleInstruction inst : OPEXConsole.getInstructions()) {
			OPEXConsole.Write(inst.name() + ": " + inst.briefHelp());
		}
	}

	@Override
	public String name() {
		return "list";
	}

	@Override
	public void help() {
		OPEXConsole.Write(
				"Instructions visible are only those registered within the console using OPEXConsole.AddInstruction()");
	}

	@Override
	public String briefHelp() {
		return "Lists all instructions currently available in the console.";
	}

}
