package com.shinkson47.opex.backend.runtime.console.instruction.instructions;

import com.shinkson47.opex.backend.resources.pools.GlobalPools;
import com.shinkson47.opex.backend.runtime.console.Console;
import com.shinkson47.opex.backend.runtime.console.instruction.Instruction;
import com.shinkson47.opex.backend.runtime.console.instruction.Switch;

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
public class INSTList extends Instruction {

    private static class switchDefault extends Switch {
        public switchDefault() {
            super(Switch.DEFAULT_SWITCH_NAME, "as above.", 0,0);
        }

        /**
         * <h2>This switches action</h2>
         *
         * @param tokes the command line arguments parsed for this switch.
         * @return True if action was performed successfully. otherwise false.
         */
        @Override
        protected boolean doAction(String[] tokes) {
            List(false);
            return true;
        }
    }

    private static class switchVerbose extends Switch {

        public switchVerbose() {
            super("verbose", "Prints help for every instruction.", 0,0);
        }

        /**
         * <h2>This switches action</h2>
         *
         * @param args the command line arguments parsed for this switch.
         * @return True if action was performed successfully. otherwise false.
         */
        @Override
        protected boolean doAction(String[] args) {
            List(true);
            return false;
        }
    }

    public INSTList() {
        super("list", "Lists all pooled instruction", new switchDefault(), new switchVerbose());
    }

    private static void List(boolean verbose){
        for(String key : GlobalPools.INSTRUCTION_POOL.keySet()) {
            if (verbose)
                Console.instructionWrite(RenderHelp(GlobalPools.INSTRUCTION_POOL.get(key)));
            else
                Console.instructionWrite(GlobalPools.INSTRUCTION_POOL.get(key).RenderHelp());
        }
    }
}
