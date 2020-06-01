package com.shinkson47.opex.backend.runtime.console.instructions;

import com.shinkson47.opex.backend.runtime.engine.OPEX;
import com.shinkson47.opex.backend.runtime.console.OPEXConsole;
import com.shinkson47.opex.backend.runtime.console.OPEXConsoleInstruction;

import java.lang.Override;
import java.lang.String;

public final class INSTEngine implements OPEXConsoleInstruction {
  @Override
  public void parse() {
    switch (ConsoleOptions.valueOf(OPEXConsole.getParamString("Switch to perform", ConsoleOptions.class))) {
      case halt:
        OPEX.getEngineSuper().stop(); break;
    }
  }

  @Override
  public String name() {
    return "engine";
  }

  @Override
  public String briefHelp() {
    return "General OPEX engine tools";
  }

  @Override
  public void help() {
    OPEXConsole.Write("Arguments:");
    OPEXConsole.Write("halt - Halts the runtime environment");
    OPEXConsole.Write("monitor - Launches the engine monitor");
  }

  private enum ConsoleOptions {
    halt
  }
}