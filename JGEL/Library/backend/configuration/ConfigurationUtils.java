package backend.configuration;

import backend.errormanagement.EMSHelper;
import frontend.windows.JGELWindowHelper;

public class ConfigurationUtils {

	@SuppressWarnings("static-access")
	public static void loadConfig(JGELConfig c) {
		//EMS
		EMSHelper.setErrorTollerance(c.ErrorMargin);
		EMSHelper.setAllowEIS(c.AllowEIS);
		EMSHelper.setAllowErrNofif(c.AllowErrNotif);
		EMSHelper.setAllowCascadeDetection(c.AllowCascadeDetection);
		EMSHelper.setMillisTollerance(c.MillisTollerance);
		EMSHelper.setCascadeTollerance(c.CascadeTollerance);
		
		//Window Manager
		JGELWindowHelper.setDefaultResolutionX(c.DefaultResolutionX);
		JGELWindowHelper.setDefaultResolutionY(c.DefaultResolutionY);
	}
	
}
