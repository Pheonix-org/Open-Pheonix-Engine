package BackEnd.Runtime.Console.Instructions;

import BackEnd.Runtime.Console.JGELConsole;
import BackEnd.Runtime.Console.JGELConsoleInstruction;

public class INSTClear implements JGELConsoleInstruction {

	@Override
	public void Parse() {
		JGELConsole.Clear();
	}

	@Override
	public String Name() {
		return "clear";
	}

	@Override
	public void Help() {
		
	}

	@Override
	public String BriefHelp() {
		return ("Writes 100 blank lines to the console window.");
	}

}
