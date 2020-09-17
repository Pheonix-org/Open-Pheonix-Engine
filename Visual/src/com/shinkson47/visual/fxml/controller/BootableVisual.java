package com.shinkson47.visual.fxml.controller;

import com.shinkson47.opex.backend.runtime.environment.OPEX;
import com.shinkson47.opex.backend.runtime.errormanagement.EMSHelper;
import com.shinkson47.opex.backend.runtime.errormanagement.exceptions.OPEXStartFailure;
import com.shinkson47.opex.backend.runtime.threading.OPEXGame;
import com.shinkson47.opex.backend.toolbox.Version;
import com.shinkson47.opex.frontend.fxml.FXMLController;
import com.shinkson47.opex.frontend.fxml.FXMLMain;

import java.io.IOException;

/**
 *
 */
public class BootableVisual extends FXMLMain<Main> {

    public static final String LOCATION_PREFIX = "../../../visual/fxml/";

    /**
     * @param _dummyController Template controller to copy when creating the application.
     */
    public BootableVisual() {
        super(new Main());
    }

    @Override
    public void stop() {

    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void preLoad() {
        
    }

    @Override
    protected void postLoad() {

    }
}
