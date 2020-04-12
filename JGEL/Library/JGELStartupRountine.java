import BackEnd.Runtime.Console.JGELConsole;
import BackEnd.Runtime.Console.Instructions.INSTClear;
import BackEnd.Runtime.Console.Instructions.INSTHalt;
import BackEnd.Runtime.Console.Instructions.INSTHelp;
import BackEnd.Runtime.Console.Instructions.INSTList;
import BackEnd.Runtime.Console.Instructions.INSTThread;
import BackEnd.Runtime.Hooking.JGELHookUpdater;
import BackEnd.Runtime.Threading.JGELThreadManager;

/**
 * This class is intended for use only by JGEl.
 * A container for organised and fragmentation of JGEL's startup routine.
 * 
 * Subroutines are written in source following the order of expected correct execution.
 * 
 * @author gordie
 *
 */
final class JGELStartupRountine {

	/**
	 * Adds
	 */
	protected static void AddConsoleInstructions() {
		JGELConsole.AddInstruction(new INSTClear());
		JGELConsole.AddInstruction(new INSTHalt());
		JGELConsole.AddInstruction(new INSTHelp());
		JGELConsole.AddInstruction(new INSTList());
		JGELConsole.AddInstruction(new INSTThread());
	}
	
	/**
	 * Uses the thread manager to execute JGEL's internal runables, causing them to run thier own startup routines and begin to idle.
	 * 
	 * This stage should be called once all resources the executables need have been loaded, and they've been configured correctly.
	 */
	protected static void StartRunnables() {
		JGELThreadManager.CreateThread(new JGELHookUpdater(), "JGELHookUpdater");
		JGELThreadManager.CreateThread(new JGELConsole(), "JGELConsole");
	}
	

	
}
