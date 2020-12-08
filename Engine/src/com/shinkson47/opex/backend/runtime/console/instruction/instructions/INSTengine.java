package com.shinkson47.opex.backend.runtime.console.instruction.instructions;

import com.shinkson47.opex.backend.runtime.console.Console;
import com.shinkson47.opex.backend.runtime.console.instruction.Instruction;
import com.shinkson47.opex.backend.runtime.console.instruction.Switch;
import com.shinkson47.opex.backend.runtime.environment.OPEX;
import com.shinkson47.opex.backend.runtime.environment.RuntimeHelper;
import com.shinkson47.opex.backend.toolbox.HaltCodes;

import java.lang.Override;
import java.lang.String;

/**
 * General engine controlls
 */
public final class INSTengine extends Instruction {
  public INSTengine() {
    super(INSTengine.class, "engine", "General engine controls");
  }

  /**
   * Registers a halt request
   */
  public static final class haltSwitch extends Switch {
    public haltSwitch() {
      super("halt", "Registers a halt request [Halt message : String[<=100]?] ", 0, 100);
    }

    /**
     * Registers a halt request
     */
    @Override
    public boolean doAction(String[] args) {
      if (args.length > 0)
        RuntimeHelper.shutdown(HaltCodes.ENGINE_SHUTDOWN_REQUEST, Console.tokesToString(args));
      else
        OPEX.Stop();
      return false;
    }
  }
}
