package com.shinkson47.opex.backend.runtime.console.instruction.instructions;

import com.shinkson47.opex.backend.runtime.console.instruction.Instruction;
import com.shinkson47.opex.backend.runtime.console.instruction.Switch;
import com.shinkson47.opex.backend.runtime.environment.OPEX;

import java.lang.Override;
import java.lang.String;

/**
 * test is just a test
 */
public final class INSTtest extends Instruction {
  public INSTtest() {
    super("test", "test is just a test", new DEFAULTSwitch(), new testSwitch());
  }

  /**
   * nothing, this is just a test
   */
  public static final class DEFAULTSwitch extends Switch {
    public DEFAULTSwitch() {
      super("DEFAULT", "nothing, this is just a test", 0, 0);
    }

    /**
     * nothing, this is just a test
     */
    @Override
    public boolean doAction(String[] args) {
      // TODO implement DEFAULT's functionality.;
      return false;
    }
  }

  /**
   * tests stuff
   */
  public static final class testSwitch extends Switch {
    public testSwitch() {
      super("test", "tests stuff", 1, 5);
    }

    /**
     * tests stuff
     */
    @Override
    public boolean doAction(String[] args) {
      OPEX.Stop();
      return false;
    }
  }
}
