package com.Shinkson47.JGEL.BackEnd.Configuration.Defaults;

import com.Shinkson47.JGEL.BackEnd.General.GeneralTools;
import com.Shinkson47.JGEL.BackEnd.Input.KeyInputConfiguration;
import com.Shinkson47.JGEL.BackEnd.Operation.Diagnostics.JGELDebugger;
import com.Shinkson47.JGEL.BackEnd.Operation.ErrorManagement.ErrorManager;

public class DefaultKeyBindings extends KeyInputConfiguration{

	public DefaultKeyBindings() {
		try {
			super.BindPress(10, GeneralTools.getMethod(JGELDebugger.class, "New", null));
		} catch (SecurityException e) {
			ErrorManager.Error(16, e);
		}
	}
}
