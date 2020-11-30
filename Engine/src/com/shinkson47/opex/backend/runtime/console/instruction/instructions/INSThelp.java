package com.shinkson47.opex.backend.runtime.console.instruction.instructions;

import com.shinkson47.opex.backend.resources.pools.GlobalPools;
import com.shinkson47.opex.backend.runtime.console.Console;
import com.shinkson47.opex.backend.runtime.console.instruction.Instruction;
import com.shinkson47.opex.backend.runtime.console.instruction.Switch;
import java.lang.Override;
import java.lang.String;

/**
 * Shows the help strings for instructions and thier switches.
 */
public final class INSThelp extends Instruction {
  public INSThelp() {
    super("help", "Shows the help strings for instructions and thier switches.", true, new DEFAULTSwitch());
  }

  /**
   * Shows the help string for the parsed instruction
   */
  private static final class DEFAULTSwitch extends Switch {
    public DEFAULTSwitch() {
      super("DEFAULT", "as above. [instruction]", 0, 1);
    }

    /**
     * Shows the help string for the parsed instruction
     */
    @Override
    public boolean doAction(String[] args) {
      if (args.length == 0)
        Console.parse("help help");
      else
        Console.instructionWrite(
                Instruction.RenderHelp(GlobalPools.INSTRUCTION_POOL.get(args[0]))
        );

      return false;
    }
  }
}
