package BackEnd.Configuration;

import BackEnd.ErrorManagement.JGELEMS;

public class ConfigurationInterpreter {

	@SuppressWarnings("static-access")
	public static void LoadConfig(JGELConfig c) {
		
		//EMS
		JGELEMS.SetErrorTollerance(c.ErrorMargin);
		JGELEMS.SetAllowEIS(c.AllowEIS);
		JGELEMS.SetAllowErrNofif(c.AllowErrNotif);
		JGELEMS.SetAllowCascadeDetection(c.AllowCascadeDetection);
		JGELEMS.setMillisTollerance(c.MillisTollerance);
		JGELEMS.setCascadeTollerance(c.CascadeTollerance);
	}
	
}
