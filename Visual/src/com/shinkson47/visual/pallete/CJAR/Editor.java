package com.shinkson47.visual.pallete.CJAR;

import javax.swing.*;

public class Editor extends JPanel {
    private JPanel panelMain;
    private JPanel panelLeft;
    private JTree fileTree;
    private JCheckBox hasLoadScriptCheckBox;
    private JTextField txtFileCount;

    public Editor(){
        super.add(panelMain);
        super.setVisible(true);
    }

}
