package com.shinkson47.opex.backend.runtime.console.instructions;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * <h1></h1>
 * <br>
 * <p>
 *
 * </p>
 *
 * @author <a href="https://www.shinkson47.in">Jordan T. Gray on 27/11/2020</a>
 * @version 1
 * @since v1
 */
public abstract class Instruction extends InstructionHelp {

    protected ArrayList<Switch> switches;

    public Instruction(String help) {
        this(help, new ArrayList<>());
    }

    public Instruction(String help, Switch... switches) {
        this(help, new ArrayList<>(Arrays.asList(switches)));
    }

    public Instruction(String help, ArrayList<Switch> switches) {
        super(help);
        this.switches = switches;
    }
}
