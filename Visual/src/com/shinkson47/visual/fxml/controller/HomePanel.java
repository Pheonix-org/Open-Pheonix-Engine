package com.shinkson47.visual.fxml.controller;


import com.shinkson47.opex.frontend.fxml.FXMLController;

public class HomePanel extends FXMLController {
    static final String PANEL_HOME_FXML = BootableVisual.LOCATION_PREFIX + "panels/HomePanel.fxml";

    public HomePanel(){
        super(PANEL_HOME_FXML);
    }
}
