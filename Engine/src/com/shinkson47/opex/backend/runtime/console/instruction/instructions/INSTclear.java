package com.shinkson47.opex.backend.runtime.console.instruction.instructions;

import com.shinkson47.opex.backend.runtime.console.Console;
import com.shinkson47.opex.backend.runtime.console.instruction.Instruction;
import com.shinkson47.opex.backend.runtime.console.instruction.Switch;
import java.lang.Override;
import java.lang.String;

/**
 * Writes 100 blank lines to the console
 */
public final class INSTclear extends Instruction {
  public INSTclear() {
    super("clear", "Writes 100 blank lines to the console", new DEFAULTSwitch());
  }

  /**
   * as above.
   */
  public static final class DEFAULTSwitch extends Switch {
    public DEFAULTSwitch() {
      super("DEFAULT", "as above.", 0, 0);
    }

    /**
     * as above.
     */
    @Override
    public boolean doAction(String[] args) {
      Console.clear();
      return false;
    }
  }
}
