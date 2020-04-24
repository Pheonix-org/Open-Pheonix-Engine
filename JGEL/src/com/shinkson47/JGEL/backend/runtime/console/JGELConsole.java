package backend.runtime.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import backend.errormanagement.EMSHelper;
import backend.runtime.threading.JGELRunnable;

public class JGELConsole implements JGELRunnable {
	private static BufferedReader InputReader = new BufferedReader(new InputStreamReader(System.in));
	private static boolean ReadInput = false;

	private static List<JGELConsoleInstruction> instructions = new ArrayList<JGELConsoleInstruction>();

	/**
	 * @return the instructions
	 */
	public static List<JGELConsoleInstruction> getInstructions() {
		List<JGELConsoleInstruction> copy = new ArrayList<JGELConsoleInstruction>();
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
	public static void addInstruction(JGELConsoleInstruction parser) {
		if (getInstruction(parser.name()) == null) {
			instructions.add(parser);
		} else {
			EMSHelper.warn(
					"[JGELConsole] Attempted to register an instruction parser that already exsists, or has a conflicting name: "
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
	public static JGELConsoleInstruction getInstruction(String name) {
		for (JGELConsoleInstruction inst : instructions) {
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
		JGELConsoleInstruction parser = getInstruction(line);
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
				System.out.println("[JGELConsole] Param: " + Description);
				System.out.println("[JGELConsole] Ready for parameter [Boolean] >>");
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
				System.out.println("[JGELConsole] Param: " + Description);
				System.out.println("[JGELConsole] Ready for parameter [String] >>");
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
		System.out.print("[JGELConsole] Console Closed.");
	}

	// TODO make write private, and standardise for internal console writes only.
	// TODO make writeln for public line writing, but depricate it. Suggest using
	// logging.
	public static void Write(String line) {
		System.out.println(line);
	}

	@Override
	public void stop() {
		ReadInput = false;
	}

	/**
	 * Log a message as if from inside of jgel. Intended for use only by jgel's
	 * internal classes.
	 *
	 * For external logging @see this.ExternalLog(String message);
	 * 
	 * @param message to log
	 */
	public static void internalLog(String message) {
		System.out.println("[JGEL] " + message);
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