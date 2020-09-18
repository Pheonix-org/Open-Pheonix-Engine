package com.shinkson47.visual.fxml.controller;

import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

public class HomePanel extends Panel {
    static final String PANEL_HOME_FXML = visual.LOCATION_PREFIX + "panels/Home.fxml";


    public HomePanel(){
        super(PANEL_HOME_FXML);
    }
}
