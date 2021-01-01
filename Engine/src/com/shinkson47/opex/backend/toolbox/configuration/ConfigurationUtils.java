package com.shinkson47.opex.backend.toolbox.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shinkson47.opex.backend.resources.pools.GlobalPools;
import com.shinkson47.opex.backend.runtime.errormanagement.EMSHelper;
import com.shinkson47.opex.backend.runtime.hooking.ConfigHook;
import com.shinkson47.opex.backend.runtime.hooking.OPEXBootHook;
import com.shinkson47.opex.backend.runtime.invokation.AutoInvoke;
import com.shinkson47.opex.frontend.window.OPEXWindowHelper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * <h1>OPEX's Configuration tools</h1>
 */
public final class ConfigurationUtils extends OPEXBootHook {

    /**
     * <h2>Keys for default OPEX Configuration keys</h2>
     */
    public static enum OPEX_CONFIG_KEYS {
        SPLASH_TIMEOUT,
        EMS_DUMP_STACK,
        EMS_ALLOW_CASCADE_DETECTION,
        EMS_ALLOW_ERROR_NOTIF,
        EMS_ERROR_TOLLERANCE,
        EMS_ALLOW_EIS,
        EMS_ERROR_MILLIS,
        THREAD_ASYNC_BLOCKING_Q,
        THREAD_ASYNC_KEEP_ALIVE,
        THREAD_ASYNC_CORE,
        THREAD_ASYNC_MAX,
        DEFAULT_RESOLUTION_X,
        DEFAULT_RESOLUTION_Y
    }

    /**
     * <h2>Boot hook to load config on boot</h2>
     */
    @Override
    public void BootHook() {
        LoadConfig("./rsc");
    }

    /**
     * <h2>Loads a congig from file</h2>
     * Replaces all values in {@link GlobalPools#CONFIG_POOL} with values found in the provided JSON.
     * @param s Directory or file to load. Must point to a folder that contains <b>config.json</b>, or end in <b>/config.json</b>.
     *          <blockquote>
     *          i.e LoadConfig("./rsc")
     *          OR
     *          LoadConfig("./rsc/config.json")
     *          </blockquote>
     */
    public static void LoadConfig(String s) {
        try {
            File in = new File(s +
                    ((s.endsWith("/config.json")) ? "" : "/config.json" ));

            GlobalPools.CONFIG_POOL.clear();
            GlobalPools.CONFIG_POOL.putAll(new ObjectMapper().readValue(in, HashMap.class));
        } catch (IOException e) {
            EMSHelper.handleException(e);
        }
        CheckConfig();
        AssertConfig();
    }

    /**
     * <h2>Searches for {@link OPEX_CONFIG_KEYS} presence within the loaded config</h2>
     */
    private static void CheckConfig() {
        for (OPEX_CONFIG_KEYS key : OPEX_CONFIG_KEYS.values())
            if (GlobalPools.CONFIG_POOL.get(key.toString()) == null) EMSHelper.warn("OPEX Config value '" + key.toString() + "' does not exist within the loaded config!");
    }

    /**
     * <h2>Tnvokes config hooks to load config values</h2>
     */
    public static void AssertConfig() {
        AutoInvoke.FindAndInvoke(ConfigHook.class);
    }

    public static String GetConfigVal(OPEX_CONFIG_KEYS key){
        return GetConfigVal(key.toString());
    }

    public static String GetConfigVal(String key){
        return GlobalPools.CONFIG_POOL.get(key);
    }
}