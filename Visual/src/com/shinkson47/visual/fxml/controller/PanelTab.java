package com.shinkson47.visual.fxml.controller;

import com.shinkson47.opex.frontend.fxml.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PanelTab extends FXMLController {

    static final String PANEL_TAB_FXML = visual.LOCATION_PREFIX + "PanelTab.fxml";

    //#region FXML elements
    @FXML
    private Label txtName;
    //#endregion

    public PanelTab(){
        super(PANEL_TAB_FXML);
    }

    public PanelTab setText(String string){
        txtName.setText(string);
        return this;
    }

    @FXML
    public void select(){
        final Main parent = super.<Main>castParent();
        parent.selectPanel(
                Main.TABS.indexOf(this)
        );

    }
}
