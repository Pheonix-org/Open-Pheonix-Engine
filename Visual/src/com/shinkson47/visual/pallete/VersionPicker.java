package com.shinkson47.visual.pallete;

import com.shinkson47.opex.backend.toolbox.Version;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

public final class VersionPicker extends JFrame {
    private JPanel panelMain;
    private JSpinner yearSpinner;
    private JSpinner monthSpinner;
    private JSpinner daySpinner;
    private JButton btnCancel;
    private JButton btnOkay;
    private JTextField txtBuild;



    private Version version = new Version(-1, -1, -1, "X");

    private static final SpinnerModel YEAR_SPINNER_MODEL = new SpinnerNumberModel(Calendar.getInstance().get(1), 1970, Integer.MAX_VALUE, 1);

    private static final SpinnerModel MONTH_SPINNER_MODEL = new SpinnerNumberModel(Calendar.getInstance().get(2), 1, 12, 1);

    private static final SpinnerModel DAY_SPINNER_MODEL = new SpinnerNumberModel(Calendar.getInstance().get(5), 1, 32, 1);

    private final Version getVersion() {
        return this.version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public final void close() {
        if (this.isVisible()) {
            this.setVisible(false);
            this.dispose();
        }
    }

    public VersionPicker() {
            this.btnOkay.addActionListener(it -> {
                setVersion(
                        new Version(
                                Integer.parseInt((String) yearSpinner.getValue()),                                      // wtf is this casting lol
                                Integer.parseInt((String) monthSpinner.getValue()),                                     // (int)(string)(object)(string, that's actually an int)
                                Integer.parseInt((String) daySpinner.getValue()),                                       // what actual gobshite
                                txtBuild.getText()
                        )
                );

                close();
            });

        btnCancel.addActionListener(it -> VersionPicker.this.close());

        yearSpinner.setModel(YEAR_SPINNER_MODEL);
        monthSpinner.setModel(MONTH_SPINNER_MODEL);
        daySpinner.setModel(DAY_SPINNER_MODEL);

        this.setContentPane(panelMain);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
