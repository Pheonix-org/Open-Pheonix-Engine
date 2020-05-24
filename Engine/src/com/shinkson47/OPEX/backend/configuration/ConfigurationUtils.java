package com.shinkson47.OPEX.backend.configuration;

import com.shinkson47.OPEX.backend.errormanagement.EMSHelper;
import com.shinkson47.OPEX.frontend.windows.OPEXWindowHelper;

public class ConfigurationUtils {

	@SuppressWarnings("static-access")
	public static void loadConfig(OPEXConfig c) {
		// EMS
		EMSHelper.setErrorTollerance(OPEXConfig.ErrorMargin);
		EMSHelper.setAllowEIS(OPEXConfig.AllowEIS);
		EMSHelper.setAllowErrNofif(OPEXConfig.AllowErrNotif);
		EMSHelper.setAllowCascadeDetection(OPEXConfig.AllowCascadeDetection);
		EMSHelper.setMillisTollerance(OPEXConfig.MillisTollerance);
		EMSHelper.setCascadeTollerance(OPEXConfig.CascadeTollerance);

		// Window Manager
		OPEXWindowHelper.setDefaultResolutionX(OPEXConfig.DefaultResolutionX);
		OPEXWindowHelper.setDefaultResolutionY(OPEXConfig.DefaultResolutionY);
	}

}
