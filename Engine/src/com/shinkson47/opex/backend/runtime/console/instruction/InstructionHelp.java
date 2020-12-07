package com.shinkson47.opex.backend.runtime.console.instruction;

import java.io.Serializable;

/**
 * <h1>The help attribute of an instruction or a switch.</h1>
 *
 * @author <a href="https://www.shinkson47.in">Jordan T. Gray on 27/11/2020</a>
 * @version 1
 * @since v1
 */
public abstract class InstructionHelp implements NamedInstruction, Serializable {

    /**
     * <h2>The name of this attribute</h2>
     * This will be the string that the user types to access it.
     */
    private final String name;

    /**
     * <h2>The help string attribute</h2>
     * The string shown to the user when they request help about this attribute.
     */
    private final String help;

    //#region construction
    public InstructionHelp(String name, String help){
        this.name = name;
        this.help = help;
    }
    //#endregion construction

    //#region get/set
    public String getHelp() {
        return help;
    }

    @Override
    public String getName() {
        return name;
    }
    //#endregion get/set

    /**
     * <h2>Renders this attribute via {@link #RenderHelp}</h2>
     * @return The rendered help string
     */
    public String RenderHelp(){
        return RenderHelp(this);
    }

    /**
     * <h2>Produces a help string that's formatted and ready to display.</h2>
     * @param inst The instruction to render.
     * @return The rendered help string.
     */
    public static String RenderHelp(InstructionHelp inst) {
        if(inst == null) return "";
        return inst.getName() + " : " + inst.getHelp();
    }
}