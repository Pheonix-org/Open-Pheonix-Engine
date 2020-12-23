package com.shinkson47.visual.fxml.controller;

import com.shinkson47.opex.frontend.fxml.FXMLMain;

/**
 * JVM Bootable entrypoint for
 */
public final class visual extends FXMLMain<Main>  {

    public visual() {
        super(Main.Main);
    }

    public static final String LOCATION_PREFIX = "../../../visual/fxml/";

    /**
     * Tasks to do BEFORE UX is created.
     */
    @Override
    protected void preLoad() {

    }

    /**
     * Tasks to be completed AFTER UX is created.
     */
    @Override
    protected void postLoad() {
        Main.Main = controller;
        controller.postLoad();
    }

    public static void _launch(){
        launch();
    }
}
