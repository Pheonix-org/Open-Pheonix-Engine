package BackEnd.Runtime.Console;

/**
 * Super for all JGELConsole Instruction Parsers.
 * Parsers need to be added to the console with JGELConsole.AddInstruction.
 * 
 * @see JGELConsole
 * @author gordie
 *
 */
public interface JGELConsoleInstruction {
	
	/**
	 * This instruction has been called, handle it.
	 * 
	 * @see JGELConsole
	 */
	public void Parse();
	
	/**
	 * Name of the command parser. What the user will type to invoke this parser.
	 * 
	 * Then must be fully in lowercase.
	 */
	public String Name();
	
	/**
	 * Invoked when a user requests help on this command.
	 * This method should write to the terminal a man page type of help for this instruction, detailing arguments.
	 */
	public void Help();
	
	/**
	 * A single line description of what the instruction is for.
	 * 
	 * Not intended for useage or help.
	 * @see this.Help();
	 */
	public String BriefHelp();
}
