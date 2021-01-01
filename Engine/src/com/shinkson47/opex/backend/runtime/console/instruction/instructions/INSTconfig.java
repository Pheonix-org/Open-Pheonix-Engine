package com.shinkson47.opex.backend.runtime.console.instruction.instructions;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinkson47.opex.backend.resources.pools.GlobalPools;
import com.shinkson47.opex.backend.resources.pools.Pool;
import com.shinkson47.opex.backend.runtime.console.Console;
import com.shinkson47.opex.backend.runtime.console.instruction.Instruction;
import com.shinkson47.opex.backend.runtime.console.instruction.Switch;
import com.shinkson47.opex.backend.runtime.errormanagement.EMSHelper;
import com.shinkson47.opex.backend.toolbox.configuration.ConfigurationUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;
import java.util.HashMap;

/**
 * controls OPEX's configuration values
 */
public final class INSTconfig extends Instruction {
  public INSTconfig() {
    super(INSTconfig.class, "config", "controls OPEX's configuration values");
  }

  /**
   * Prints all config keys, and thier current values
   */
  public static final class defaultSwitch extends Switch {
    public defaultSwitch() {
      super(Switch.DEFAULT_SWITCH_NAME, "Prints all config keys, and thier current values", 0, 0);
    }

    /**
     * Prints all config keys, and thier current values
     */
    @Override
    public boolean doAction(String[] args) {
      Console.parse("pool keys CONFIG_POOL true");
      return false;
    }
  }

  /**
   * creates the provided key [name: String!]
   */
  public static final class createSwitch extends Switch {
    public createSwitch() {
      super("create", "creates a config entry with the provided key and value [name: String!, value: String[<100]!]", 2, 100);
    }

    /**
     * creates the provided key [name: String!]
     */
    @Override
    public boolean doAction(String[] args) {
      GlobalPools.CONFIG_POOL.put(args[0], Console.tokesToString(Console.StripFirst(args)));
      return false;
    }
  }

  /**
   * writes the current state of configuration keys to config.json in the path provided [path: String!]
   */
  public static final class saveSwitch extends Switch {
    public saveSwitch() {
      super("save", "writes the current state of configuration keys to config.json in the path provided [path: String!]", 1, 1);
    }

    /**
     * writes the current state of configuration keys to config.json in the path provided [path: String!]
     */
    @Override
    public boolean doAction(String[] args) {
      try {
        ConfigurationUtils.Save(args[0]);
      } catch (IOException e) {
        EMSHelper.handleException(e);
      }
      //HashMap<String, String> config = new ObjectMapper().readValue(str, HashMap.class);
      return false;
    }
  }

  /**
   * writes the current state of configuration keys to config.json in the path provided [path: String!]
   */
  public static final class loadSwitch extends Switch {
    public loadSwitch() {
      super("load", "replaces loaded configuration from file [dir containing config.json: String!]", 1, 1);
    }

    /**
     * writes the current state of configuration keys to config.json in the path provided [path: String!]
     */
    @Override
    public boolean doAction(String[] args) {
      ConfigurationUtils.LoadConfig(args[0]);
      return true;
    }
  }
}
// TODO move these functions to a config class.
