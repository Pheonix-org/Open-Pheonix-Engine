package com.shinkson47.opex.backend.runtime.console.instruction.instructions;
import com.shinkson47.opex.backend.runtime.console.Console;
import com.shinkson47.opex.backend.runtime.console.instruction.Instruction;
import com.shinkson47.opex.backend.runtime.console.instruction.Switch;
import com.shinkson47.opex.backend.runtime.threading.OPEXThread;
import com.shinkson47.opex.backend.runtime.threading.ThreadManager;

import java.lang.Override;
import java.lang.String;

/**
 * Controls OPEX's thread manager
 */
public final class INSTthread extends Instruction {
  public INSTthread() {
    super("thread", "Controls OPEX's thread manager", new DEFAULTSwitch(), new killSwitch());
  }

  /**
   * Lists all threads controlled by OPEX
   */
  public static final class DEFAULTSwitch extends Switch {
    public DEFAULTSwitch() {
      super("DEFAULT", "Lists all threads controlled by OPEX", 0, 0);
    }

    /**
     * Lists all threads controlled by OPEX
     */
    @Override
    public boolean doAction(String[] args) {
      for (OPEXThread t : ThreadManager.getAllThreads())Console.instructionWrite(t.getThread().getName()); // TODO Change to pools when thread manager it mutated to use pools.
      return false;
    }
  }

  /**
   * Kills a thread by name [name : String!]
   */
  public static final class killSwitch extends Switch {
    public killSwitch() {
      super("kill", "Kills a thread by name [name : String!]", 1, 1);
    }

    /**
     * Kills a thread by name [name : String!]
     */
    @Override
    public boolean doAction(String[] args) {
      Console.instructionWrite("Force dispose thread call issued.");
      ThreadManager.forceDisposeThread(args[0]);
      return false;
    }
  }
}
