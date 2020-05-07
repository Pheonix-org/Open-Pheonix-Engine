package backend.runtime.console.instructions;

import backend.runtime.JGELEnvironmentUtils;
import backend.runtime.console.JGELConsole;
import backend.runtime.console.JGELConsoleInstruction;
import backend.runtime.threading.JGELThreadManager;
import backend.toolbox.EngineMonitor.EngineMonitor;

import java.lang.Override;
import java.lang.String;

public final class INSTEngine implements JGELConsoleInstruction {
  @Override
  public void parse() {
    switch (ConsoleOptions.valueOf(JGELConsole.getParamString("Switch to perform", ConsoleOptions.class))) {
      case halt:
        JGELEnvironmentUtils.shutdown();break;
      case monitor:
        JGELThreadManager.createThread(new EngineMonitor(), "EngineMonitorThread"); break;
    }
  }

  @Override
  public String name() {
    return "engine";
  }

  @Override
  public String briefHelp() {
    return "General JGEL engine tools";
  }

  @Override
  public void help() {
    JGELConsole.Write("Arguments:");
    JGELConsole.Write("halt - Halts the runtime environment");
    JGELConsole.Write("monitor - Launches the engine monitor");
  }

  private enum ConsoleOptions {
    halt,

    monitor
  }
}
