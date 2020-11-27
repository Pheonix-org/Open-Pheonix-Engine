package com.shinkson47.opex.backend.runtime.console.instructions;

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
                    "finds an instruction in the pool, then displays thier help",
                    1,1
                    );
        }

        /**
         * <h2>Performs this switches action</h2>
         *
         * @param args the command line arguments parsed for this switch.
         * @return True if action was performed successfully. otherwise false.
         */
        @Override
        protected boolean doAction(String[] args) {
            // TODO get the console interpreter to execute 'help help'
            return false;
        }
    }
    //#endregion switches

    public INSTHelp() {
        super("Shows help for pooled instructions.",
                new switchDefault()
        );
    }
}
