package BackEnd.Runtime.Console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import BackEnd.ErrorManagement.JGELEMS;
import BackEnd.Runtime.Threading.JGELRunnable;

public class JGELConsole implements JGELRunnable{
	private static List<JGELConsoleInstruction> instructions = new ArrayList<JGELConsoleInstruction>(); 
	/**
	 * @return the instructions
	 */
	public static List<JGELConsoleInstruction> getInstructions() {
		return List.copyOf(instructions);
	}


	private static BufferedReader InputReader = new BufferedReader(new InputStreamReader(System.in));   

	
	public static void ShowWindow() {
		//TODO gui version	
	}
	
	/**
	 * Adds an instruction parser into the console for use.
	 * 
	 * Internal instructions are located in BackEnd.Runtime.Console.Instructions.
	 * @param parser - the instruction parser to add.
	 */
	public static void AddInstruction(JGELConsoleInstruction parser) {
		if (GetInstruction(parser.Name()) == null) {
			instructions.add(parser);
		} else {
			JGELEMS.Warn("[JGELConsole] Attempted to register an instruction parser that already exsists, or has a conflicting name: " + parser.Name());
		}
	}
	
	/**
	 * Find a registered instruction parser by name.
	 * 
	 * @param name of the instruction who's parser is desired.
	 * @returns the parser.
	 * @returns null if no match is found.
	 */
	public static JGELConsoleInstruction GetInstruction(String name) {
		for (JGELConsoleInstruction inst : instructions) {
			if (inst.Name().contentEquals(name)) {
				return inst;
			}
		}	
		return null;
	}
		
	/**
	 * from the 
	 * @param line
	 */
	public static void Parse(String line) {
		JGELConsoleInstruction parser = GetInstruction(line);
		if (parser == null) {
			JGELEMS.Warn("Unknown Console Instruction Parsed.");
			return;
		}
		Write(parser.BriefHelp());
		parser.Help();
		parser.Parse();
	}
	
	public static Boolean GetParamBool(String Description) {	
		boolean parsed;
		while(true) {
			try {
				System.out.println("[JGELConsole] Param: " + Description);
				System.out.println("[JGELConsole] Ready for parameter [Boolean] >>");
				String input = InputReader.readLine();
				parsed = Boolean.parseBoolean(input);
				break;
			} catch (Exception e) {
				JGELEMS.Warn("Parameter was invalid.");
				JGELEMS.HandleException(e, true);
			}
		}
		return parsed;
	}
	
	public static String GetParamString(String Description) {
		String temp;
		while(true) {
			try {
				System.out.println("[JGELConsole] Param: " + Description);
				System.out.println("[JGELConsole] Ready for parameter [String] >>");
				temp = InputReader.readLine();
				if (temp == "") {
					throw new IllegalStateException("Parameter provided was empty.");
				}
				
				break;
			} catch (Exception e) {
				JGELEMS.Warn("Error whilst reading input");
				JGELEMS.HandleException(e, true);
			}
		}
		return temp;
	}
	
	public static String GetParamString(String Description, Class<?> filter) {
		String temp = GetParamString(Description);
		try {
			Enum.valueOf((Class<Enum>) filter, temp);
			return temp;
		} catch(Exception e) {
			JGELEMS.HandleException(new IllegalStateException("Input was not a valid option"));
		}
		return null;
	}
		
	public static void Clear() {
	for (int i = 0; i <= 100; i++) {
			System.out.println(" ");
		}
	}


	private static boolean ReadInput = false;
	@Override
	public void run() {
		InternalLog("[Console] Console thread starting. Use 'list' and 'help' to get started.");
		InternalLog("[Console] Boy, that's a lot of threads! Type 'thread', then 'list' to see them all!");
		ReadInput = true;
		while(ReadInput) {
			try {
				InternalLog("[Console] Console ready for instruction.");
				Parse(InputReader.readLine());
			} catch (IOException e) {
				JGELEMS.HandleException(e);
			}
		}
		
		System.out.print("[JGELConsole] Console Closed.");
	}
	
	
	//TODO make write private, and standardise for internal console writes only.
	//TODO make writeln for public line writing, but depricate it. Suggest using logging.
	public static void Write(String line) {
		System.out.println(line);
	}

	@Override
	public void stop() {
		ReadInput = false;
	}

	/**
	 * Log a message as if from inside of jgel.
	 * Intended for use only by jgel's internal classes.
	 * 
	 * For external logging @see this.ExternalLog(String message);
	 * @param message to log
	 */
	public static void InternalLog(String message) {
		System.out.println("[JGEL] " + message);
	}
	
	/**
	 * Log a message from an external perspective
	 * (Perspective of the game)
	 * @param message
	 */
	public static void ExternalLog(String message) {
		System.out.println("[Game] " + message);
	}

}
