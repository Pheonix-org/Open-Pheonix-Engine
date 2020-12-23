package com.shinkson47.opex.backend.runtime.console.instruction.instructions;

import java.lang.Override;
import java.lang.String;

public final class HelloWorld {
  HelloWorld() {
    super(test, this is just a test instructions, DEFAULTSwitch);
  }

  private static final class DEFAULTSwitch {
    DEFAULTSwitch() {
      super(DEFAULT, nothing, this is a test, 0, 0);
    }

    @Override
    private boolean doAction(String[] args) {
    }
  }
}
