package com.shinkson47.opex.backend.toolbox.configuration;

import com.shinkson47.opex.backend.runtime.errormanagement.EMSHelper;
import com.shinkson47.opex.frontend.window.OPEXWindowHelper;

public final class ConfigurationUtils {
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