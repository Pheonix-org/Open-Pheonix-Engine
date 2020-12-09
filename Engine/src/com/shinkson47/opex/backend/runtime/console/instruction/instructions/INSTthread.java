package com.shinkson47.opex.backend.runtime.console.instruction.instructions;
import com.shinkson47.opex.backend.runtime.console.Console;
import com.shinkson47.opex.backend.runtime.console.instruction.Instruction;
import com.shinkson47.opex.backend.runtime.console.instruction.Switch;
import com.shinkson47.opex.backend.runtime.threading.OPEXThread;
import com.shinkson47.opex.backend.runtime.threading.ThreadManager;

import java.lang.Override;
import java.lang.String;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Controls OPEX's thread manager
 */
public final class INSTthread extends Instruction {
  public INSTthread() {
    super(INSTthread.class, "thread", "Controls OPEX's thread manager", true);
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


  public static final class asyncSwitch extends Switch {
    public asyncSwitch (){
      super("async", "Displays info about the async thread pool that executes dispatched OPEXDispatchableEvents.",0,0);
    }

    /**
     * Invokes Console#getParam...
     * @param args the command line arguments parsed for this switch.
     * @return true.
     */
    @Override
    protected boolean doAction(String[] args) {
      ThreadPoolExecutor async = ThreadManager.getAsyncPool();

      Console.instructionWrite(
              Console.barMessage("async info (approx)")
                      + Console.NL_INDENTED + "Threads alive : " + async.getPoolSize()
                      + Console.NL_INDENTED + "Threads idle : " + (async.getPoolSize() - async.getActiveCount())
                      + Console.NL_INDENTED + "Threads active : " + async.getActiveCount()
                      + Console.NL_INDENTED
                      + Console.NL_INDENTED + "Total tasks dispatched : " + async.getTaskCount()
                      + Console.NL_INDENTED + "Total tasks completed : " + async.getCompletedTaskCount()
                      + Console.NL_INDENTED
                      + Console.NL_INDENTED + "Largest pool size : " + async.getLargestPoolSize()
                      + Console.NL_INDENTED + "Core pool size : " + async.getCorePoolSize()
                      + Console.NL_INDENTED + "Max pool size : " + async.getMaximumPoolSize()
      );
      return true;
    }
  };

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
