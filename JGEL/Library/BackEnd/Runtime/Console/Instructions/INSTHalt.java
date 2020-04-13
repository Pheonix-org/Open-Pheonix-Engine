package BackEnd.Runtime.Console.Instructions;

import BackEnd.Runtime.JGELEnvironmentManager;
import BackEnd.Runtime.Console.JGELConsoleInstruction;

public class INSTHalt implements JGELConsoleInstruction {

	@Override
	public void Parse() {
		JGELEnvironmentManager.Shutdown();
	}

	@Override
	public String Name() {
		return "halt";
	}

	@Override
	public void Help() {

	}

	@Override
	public String BriefHelp() {
		return "Halts the JGEL runtime via the runtime environment.";
	}

}
