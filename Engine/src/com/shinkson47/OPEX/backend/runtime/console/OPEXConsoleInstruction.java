package com.shinkson47.OPEX.backend.runtime.console;

/**
 * Super for all OPEXConsole Instruction Parsers. Parsers need to be added to
 * the console with OPEXConsole.AddInstruction.
 *
 * @see OPEXConsole
 * @author gordie
 *
 */
public interface OPEXConsoleInstruction {

	/**
	 * This instruction has been called, handle it.
	 *
	 * @see OPEXConsole
	 */
	public void parse();

	/**
	 * Name of the command parser. What the user will type to invoke this parser.
	 *
	 * Then must be fully in lowercase.
	 */
	public String name();

	/**
	 * Invoked when a user requests help on this command. This method should write
	 * to the terminal a man page type of help for this instruction, detailing
	 * arguments.
	 */
	public void help();

	/**
	 * A single line description of what the instruction is for.
	 *
	 * Not intended for useage or help.
	 * 
	 * @see this.Help();
	 */
	public String briefHelp();

}
