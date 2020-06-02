package com.shinkson47.opex.backend.runtime.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.shinkson47.opex.backend.runtime.console.instructions.IConsoleInstruction;
import com.shinkson47.opex.backend.runtime.errormanagement.EMSHelper;
import com.shinkson47.opex.backend.runtime.hooking.EventHooker;
import com.shinkson47.opex.backend.runtime.threading.IOPEXRunnable;

public class Console implements IOPEXRunnable {
	private static BufferedReader InputReader = new BufferedReader(new InputStreamReader(System.in));
	/**
	 * Triggers events for every line added to the console's output.
	 * use to monitor, grab, and display console output.
	 */
	public static EventHooker OutputEventHooker = new EventHooker();
	public static List<String> Lines = new ArrayList<String>();
	private static boolean ReadInput = false;

	private static List<IConsoleInstruction> instructions = new ArrayList<IConsoleInstruction>();

	/**
	 * @return the instructions
	 */
	public static List<IConsoleInstruction> getInstructions() {
		List<IConsoleInstruction> copy = new ArrayList<IConsoleInstruction>();
		copy.addAll(instructions);
		return copy;
	}

	public static void showWindow() {
		// TODO gui version
	}

	/**
	 * Adds an instruction parser into the console for use.
	 *
	 * Internal instructions are located in BackEnd.Runtime.Console.Instructions.
	 * 
	 * @param parser - the instruction parser to add.
	 */
	public static void addInstruction(IConsoleInstruction parser) {
		if (getInstruction(parser.name()) == null) {
			instructions.add(parser);
		} else {
			EMSHelper.warn(
					"[OPEXConsole] Attempted to register an instruction parser that already exsists, or has a conflicting name: "
							+ parser.name());
		}
	}

	/**
	 * Find a registered instruction parser by name.
	 *
	 * @param name of the instruction who's parser is desired.
	 * @returns the parser.
	 * @returns null if no match is found.
	 */
	public static IConsoleInstruction getInstruction(String name) {
		for (IConsoleInstruction inst : instructions) {
			if (inst.name().contentEquals(name)) {
				return inst;
			}
		}
		return null;
	}

	/**
	 * from the
	 * 
	 * @param line
	 */
	public static void parse(String line) {
		IConsoleInstruction parser = getInstruction(line);
		if (parser == null) {
			EMSHelper.warn("Unknown Console Instruction Parsed.");
			return;
		}
		Write(parser.briefHelp());
		parser.help();
		parser.parse();
	}

	public static Boolean getParamBool(String Description) {
		boolean parsed;
		while (true) {
			try {
				System.out.println("[OPEXConsole] Param: " + Description);
				System.out.println("[OPEXConsole] Ready for parameter [Boolean] >>");
				String input = InputReader.readLine();
				parsed = Boolean.parseBoolean(input);
				break;
			} catch (Exception e) {
				EMSHelper.warn("Parameter was invalid.");
				EMSHelper.handleException(e, true);
			}
		}
		return parsed;
	}

	public static String getParamString(String Description) {
		String temp;
		while (true) {
			try {
				System.out.println("[OPEXConsole] Param: " + Description);
				System.out.println("[OPEXConsole] Ready for parameter [String] >>");
				temp = InputReader.readLine();
				if ("".equals(temp)) {
					throw new IllegalStateException("Parameter provided was empty.");
				}

				break;
			} catch (Exception e) {
				EMSHelper.warn("Error whilst reading input");
				EMSHelper.handleException(e, true);
			}
		}
		return temp;
	}

	public static String getParamString(String Description, Class<?> filter) {
		String temp = getParamString(Description);
		try {
			Enum.valueOf((Class<Enum>) filter, temp);
			return temp;
		} catch (Exception e) {
			EMSHelper.handleException(new IllegalStateException("Input was not a valid option"));
		}
		return null;
	}

	public static void clear() {
		for (int i = 0; i <= 100; i++) {
			System.out.println(" ");
		}
	}

	@Override
	public void run() {
		internalLog("[Console] Console thread starting. Use 'list' and 'help' to get started.");
		internalLog("[Console] Boy, that's a lot of threads! Type 'thread', then 'list' to see them all!");
		ReadInput = true;
		while (ReadInput) {
			try {
				internalLog("[Console] Console ready for instruction.");
				parse(InputReader.readLine());
			} catch (IOException e) {
				EMSHelper.handleException(e);
			}
		}
		System.out.print("[OPEXConsole] Console Closed.");
	}

	// TODO make write private, and standardise for internal console writes only.
	// TODO make writeln for public line writing, but depricate it. Suggest using
	// logging.
	public static void Write(String line) {
		Lines.add(line);
		System.out.println(line);
		OutputEventHooker.triggerUpdate();
	}

	@Override
	public void stop() {
		ReadInput = false;
	}

	/**
	 * Log a message as if from inside of OPEX. Intended for use only by OPEX's
	 * internal classes.
	 *
	 * For external logging @see this.ExternalLog(String message);
	 * 
	 * @param message to log
	 */
	public static void internalLog(String message) {
		System.out.println("[OPEX] " + message);
	}

	/**
	 * Log a message from an external perspective (Perspective of the game)
	 * 
	 * @param message
	 */
	public static void externalLog(String message) {
		System.out.println("[Game] " + message);
	}

	public static void notifyUnknownSwitch() {
		Write("no such switch. Use help <inst>.");
	}
}