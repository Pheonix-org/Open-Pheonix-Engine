package backend.runtime.engine;
import backend.runtime.console.JGELConsole;
import backend.runtime.console.instructions.INSTClear;
import backend.runtime.console.instructions.INSTHalt;
import backend.runtime.console.instructions.INSTHelp;
import backend.runtime.console.instructions.INSTList;
import backend.runtime.console.instructions.INSTThread;
import backend.runtime.hooking.JGELHookUpdater;
import backend.runtime.threading.JGELThreadManager;

/**
 * This class is intended for use only by JGEl.
 * A container for organised and fragmentation of JGEL's startup routine.
 * 
 * Subroutines are written in source following the order of expected correct execution.
 * 
 * @author gordie
 */
final class JGELStartupRountine {

	/**
	 * Adds jgel's default internal console instructions to the console.
	 */
	protected static void addConsoleInstructions() {
		JGELConsole.addInstruction(new INSTClear());
		JGELConsole.addInstruction(new INSTHalt());
		JGELConsole.addInstruction(new INSTHelp());
		JGELConsole.addInstruction(new INSTList());
		JGELConsole.addInstruction(new INSTThread());
	}
	
	/**
	 * Uses the thread manager to execute JGEL's internal runables, causing them to run thier own startup routines and begin to idle.
	 * 
	 * This stage should be called once all resources the executables need have been loaded, and they've been configured correctly.
	 */
	protected static void startRunnables() {
		JGELThreadManager.createThread(new JGELHookUpdater(), "JGELHookUpdater");
		JGELThreadManager.createThread(new JGELConsole(), "JGELConsole");
	}
	

	
}
