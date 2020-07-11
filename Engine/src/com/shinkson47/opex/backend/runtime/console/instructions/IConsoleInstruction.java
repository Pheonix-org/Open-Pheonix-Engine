package com.shinkson47.opex.backend.runtime.console.instructions;

import com.shinkson47.opex.backend.runtime.console.Console;

/**
 * Super for all OPEXConsole Instruction Parsers. Parsers need to be added to
 * the console with OPEXConsole.AddInstruction.
 *
 * @see Console
 * @author gordie
 * @version 1
 */
public interface IConsoleInstruction {

	/**
	 * This instruction has been called, handle it.
	 *
	 * @see Console
	 */
	void parse();

	/**
	 * Name of the command parser. What the user will type to invoke this parser.
	 *
	 * Then must be fully in lowercase.
	 */
	String name();

	/**
	 * Invoked when a user requests help on this command. This method should write
	 * to the terminal a man page type of help for this instruction, detailing
	 * arguments.
	 */
	void help();

	/**
	 * A single line description of what the instruction is for.
	 *
	 * Not intended for useage or help.
	 * 
	 * @see this.Help();
	 */
	String briefHelp();

}
