package com.shinkson47.opex.backend.runtime.console;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import com.shinkson47.opex.backend.resources.pools.GlobalPools;
import com.shinkson47.opex.backend.runtime.console.instruction.IConsoleInstruction;
import com.shinkson47.opex.backend.runtime.console.instruction.Instruction;
import com.shinkson47.opex.backend.runtime.errormanagement.EMSHelper;
import com.shinkson47.opex.backend.runtime.threading.IOPEXRunnable;
import javafx.collections.ObservableList;
import javafx.scene.layout.BorderPane;
import sun.security.krb5.internal.crypto.Des;

/**
 * <h1>OPEX's console.</h1>
 *
 * Started by the engine, this thread handles instruction invocation using strings from system.in.
 * IConsoleInstructions must be added with addInstruction(IConsoleInstruction) to be invokable.
 *
 * @since 2020.7.11.A
 * @version 1.1
 * @author Jordan Gray
 */
public class Console implements IOPEXRunnable {
    public static final String NL_INDENTED = "\n\t\t\t\t ";

    //#region constants
	/**
	 * <h2>Global sysin terminal reader.</h2>
	 */
	private static final BufferedReader InputReader = new BufferedReader(new InputStreamReader(System.in));

	public static final String ERR_NOT_VALID_COMMAND = " is not a valid instruction."; 									// TODO replace with internationalisation when implemented.
	public static final String ERR_NO_SWITCH = "This instruction requires a switch name, but none was parsed."; 		// TODO replace with internationalisation when implemented.
	public static final String NO_SUCH_INSTRUCTION = "No such instruction exists!";
	//#endregion constants


	//#region vars
	/**
	 * Triggers events for every line added to the console's output.
	 * use to monitor, grab, and display console output.
	 */
	public static List<String> Lines = new ArrayList<String>();

	/**
	 * <h2>Whilst true, console thread is kept alive to read instructions.</h2>
	 */
	private static boolean ReadInput = false;
	private static boolean prefShowSuccess = false; // TODO replace with preference pool once preferences are implemented.
	//#endregion

	/**
	 * No longer has no effect.
	 * @deprecated  this will be removed.
	 */
	@Deprecated
	public static List<IConsoleInstruction> getInstructions() {
		return null;
	}

	/**
	 * <h2>No longer has any effect.</h2>
	 * @deprecated this will be removed.
	 */
	@Deprecated
	public static void addInstruction(Instruction inst) {
		GlobalPools.INSTRUCTION_POOL.put(inst.getName(), inst);
	}

	/**
	 * <h2>No longer has any effect.</h2>
	 * @deprecated this will be removed.
	 */
	public static IConsoleInstruction getInstruction(String name) {
		return null;
	}

	/**
	 * <h2>Locates instructions, and executes it.</h2>
	 * 
	 * @param line
	 */
	public static void parse(String line) {
		String[] tokes = getTokens(line);
		if (tokes.length == 0) return;  				 // Nothing to parse. Return.

		Instruction inst = GlobalPools.INSTRUCTION_POOL.getOrDefault(tokes[0], null);
		if (inst == null) {
			Write(tokes[0] + ERR_NOT_VALID_COMMAND);
			return;
		}

		boolean result = inst.parse(StripFirst(tokes));

		if (prefShowSuccess)
			instructionWrite("Operation successful : " + result);
	}

	/**
	 * <h2>Splits the line provided by spaces.</h2>
	 * i.e "help instruction" = <b>String["help", "instruction"]</b><br>
	 * Parsing <b>null</b> results in <b>String[]</b>.
	 * @param tokes The line to split.
	 * @return A list of words in from the line.
	 */
	public static String[] getTokens(String tokes){
		return (tokes == null) ? new String[0] : tokes.split(" ");
	}

	/**
	 * Removes the first element of the provided list of tokens.
	 * TODO test
	 * @param tokes tokens to strip
	 * @return the remaining token array.
	 */
	public static String[] StripFirst(String[] tokes) {
		if (tokes.length <= 1) return new String[0];
		return Arrays.copyOfRange(tokes, 1, tokes.length, String[].class);
	}

	public static boolean TokesContain(String[] tokes, String test){
		boolean i = false;
		for(String toke : tokes)
			i = (!i && toke.equals(test));

		return i;
	}


	@Override
	public void run() {
		instructionWrite("Console thread starting. Use 'list' and 'help' to get started.");
		ReadInput = true;
		while (ReadInput) {
			try {
				instructionWrite("Console ready for instruction.");
				parse(InputReader.readLine());
			} catch (IOException e) {
				EMSHelper.handleException(e);
			}
		}
		instructionWrite("Console Closed.");
	}

	// TODO make write private, and standardise for internal console writes only.
	// TODO make writeln for public line writing, but depricate it. Suggest using
	// logging.
	public static void Write(String line) {
		Lines.add(line);
		System.out.println(line);
	}

	@Override
	public void stop() {
		ReadInput = false;
	}

	/**
	 * Prompts the user for a boolean parameter, parses once sucessfully gathered.
	 * @param Description reason for this parameter
	 * @return the value provided, once validated.
	 */
	public static Boolean getParamBool(String Description) {
		return Boolean.valueOf(getParam(Boolean.class.getSimpleName(),Description));
	}

	public static Integer getParamInt(String Description){
		return Integer.parseInt(getParam(Integer.class.getSimpleName(), Description));
	}

	public static String getParamString(String Description) {
		return getParamString(Description, null);
	}

	public static String getParamString(String Description, Class<Enum> filter) {
		while (true) {
			String value = getParam(String.class.getSimpleName(), Description);
			try {
				if (filter != null)
					Enum.valueOf(filter, value);
				return value;
			} catch (Exception e) {
				EMSHelper.handleException(new IllegalStateException("Input was not a valid option"));
			}
		}
	}

	public static String getParam(String TypeName, String description) {
		System.out.println("[OPEXConsole] Param: " + description);
		System.out.println("[OPEXConsole] Ready for parameter [" + TypeName + "] >>");
		try {
			return InputReader.readLine();
		} catch (IOException e) {
			EMSHelper.handleException(e);
		}
		return "";
	}

	public static void instructionWrite(String message) {
		internalLog("[CONSOLE] " + message);
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

	/**
	 * Parses string to system in.
	 *
	 * provides a standardises method of parsing a string to the console
	 *
	 * @throws IOException if system.in cannot read the data.
	 * @param data
	 */
	public static void Parse(byte[] data) throws IOException {
		System.in.read(data);
	}

	/**
	 * Parses string to system in.
	 *
	 * provides a standardises method of parsing a string to the console
	 *
	 * @throws IOException if system.in cannot read the data.
	 * @param data string to parse to system.in
	 */
	public static void Parse(String data) throws IOException {Parse(data.getBytes());}

}