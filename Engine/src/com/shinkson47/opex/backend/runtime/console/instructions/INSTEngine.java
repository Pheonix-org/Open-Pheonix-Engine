package com.shinkson47.opex.backend.runtime.console.instructions;

import com.shinkson47.opex.backend.runtime.entry.OPEX;
import com.shinkson47.opex.backend.runtime.console.Console;

import java.lang.Override;
import java.lang.String;

public final class INSTEngine implements IConsoleInstruction {
  @Override
  public void parse() {
    switch (ConsoleOptions.valueOf(Console.getParamString("Switch to perform", ConsoleOptions.class))) {
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
    Console.Write("Arguments:");
    Console.Write("halt - Halts the runtime environment");
    Console.Write("monitor - Launches the engine monitor");
  }

  private enum ConsoleOptions {
    halt
  }
}
