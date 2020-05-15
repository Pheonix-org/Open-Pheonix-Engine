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
<<<<<<< HEAD:JGEL/Library/BackEnd/Runtime/Console/JGELConsoleInstruction.java
<<<<<<< Updated upstream:JGEL/Library/BackEnd/Runtime/Console/JGELConsoleInstruction.java
	 * 
	 * Not intended for useage or help.
=======
	 *
	 * Not intended for useage or help.
	 *
>>>>>>> Stashed changes:JGEL/src/com/shinkson47/JGEL/backend/runtime/console/JGELConsoleInstruction.java
=======
	 *
	 * Not intended for useage or help.
	 * 
>>>>>>> master:src/com/shinkson47/OPEX/backend/runtime/console/OPEXConsoleInstruction.java
	 * @see this.Help();
	 */
	public String briefHelp();

}
