package backend.configuration;

import backend.errormanagement.EMSHelper;
import frontend.windows.JGELWindowHelper;

public class ConfigurationUtils {

	@SuppressWarnings("static-access")
	public static void loadConfig(JGELConfig c) {
		// EMS
		EMSHelper.setErrorTollerance(JGELConfig.ErrorMargin);
		EMSHelper.setAllowEIS(JGELConfig.AllowEIS);
		EMSHelper.setAllowErrNofif(JGELConfig.AllowErrNotif);
		EMSHelper.setAllowCascadeDetection(JGELConfig.AllowCascadeDetection);
		EMSHelper.setMillisTollerance(JGELConfig.MillisTollerance);
		EMSHelper.setCascadeTollerance(JGELConfig.CascadeTollerance);

		// Window Manager
		JGELWindowHelper.setDefaultResolutionX(JGELConfig.DefaultResolutionX);
		JGELWindowHelper.setDefaultResolutionY(JGELConfig.DefaultResolutionY);
	}

}
