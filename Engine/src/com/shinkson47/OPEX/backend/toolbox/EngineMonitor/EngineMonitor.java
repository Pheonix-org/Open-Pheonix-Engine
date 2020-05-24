package com.shinkson47.OPEX.backend.toolbox.EngineMonitor;

import com.shinkson47.OPEX.backend.runtime.hooking.OPEXHook;
import com.shinkson47.OPEX.backend.runtime.hooking.OPEXHookUpdater;
import com.shinkson47.OPEX.backend.runtime.threading.IOPEXRunnable;
import com.shinkson47.OPEX.backend.runtime.threading.OPEXThreadManager;
import com.shinkson47.OPEX.backend.toolbox.OPEXBackEndUtils;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class EngineMonitor extends JFrame implements IOPEXRunnable, OPEXHook {

    private JPanel panelMain;
    private JTabbedPane tabbedPane1;
    private DefaultListModel<String> ConsoleOutputModel = new DefaultListModel<String>();
    private JList ConsoleOutput = new JList(ConsoleOutputModel);
    private JTextField ConsoleInput;

    public EngineMonitor() {
        ConsoleInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    OPEXBackEndUtils.ParseToSystemIn(ConsoleInput.getText());
                    ConsoleInput.setText("");
                }
            }
        });
    }

    public static void main(String[] args){
        OPEXThreadManager.createThread(new EngineMonitor(), "EngineMonitorThread");
    }

    @Override
    public void stop() {
        this.dispose();
        OPEXHookUpdater.deregisterUpdateHook("EngineMonitorHook");

    }

    @Override
    public void run() {
        this.setContentPane(panelMain);
        this.pack();
        this.setVisible(true);
        OPEXHookUpdater.registerUpdateHook(this, "EngineMonitorHook");
    }

    @Override
    public void enterUpdateEvent() {

    }

    @Override
    public void updateEvent() {

    }

    @Override
    public void exitUpdateEvent() {

    }
}
