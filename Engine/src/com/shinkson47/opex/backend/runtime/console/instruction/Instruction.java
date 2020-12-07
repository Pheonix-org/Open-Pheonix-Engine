package com.shinkson47.opex.backend.runtime.console.instruction;

import com.shinkson47.opex.backend.resources.pools.GlobalPools;
import com.shinkson47.opex.backend.runtime.console.Console;
import com.shinkson47.opex.backend.runtime.invokation.AutoInvoke;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

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
public abstract class Instruction extends InstructionHelp implements Serializable {

    /**
     * <h2>If true, this switch will use the default switch when a switch name is not valid.</h2>
     * In this case, the token that was being interpreted as a switch will be preserved and parsed
     * to the default switch, along with all the subsequent tokens.
     */
    private boolean defaultIfNull;

    protected ArrayList<Switch> switches;

    public Instruction(){
        this("","");
    }

    public Instruction(String name, String help) {
        this(name, help, new ArrayList<>(), false);
    }

    public Instruction(String name, String help, boolean defaultIfNull) {
        this(name, help, new ArrayList<>(), defaultIfNull);
    }

    public Instruction(String name, String help, Switch... switches) {
        this(name, help, false, switches);
    }

    public Instruction(String name, String help, boolean defaultIfNull, Switch... switches) {
        this(name, help, new ArrayList<>(Arrays.asList(switches)), defaultIfNull);
    }

    public Instruction(String name, String help, ArrayList<Switch> switches, boolean defaultIfNull) {
        super(name, help);
        this.switches = switches;
        this.defaultIfNull = defaultIfNull;
        add(this);
    }

    /**
     * <h2>Begins executing this instruction.</h2>
     * Uses switches to locate a matching switch, and executes it with
     * the remaining tokens.
     * @param tokes command line tokens, not including the name of this instruction.
     */
    public boolean parse(String[] tokes){
        Switch toActivate;

        if (tokes.length == 0) {                                     // If there's no tokens,
            toActivate = getDefault();                               // search for a default switch.
            if (toActivate == null)                                  // No token switch, and no default;
                Console.instructionWrite(Console.ERR_NO_SWITCH);                // Print error.

        } else                                                       // If a switch token was parsed,
            toActivate = findSwitch(tokes[0]);                       // Find it.

                                                                     // If there's no switch to execute, return a failure.
                                                                     // Otherwise execure the switch with the remaining tokens, and return it's result.
        /*
            This is a simplified ternary. If to activate is null, the double && prevents the startAction,
            and evaluates the entire expression to false.

            It it's true, the right side will be executed, and the final evaluation will be calculated from
            true && execution result.
         */
        return (toActivate == null) ?                                  // If toActivate is null,
                (defaultIfNull)                                        // Should we default?
                        && ((getDefault() != null)                     //   Is default available? If not, return false.
                        && activate(getDefault(), tokes, false))  //   Otherwise activate the default, and return the result.
                :
                activate(toActivate, tokes);                            // Otherwise activate the toActivate and return result.
    }

    protected Switch getDefault() {
        final Switch def = findSwitch(Switch.DEFAULT_SWITCH_NAME);
        return def;
    }

    /**
     * <h2>Exectutes the provided switch.</h2>
     * Can be overriden to replace the functionality of this instructions activation.
     * @param toActivate The switch to activate.
     * @return true if operation was considered successful,
     */
    public boolean activate(Switch toActivate, String[] tokens) {
        return activate(toActivate, tokens, true);
    }

    /**
     * <h2>Exectutes the provided switch.</h2>
     * Can be overriden to replace the functionality of this instructions activation.
     * @param toActivate The switch to activate.
     * @return true if operation was considered successful,
     */
    public boolean activate(Switch toActivate, String[] tokens, boolean strip) {
        return toActivate.startAction((strip) ? Console.StripFirst(tokens) : tokens);
    }


    /**
     * <h2>Finds a switch by thier invocation name</h2>
     * @param Name The switch name to find.
     * @return A matching switch, or null.
     */
    protected Switch findSwitch(String Name){
        AtomicReference<Switch> s = new AtomicReference<>();
        switches.forEach(ls -> {if (ls.getName().equals(Name)) s.set(ls);}
        );
        return s.get();
    }

    public ArrayList<Switch> getSwitches() {
        return switches;
    }

    public static String RenderHelp(Instruction inst) {
        if(inst == null) return "";

        String renderedHelp =
                        Console.barMessage(inst.getName() + " instruction")
                        + Console.NL_INDENTED + inst.RenderHelp()
                        + Console.NL_INDENTED + Console.barMessage(inst.getName() + "'s Switches");
        for(Switch swtc : inst.getSwitches())
            renderedHelp += Console.NL_INDENTED + swtc.getName() + " : " + swtc.getHelp();

        renderedHelp += Console.NL_INDENTED;
        return renderedHelp;
    }

    public static void add(Instruction inst){
        GlobalPools.INSTRUCTION_POOL.put(inst.getName(), inst);
    }

    /**
     * {@inheritDoc}
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "Instruction:{name=" +
                getName()
                + ", " + Console.NL_INDENTED
                + RenderHelp(this)
        + "}";
    }

    protected void write(String s){
        Console.instructionWrite(s);
    }

}
