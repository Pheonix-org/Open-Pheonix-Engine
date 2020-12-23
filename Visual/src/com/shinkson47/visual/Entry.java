package com.shinkson47.visual;

import com.shinkson47.opex.backend.runtime.environment.OPEX;
import com.shinkson47.opex.backend.runtime.errormanagement.EMSHelper;
import com.shinkson47.opex.backend.runtime.errormanagement.exceptions.OPEXStartFailure;
import com.shinkson47.opex.backend.runtime.threading.OPEXGame;
import com.shinkson47.opex.backend.toolbox.Version;
import com.shinkson47.visual.fxml.controller.visual;

public class Entry extends OPEXGame {

    @Override
    public Version version() {
        return new Version(2020, 9, 18, "WIP");
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args)  {
        try {
            new OPEX(new Entry());
        } catch (OPEXStartFailure e){
            EMSHelper.handleException(e);
        }
    }
    /**
     * Engine has called for a controlled shutdown of OPEX-V.
     */
    @Override
    public void stop() {

    }


    /**
     * OPEX Entry point - Start loading OPEX-V
     */
    @Override
    public void run() {
        visual._launch();
    }
}
