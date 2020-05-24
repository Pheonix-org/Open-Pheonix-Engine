package com.shinkson47.OPEX.backend.runtime.console.instructions;

import com.shinkson47.OPEX.backend.runtime.engine.OPEX;
import com.shinkson47.OPEX.backend.runtime.console.OPEXConsole;
import com.shinkson47.OPEX.backend.runtime.console.OPEXConsoleInstruction;
import com.shinkson47.OPEX.backend.runtime.threading.OPEXThreadManager;
import com.shinkson47.OPEX.backend.toolbox.EngineMonitor.EngineMonitor;

import java.lang.Override;
import java.lang.String;

public final class INSTEngine implements OPEXConsoleInstruction {
  @Override
  public void parse() {
    switch (ConsoleOptions.valueOf(OPEXConsole.getParamString("Switch to perform", ConsoleOptions.class))) {
      case halt:
        OPEX.getEngineSuper().stop(); break;
      case monitor:
        OPEXThreadManager.createThread(new EngineMonitor(), "EngineMonitorThread"); break;
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
    halt,

    monitor
  }
}
