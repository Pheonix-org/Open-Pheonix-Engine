package com.shinkson47.opex.backend.runtime.console.instructions;

import com.shinkson47.opex.backend.runtime.console.OPEXConsole;
import com.shinkson47.opex.backend.runtime.console.OPEXConsoleInstruction;
import com.shinkson47.opex.backend.runtime.threading.OPEXThread;
import com.shinkson47.opex.backend.runtime.threading.OPEXThreadManager;

/**
 * Controls the thread manager
 * 
 * @author OPEXConmmandBuilder
 **/
public final class INSTThread implements OPEXConsoleInstruction {
	public enum ConsoleOptions {
		list, kill,
	}

	@Override
	public void parse() {
		switch (OPEXConsole.getParamString("Switch to perform", ConsoleOptions.class)) {
		case "list":
			for (OPEXThread thread : OPEXThreadManager.getAllThreads()) {
				System.out.println("[OPEXConsole] " + "[" + thread.getID() + "] " + thread.getThread().getName());
			}

			break;
		case "kill":
			OPEXConsole.Write("[OPEXThread] Thread killed: " + OPEXThreadManager.forceDisposeThread(
					OPEXThreadManager.getThread(OPEXConsole.getParamString("Name of thread to kill"))));

			break;

		default:
			OPEXConsole.notifyUnknownSwitch();
			break;
		}
	}

	@Override
	public String name() {
		return "thread";
	}

	@Override
	public String briefHelp() {
		return "Controlls the thread manager";
	}

	@Override
	public void help() {
		OPEXConsole.Write("Arguments:");
		OPEXConsole.Write("	list - lists all threads");
		OPEXConsole.Write("	kill - kills a thread by name");

	}
}