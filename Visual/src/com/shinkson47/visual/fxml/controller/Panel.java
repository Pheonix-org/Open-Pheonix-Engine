package com.shinkson47.visual.fxml.controller;

import com.shinkson47.opex.frontend.fxml.FXMLController;

public class Panel extends FXMLController {

    private String panelName = "";

    public Panel(String _FXML) {
        super(_FXML);
    }

    public Panel setPanelName(String name) {
        panelName = name;
        return this;
    }

    public String getPanelName(){
        return panelName;
    }
}
