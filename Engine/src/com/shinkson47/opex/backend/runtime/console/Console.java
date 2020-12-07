package com.shinkson47.opex.backend.runtime.console;

import com.shinkson47.opex.backend.resources.pools.GlobalPools;
import com.shinkson47.opex.backend.runtime.console.instruction.Instruction;
import com.shinkson47.opex.backend.runtime.errormanagement.EMSHelper;
import com.shinkson47.opex.backend.runtime.errormanagement.LoggerUtils;
import com.shinkson47.opex.backend.runtime.invokation.BootInvokable;
import com.shinkson47.opex.backend.runtime.threading.IOPEXRunnable;
import com.sun.istack.internal.Nullable;
import org.reflections.Reflections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * <h1>OPEX's console.</h1>
 * <p>
 * A persistent thread started by the engine, which handles instruction invocation using user provided tokens from system.in.
 * To learn about Instructions and how to use the console, read the <a href=https://github.com/Pheonix-org/Open-Pheonix-Engine/wiki/Command-Line-Interface#command-line-interface>wiki</a>
 *
 *
 *<br><br>
 *
 * Console instructions are found and loaded automatically using reflection.
 * <br> Loaded instruction instances can be found in {@link GlobalPools#INSTRUCTION_POOL}
 *
 * @author Jordan Gray
 * @version 1.1
 * @since 2020.7.11.A
 */
public class Console extends BootInvokable implements IOPEXRunnable {

    //#region constants
    /**
     * <h2>New line with standardised indentation.</h2>
     *
     * When rendering text for the console's output, this starts a new line,
     * whose start matches the prefix <i>[OPEX] [CONSOLE] </i>.
     */
    public static final String NL_INDENTED = "\n\t\t\t\t ";

    /**
     * <h2>100 new line characters.</h2>
     */
    public static final String CLEAR_TEXT = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n";


    /**
     * <h2>Global sysin terminal reader.</h2>
     * The System In reader used throughout the console.
     */
    private static final BufferedReader InputReader = new BufferedReader(new InputStreamReader(System.in));

    public static final String ERR_NOT_VALID_COMMAND = " is not a valid instruction.";                                  // TODO replace with internationalisation when implemented.
    public static final String ERR_NO_SWITCH = "This instruction requires a switch name, but none was parsed.";         // TODO replace with internationalisation when implemented.
    private static final String VALUE_MSG = "value";
    //#endregion constants

    //#region vars
    /**
     * <h2>Local container for all console output</h2>
     */
    public static List<String> Lines = new ArrayList<String>();

    /**
     * <h2>If true, Console will remain idle and alive to await instruction</h2>
     * If set to false, the console will automatically close itself after the next parse.
     */
    public static boolean ReadInput = false;

    /**
     * <h2>If true, Console will print the success of every instruction executed</h2>
     */
    public static boolean prefShowSuccess = false; 																	    // TODO replace with preference pool once preferences are implemented.
    //#endregion

    //#region console thread
    /**
     * <h2>The main console thread operation</h2>
     * The method which persists to read user inputs and parses it as a console instruction.
     */
    @Override
    public void run() {
        instructionWrite("Console thread starting.");
        parse("help console");
        while (ReadInput) {
            try {
                instructionWrite("Console ready for instruction.");
                parse(InputReader.readLine());
            } catch (IOException e) {
                EMSHelper.handleException(e);
            }
        }
        instructionWrite(barMessage("Console Closed."));
    }

    /**
     * <h2>Close console thread notification</h2>
     * lowers {@link Console#ReadInput} flag.
     */
    @Override
    public void stop() {
        ReadInput = false;
    }
    //#endregion console thread

    //#region parsing
    /**
     * <h2>Parses a string of tokens</h2>
     * Locates instructions, and begins the chain of decoding and executing the instruction with the remaining tokens. <br>
     * This method itself does not locate or validate switches, parameters, or the like. That's the job of {@link Instruction#parse(String[])}
     *
     * @param line The console line to parse.
     */
    public static void parse(String line) {
        final String[] tokes = getTokens(line);                 // Split the string into seperate tokens
        if (tokes.length == 0) return;                          // Nothing to parse. Return.
        // Search for a matching instruction.
        Instruction inst = GlobalPools.INSTRUCTION_POOL.getOrDefault(tokes[0], null);
        if (inst == null) {                                     // If none is found,
            Write(tokes[0] + ERR_NOT_VALID_COMMAND);       // notify and return.
            return;
        }

        boolean result = inst.parse(StripFirst(tokes));         // Strip the instruction name from the tokens, and execute the instruction with the rest.
        // NB : Instruction.Parse handles the rest of the execution.

        if (prefShowSuccess)                                    // If requested,
            instructionWrite("Operation successful : " + result);// Show if the operation was successful.
    }

    /**
     * Parses string to system in.
     * <p>
     * provides a standardises method of parsing a string to the console
     *
     * @param data
     * @throws IOException if system.in cannot read the data.
     */
    public static void Parse(byte[] data) throws IOException {
        System.in.read(data);
    }

    /**
     * Parses string to system in.
     * <p>
     * provides a standardises method of parsing a string to the console
     *
     * @param data string to parse to system.in
     * @throws IOException if system.in cannot read the data.
     */
    public static void Parse(String data) throws IOException {
        Parse(data.getBytes());
    }

    public static String tokesToString(String[] tokes){
        StringBuilder builder = new StringBuilder();
        for (String s : tokes)
            builder.append(s + " ");

        return builder.toString();
    }

    //#endregion parsing

    //#region utility

    /**
     * Console instruction subroutine
     *
     * Adds OPEX's default internal console instructions to the console.
     */
    public static void loadConsoleInstructions() {
        // TODO support multiple URL's so clients can add thiers. Perhaps even find url from client object.
        scanAndAdd("com.shinkson47");
    }

    private static void scanAndAdd(String prefix){
        Reflections reflections = new Reflections(prefix);
        Set<Class<? extends Instruction>> classes = reflections.getSubTypesOf(Instruction.class);
        classes.forEach(o -> {
            try {
                o.newInstance();
            } catch (InstantiationException | IllegalAccessException e) { e.printStackTrace(); }
        });
    }


    /**
     * <h2>Splits the line provided by spaces.</h2>
     * i.e "help instruction" = <b>String["help", "instruction"]</b><br>
     * Parsing <b>null</b> results in <b>String[]</b>.
     *
     * @param tokes The line to split.
     * @return A list of words in from the parsed line.
     */
    public static String[] getTokens(String tokes) {
        return (tokes == null) ? new String[0] : tokes.split(" ");
    }

    /**
     * <h2>Removes the first element of the provided list of tokens.</h2>
     * Used when parsing the same token array to a sub 'parse'.<br>
     * i.e console create demo <br>
     * Once 'console' has been parsed and located, it will be stripped.<br>
     * The Console instruction only needs to receive the parameters that follow, not the name of itself.<br>
     * The same applies when the instruction parses the tokens to a switch.<br>
     *
     * @param tokes tokens to strip
     * @return A copy of the provided array, with the first element removed.
     */
    public static String[] StripFirst(@Nullable String[] tokes) {
        if (tokes.length <= 1) return new String[0];
        return Arrays.copyOfRange(tokes, 1, tokes.length, String[].class);
    }

    /**
     * <h2>Tests for token presence in an array of tokens.</h2>
     *
     * @param tokes Token array to search
     * @param test The token we're searching for.
     * @return True if one of the tokens in the array {@link String#matches(String)} the test.
     */
    public static boolean TokesContain(String[] tokes, String test) {
        Arrays.stream(tokes);
        boolean i = false;
        for (String toke : tokes) {     // For every token,
            i = toke.equals(test);      // test.
            if (i) break;               // And break if one matches. No need to search any more elements.
        }

        return i;
    }

    /**
     * <h2>Writes {@link Console#CLEAR_TEXT} to the console.</h2>
     */
    public static void clear() {
        Write(CLEAR_TEXT);
    }


    /**
     * <h2>Finds a delared field, and prompts the user for it's new value.</h2>
     *
     * @param cls The class which contains the field to be changed.
     * @param fieldName The name of the field to be changed
     * @return True if field was mutated successfully. False if modification is illegal, or no such field exists.
     */
    public static boolean findAndSet(Class<?> cls, String fieldName){
        try {
            Field field = cls.getDeclaredField(fieldName);
            field.setAccessible(true);
            final String name = field.getType().getSimpleName();
            if (name.toLowerCase().equals(Integer.class.getSimpleName().toLowerCase()) || //TODO this needs cleaning up.
                    name.toLowerCase().equals(Boolean.class.getSimpleName().toLowerCase()) ||
                    name.toLowerCase().equals(String.class.getSimpleName().toLowerCase()))
                field.set(field, getSpecifiedValue(name, VALUE_MSG));
            return true;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            EMSHelper.handleException(e);
            return false;
        }
    }



    /**
     * <h2>Wraps message with a bar of '='</h2>
     * ======================= msg =======================
     * @param msg the message to be wrapped.
     * @return the message provided in the format displayed.
     */
    public static String barMessage(String msg){
        return "======================= " + msg + " =======================";
    }
    //#endregion utility

    //#region writing
    /**
     * <h2>Writes a message to the console</h2>
     * in the format [OPEX] [CONSOLE] message
     * @param message
     */
    public static void instructionWrite(String message) {
        internalLog("[CONSOLE] " + message);
    }

    /**
     * Log a message as if from inside of OPEX. Intended for use only by OPEX's
     * internal classes.
     * <p>
     * For external logging @see this.ExternalLog(String message);
     *
     * @param message string to log
     */
    public static void internalLog(String message) {
        Write("[OPEX] " + message);
    }

    /**
     * Log a message from an external perspective (Perspective of the game)
     *
     * @param message
     */
    public static void externalLog(String message) {
        Write("[Game] " + message);
    }

    /**
     * <h2>Writes text directly the system.out</h2>
     * Also logs the text to {@link Console#Lines}
     * @param text the text to write
     * @deprecated Consider using {@link LoggerUtils#log(String)} or {@link Console#instructionWrite(String)} instead of
     * directly writing to the console.
     */
    @Deprecated
    public static void Write(String text) {
        Lines.add(text);
        System.out.println(text);
    }
    //#endregion writing

    //#region reading
    /**
     * <h2>Simple wrapper for {@link Console#getParamBool(String)}</h2>
     * Using this reads much clearly when obtaining confirmation from the user.
     * @param msg The prompt message
     * @return the user's response.
     */
    public static boolean confirm(String msg) {
        return getParamBool(msg);
    }

    /**
     * <h2>Prompts the user for a boolean parameter</h2>
     * Repeats prompt if input is not 'true' or 'false'.
     * Any other value, even a typo, would be evaluated as false by {@link Boolean#valueOf(String)},
     * even if that was not the user's intent.
     * @param Description reason for this parameter. Will be displayed to the user.
     * @return the value provided, once validated.
     */
    public static boolean getParamBool(String Description) {
        String value;
        do {
            value = getParam(Boolean.class.getSimpleName(), Description);
        } while (!(value.equals("true") || value.equals("false")));
        return Boolean.valueOf(value);
    }

    /**
     * <h2>Prompts the user for an integer parameter</h2>
     * Will repeatedly prompt if the input is not castable to an integer.
     * @param Description The reason for this parameter. Will be displayed to the user.
     * @return The user provided integer value.
     */
    public static int getParamInt(String Description) {
        int i;

        while (true) {
            try {
                i = Integer.parseInt(getParam(Integer.class.getSimpleName(), Description));
            } catch (Exception e) {
                continue;
            }
            break;
        }
        return i;
    }

    /**
     * <h2>Prompts the user for a string parameter.</h2>
     * @param Description The reason for this parameter. Will be displayed to the user.
     * @return any input that the user provides.
     */
    public static String getParamString(String Description) {
        return getParamString(Description, null);
    }

    /**
     * <h2>Prompts the user for a filtered string parameter.</h2>
     * Will return a user provided value, if it exists within the array of
     * <i>accepted values</i>. If acceptedValues is null
     *
     * @param Description The reason for this parameter. Will be displayed to the user.
     * @return any input that the user provides.
     */
    public static String getParamString(String Description, String[] acceptedValues) {
        while (true) {
            String value = getParam(String.class.getSimpleName(), Description);
            try {
                if ((acceptedValues != null && acceptedValues.length > 1) && !TokesContain(acceptedValues, value))
                    continue;

                return value;
            } catch (Exception e) {
                EMSHelper.handleException(new IllegalStateException("Input was not a valid option"));
            }
        }
    }

    /**
     * <h2>Gets a raw parameter</h2>
     * @param TypeName The name of the type that the user is expexted to enter.
     * @param description The reason for this prompt. Displayed to the user.
     * @return The raw user input.
     */
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

    /**
     * <h2>Dynamically prompts for a parameter</h2>
     * Infers which parameter fetching operation is to be used using the <i>type</i>
     * parameter.
     * @param type The type to enter. Either boolean, integer, or string.
     * @param message
     * @return
     */
    public static Object getSpecifiedValue(String type, String message) {
        switch (type.toLowerCase()) {
            case "boolean":
                return Console.getParamBool(message);
            case "string":
                return Console.getParamString(message);
            case "integer":
                return Console.getParamInt(message);
            default:
                internalLog("WARNING : Console was requested a parameter, but an unsupported type was provided. Raw user input will be used instead.");
                return getParam(type, message);
        }
    }
    //#endregion reading
}