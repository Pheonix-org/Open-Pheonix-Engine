package com.shinkson47.opex.backend.runtime.console.instruction.instructions;

import com.shinkson47.opex.backend.resources.pools.GlobalPools;
import com.shinkson47.opex.backend.runtime.console.Console;
import com.shinkson47.opex.backend.runtime.console.instruction.Instruction;
import com.shinkson47.opex.backend.runtime.console.instruction.Switch;

/**
 * <h1>The 'Help' Instruction</h1>
 * <br>
 * <p>
 * Displays the help strings found in instructions that're found in the TODO{@link ..} pool.
 * </p>
 *
 * @author <a href="https://www.shinkson47.in">Jordan T. Gray on 27/11/2020</a>
 * @version 1
 * @since v1
 */
public class INSTHelp extends Instruction {


    //#region switches

    /**
     * Default switch.
     * Displays help for the help instruction.
     */
    private static class switchDefault extends Switch {
        public switchDefault() {
            super(Switch.DEFAULT_SWITCH_NAME,
                    "shows the help for the 'Help' command.",
                    0,1
                    );
        }

        @Override
        protected boolean doAction(String[] args) {
            if (args.length == 0)
                Console.parse("help help");
            else{
                Instruction inst = GlobalPools.INSTRUCTION_POOL.get(args[0]);

                Console.instructionWrite(
                        (inst == null) ?
                                Console.NO_SUCH_INSTRUCTION
                                :
                                RenderHelp(inst)
                );
            }


            return true;
        }


    }

    //#endregion switches

    public INSTHelp() {
        super("help","Shows help for pooled instructions.",
                true, new switchDefault()
        );
    }

}

