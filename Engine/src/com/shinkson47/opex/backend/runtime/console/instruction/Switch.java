package com.shinkson47.opex.backend.runtime.console.instruction;

import com.shinkson47.opex.backend.runtime.console.Console;

import java.io.Serializable;

/**
 * <h1>A console instruction's operation</h1>
 * <br>
 * <p>
 * A switch represents an action that can be executed in the console, and is contained in an instruction.
 * An instruction may have more than one switch.
 * </p>
 *
 * @author <a href="https://www.shinkson47.in">Jordan T. Gray on 27/11/2020</a>
 * @version 1
 * @since v1
 */
public abstract class Switch extends InstructionHelp implements Serializable {

    /**
     * <h2>The name given to switches that're executed when no switch name is provided</h2>
     */
    public static final String DEFAULT_SWITCH_NAME = "DEFAULT";

    private final int minArgs;
    private final int maxArgs;


    public Switch(String name, String help, int minArgs, int maxArgs) {
        super(name, help);
        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
    }


    /**
     * <h2>Begins execution of this action.</h2>
     * Invoked externally, this call will validate the number of arguments parsed
     * before invoking {@link Switch#doAction(String[])}.
     * @param args The command line arguments from the user, not including the instruction or switch name.
     * @return false if number of arguments is not in range, or doAction fails. True if doAction returned successful.
     */
    public boolean startAction(String[] args){
        if (!argsInRange(args)) {
            Console.internalLog("Invalid number of arguments for this switch.");
            return false;
        }
        return doAction(args);
    }

    /**
     * <h2>This switches action</h2>
     * @param args the command line arguments parsed for this switch.
     * @return True if action was performed successfully. otherwise false.
     */
    protected abstract boolean doAction(String[] args);

    /**
     * <h2>Determines if the array parsed has a valid number of arguments</h2>
     * @param args The array to count
     * @return True if <i>args.length</i> is between {@link Switch#minArgs} and {@link Switch#maxArgs} inclusive.
     */
    protected boolean argsInRange(String[] args) {
       return args.length <= maxArgs && args.length >= minArgs;
    }
}
