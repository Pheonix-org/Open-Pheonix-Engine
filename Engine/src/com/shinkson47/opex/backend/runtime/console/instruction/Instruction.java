package com.shinkson47.opex.backend.runtime.console.instruction;

import com.shinkson47.opex.backend.resources.pools.GlobalPools;
import com.shinkson47.opex.backend.resources.pools.KeySupplier;
import com.shinkson47.opex.backend.resources.pools.Pool;
import com.shinkson47.opex.backend.runtime.console.Console;
import com.shinkson47.opex.backend.runtime.errormanagement.EMSHelper;
import org.reflections.Reflections;

import java.io.Serializable;
import java.util.ArrayList;

import static com.shinkson47.opex.backend.runtime.console.instruction.Switch.SwitchKeySupplier;

/**
 * <h1>Abstract Console Instruction</h1>
 * <br>
 * <p>
 * Stores instruction switches, and handles parsing Instruction tokens, and invoking switches.
 * <br><br>
 * Instructions are loaded to the console automatically using reflection. See {@link Console#loadConsoleInstructions()}
 *
 *
 * </p>
 * @see <a href=https://github.com/Pheonix-org/Open-Pheonix-Engine/wiki/Command-Line-Interface#command-line-interface\>The Console wiki</a>
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

    /**
     * <h2>A list of all switches that this instruction has.</h2>
     *
     */
    protected Pool<Switch> switches = new Pool<>(getName() + "Switches");

    //#region constructor
    /**
     * <h2>Creates a new Instruction.</h2>
     * @param name The name of the instruction. This is what the user will type to invoke it.
     * @param help The help string for the instruction in general, not specific to any switches.
     */
    public Instruction(Class<? extends Instruction> inst, String name, String help) {
        this(inst, name, help, false);
    }

    /**
     * <h2>Creates a new Instruction.</h2>
     * @param name The name of the instruction. This is what the user will type to invoke it.
     * @param help The help string for the instruction in general, not specific to any switches.
     * @param defaultIfNull Should we use the default switch if the first token does not match a switch name?
     */
    public Instruction(Class<? extends Instruction> inst, String name, String help, boolean defaultIfNull) {
        super(name, help);
        this.switches.putArrayList(SwitchKeySupplier, findSwitches(inst));
        this.defaultIfNull = defaultIfNull;
        add(this);
    }

    //#endregion constructor.

    //#region get/set

    /**
     * @return a copy of the switch pool's contents as an array list.
     */
    public ArrayList<Switch> getSwitches() {
        return switches.valuesAsArrayList();
    }

    /**
     * </h2>Adds an instruction to the global instruction pool</h2>
     * @param inst The instruction to add.
     */
    public static void add(Instruction inst){
        GlobalPools.INSTRUCTION_POOL.put(inst.getName(), inst);
    }
    //#endregion get/set

    //#region execution

    /**
     * <h2>Begins executing this instruction with the provided tokens.</h2>
     * Uses switches to locate a matching switch, and executes it with
     * the remaining tokens.
     * @param tokes command line tokens, not including the name of this instruction.
     */
    public boolean parse(String[] tokes){
        Switch toActivate;

        if (tokes.length == 0) {                                     // If there's no tokens,
            toActivate = getDefault();                               // search for a default switch.
            if (toActivate == null)                                  // No token switch, and no default;
                Console.instructionWrite(Console.ERR_NO_SWITCH);     // Print error.

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
                        && activate(getDefault(), tokes, false)) //   Otherwise activate the default, and return the result.
                :
                activate(toActivate, tokes);                            // Otherwise activate the toActivate and return result.
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
    //#endregion execution

    //#region utility

    /**
     * <h2>Finds a switch by thier invocation name</h2>
     * @param Name The switch name to find.
     * @return A matching switch, or null.
     */
    protected Switch findSwitch(String Name){
        return switches.get(Name);
    }

    protected ArrayList<Switch> findSwitches(Class<? extends Instruction> i) {
        ArrayList <Class<? extends Switch>> in = new ArrayList<>(new Reflections(i.getName()).getSubTypesOf(Switch.class));
        ArrayList<Switch> out = new ArrayList<>();

        in.forEach(o -> {
            try {
                out.add(o.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                EMSHelper.handleException(e);
            }
        });

        return out;
    }

    /**
     * <h2>Locates and returns the default switch</h2>
     * As defined by {@link Switch#DEFAULT_SWITCH_NAME}.
     * @return The default switch. Null if there is no default switch.
     */
    protected Switch getDefault() {
        return findSwitch(Switch.DEFAULT_SWITCH_NAME);
    }

    /**
     * TODO -====================
     * @param inst
     * @return
     */
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

    protected void write(String s){
        Console.instructionWrite(s);
    }

    public static final KeySupplier<Instruction> InstructionKeySupplier = new KeySupplier<Instruction>(){
        /**
         * {@inheritDoc}
         * Uses an instruction's name as it's key.
         *
         * @param item The instruction to extract a name from.
         * @return A value that may be used as a Pool Key to represent that item.
         */
        @Override
        public String SupplyKey(Instruction item) {
            return item.getName();
        }
    };

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
    //#endregion utility
}
