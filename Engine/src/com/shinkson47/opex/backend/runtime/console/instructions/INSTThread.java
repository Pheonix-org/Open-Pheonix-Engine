package com.shinkson47.opex.backend.runtime.console.instructions;

import com.shinkson47.opex.backend.runtime.console.Console;
import com.shinkson47.opex.backend.runtime.threading.OPEXThread;
import com.shinkson47.opex.backend.runtime.threading.OPEXThreadManager;

/**
 * Controls the thread manager
 * 
 * @author OPEXConmmandBuilder
 **/
public final class INSTThread implements IConsoleInstruction {
	public enum ConsoleOptions {
		list, kill,
	}

	@Override
	public void parse() {
		switch (Console.getParamString("Switch to perform", ConsoleOptions.class)) {
		case "list":
			for (OPEXThread thread : OPEXThreadManager.getAllThreads()) {
				System.out.println("[OPEXConsole] " + "[" + thread.getID() + "] " + thread.getThread().getName());
			}

			break;
		case "kill":
			Console.Write("[OPEXThread] Thread killed: " + OPEXThreadManager.forceDisposeThread(
					OPEXThreadManager.getThread(Console.getParamString("Name of thread to kill"))));

			break;

		default:
			Console.notifyUnknownSwitch();
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
		Console.Write("Arguments:");
		Console.Write("	list - lists all threads");
		Console.Write("	kill - kills a thread by name");

	}
}