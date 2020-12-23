package com.shinkson47.opex.backend.runtime.console.instruction.instructions;

import com.shinkson47.opex.backend.runtime.console.Console;
import com.shinkson47.opex.backend.runtime.console.instruction.Instruction;
import com.shinkson47.opex.backend.runtime.console.instruction.Switch;
import com.shinkson47.opex.backend.runtime.errormanagement.EMSHelper;

import java.lang.Override;
import java.lang.String;
import java.lang.reflect.Field;

/**
 * Operates over qualified classes
 */
public final class INSTreflect extends Instruction {
  public INSTreflect() {
    super(INSTreflect.class, "reflect", "Operates over qualified classes");
  }

  /**
   * Sets a primitive field to a specifed value
   */
  public static final class pokefieldSwitch extends Switch {
    public pokefieldSwitch() {
      super("poke", "Sets a primitive field to a prompted value [fully qualified class name : String!, field name : String!]", 2, 2);
    }

    /**
     * Sets a primitive field to a specifed value
     */
    @Override
    public boolean doAction(String[] args) {
      Class<?> c;
      try {
        c = Class.forName(args[0]);
      } catch (ClassNotFoundException e) {
        Console.instructionWrite(args[0] + " is not a valid or accessible fully qualified class name.");
        return false;
      }

      return Console.findAndSet(c, args[1]);
    }
  }


  public static final class peekfieldSwitch extends Switch {
    public peekfieldSwitch() {
      super("peek", "Views the data of a field.  [fully qualified class name : String!, field name : String!]", 2, 2);
    }

    @Override
    public boolean doAction(String[] args) {
      Class<?> c;
      try {
        c = Class.forName(args[0]);
      } catch (ClassNotFoundException e) {
        EMSHelper.handleException(e);
        Console.instructionWrite(args[0] + " is not a valid or accessible fully qualified class name.");
        return false;
      }

      Field f;

      try {
        f = c.getDeclaredField(args[1]);
      } catch (NoSuchFieldException e) {
        EMSHelper.handleException(e);
        Console.instructionWrite(args[1] + " is not a valid or accessible field in " + c.getSimpleName() + "!");
        return false;
      }

      f.setAccessible(true);

      try {
        Console.instructionWrite(String.valueOf(f.get(f)));
        return true;
      } catch (IllegalAccessException e) {
        EMSHelper.handleException(e);
        Console.instructionWrite("Unable to peek " + f.getName() + " in " + c.getSimpleName());
        return false;
      }
    }
  }
}
