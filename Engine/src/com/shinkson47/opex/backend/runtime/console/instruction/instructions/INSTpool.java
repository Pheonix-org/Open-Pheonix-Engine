package com.shinkson47.opex.backend.runtime.console.instruction.instructions;

import com.shinkson47.opex.backend.resources.pools.GlobalPools;
import com.shinkson47.opex.backend.resources.pools.Pool;
import com.shinkson47.opex.backend.runtime.console.Console;
import com.shinkson47.opex.backend.runtime.console.instruction.Instruction;
import com.shinkson47.opex.backend.runtime.console.instruction.Switch;
import java.lang.Override;
import java.lang.String;

/**
 * Controlls the global pools
 */
public final class INSTpool extends Instruction {
  public INSTpool() {
    super("pool", "Controls the global pools", new keysSwitch(), new DEFAULTSwitch(), new peekSwitch(), new pokeSwitch(), new unregisterSwitch(), new removeSwitch());
  }

  /**
   * Lists all registered pools
   */
  public static final class DEFAULTSwitch extends Switch {
    public DEFAULTSwitch() {
      super("DEFAULT", "Lists all registered pools", 0, 0);
    }

    /**
     * Lists all registered pools
     */
    @Override
    public boolean doAction(String[] args) {
      Console.instructionWrite("All registered global pools:");
      String out = "";
      for (String p : GlobalPools.AllGlobal.keySet())
          out += GlobalPools.AllGlobal.get(p).getName() + Console.NL_INDENTED;

      Console.instructionWrite(out);
      return true;
    }
  }

  /**
   * Displays a key's value from a specified pool
   */
  public static final class peekSwitch extends Switch {
    public peekSwitch() {
      super("peek", "Displays a key's value from a specified pool that uses strings as keys. [pool, key]", 2, 2);
    }

    /**
     * Displays a key's value from a specified pool
     */
    @Override
    public boolean doAction(String[] args) {
      Pool p = GlobalPools.AllGlobal.get(args[0]);
      if (p == null) {
        Console.instructionWrite(args[0] + " is not a pool that exists!");
        return false;
      }

      Console.instructionWrite(
              args[0] + "#" + args[1] + " : " + p.get(args[1])
      );
      return true;
    }
  }

  /**
   * Modifies primitive type pools
   */
  public static final class pokeSwitch extends Switch {
    public pokeSwitch() {
      super("poke", "Modifies primitive type pools", 2, 2);
    }

    /**
     * Modifies primitive type pools
     */
    @Override
    public boolean doAction(String[] args) {
      // TODO implement poke's functionality.;
      return false;
    }
  }

  /**
   * Removes a pool from the global pools
   */
  public static final class unregisterSwitch extends Switch {
    public unregisterSwitch() {
      super("unregister", "Removes a pool from the global pools [pool]", 1, 1);
    }

    /**
     * Removes a pool from the global pools
     */
    @Override
    public boolean doAction(String[] args) {
      return GlobalPools.AllGlobal.remove(args[0]) != null;
    }
  }

  /**
   * Removes a key from the specified pool
   */
  public static final class removeSwitch extends Switch {
    public removeSwitch() {
      super("remove", "Removes a key from the specified pool [pool, key] ", 2, 2);
    }

    /**
     * Removes a key from the specified pool
     */
    @Override
    public boolean doAction(String[] args) {
      Pool p = GlobalPools.AllGlobal.get(args[0]);
      if (p == null) {
        Console.instructionWrite(args[0] + " is not a valid pool!");
        return false;
      }

      return p.remove(args[1]) != null;
    }
  }

  public static final class keysSwitch extends Switch {
    public keysSwitch() {
      super("keys", "Lists all keys from the specified pool [pool]", 1, 1);
    }

    /**
     * Removes a key from the specified pool
     */
    @Override
    public boolean doAction(String[] args) {
      String string = "";
      for (String s : GlobalPools.AllGlobal.get(args[0]).keySet())
        string += s + Console.NL_INDENTED;

      Console.instructionWrite(string);
      return true;
    }
  }
}
