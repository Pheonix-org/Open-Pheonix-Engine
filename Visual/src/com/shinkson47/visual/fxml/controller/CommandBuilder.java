package com.shinkson47.visual.fxml.controller;

import javafx.scene.control.*;

public class CommandBuilder extends Panel {

    public TabPane CommandTabPane;
    public Button btdLoadCmd;
    public TextField txtCmdName;
    public TextField txtCmdPackage;
    public TextField txtBriefHelp;
    public ListView listSwitches;
    public TextField txtActiveSwitchName;
    public Button btnRemoveSwitch;
    public Button btnAddSwitch;
    public TextArea txtSwitchHelp;
    public TextArea txtSwitchImports;
    public TextArea txtSwitchPayload;
    public Button btnReviewSwitches;


    public CommandBuilder(String _FXML) {
        super(_FXML);
    }
}
