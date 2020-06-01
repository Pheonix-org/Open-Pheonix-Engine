package com.shinkson47.opex.backend.configuration

import com.shinkson47.opex.backend.errormanagement.EMSHelper
import com.shinkson47.opex.frontend.window.OPEXWindowHelper

object ConfigurationUtils {
    fun loadConfig(c: OPEXConfig?) {
        // EMS
        EMSHelper.setErrorTollerance(OPEXConfig.ErrorMargin)
        EMSHelper.setAllowEIS(OPEXConfig.AllowEIS)
        EMSHelper.setAllowErrNofif(OPEXConfig.AllowErrNotif)
        EMSHelper.setAllowCascadeDetection(OPEXConfig.AllowCascadeDetection)
        EMSHelper.setMillisTollerance(OPEXConfig.MillisTollerance)
        EMSHelper.setCascadeTollerance(OPEXConfig.CascadeTollerance)

        // Window Manager
        OPEXWindowHelper.setDefaultResolutionX(OPEXConfig.DefaultResolutionX)
        OPEXWindowHelper.setDefaultResolutionY(OPEXConfig.DefaultResolutionY)
    }
}