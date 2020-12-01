package com.shinkson47.opex.backend.runtime.console.instruction;

import com.shinkson47.opex.backend.runtime.hooking.AutoInvokable;

import java.io.Serializable;

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
public abstract class InstructionHelp implements NamedInstruction, Serializable {

    private String name;
    private String help;

    public InstructionHelp(String name, String help){
        this.name = name;
        this.help = help;
    }

    public String getHelp() {
        return help;
    }

    @Override
    public String getName() {
        return name;
    }

    public String RenderHelp(){
        return RenderHelp(this);
    }

    public static String RenderHelp(InstructionHelp inst) {
        if(inst == null) return "";
        return inst.getName() + " : " + inst.getHelp();
    }

}
