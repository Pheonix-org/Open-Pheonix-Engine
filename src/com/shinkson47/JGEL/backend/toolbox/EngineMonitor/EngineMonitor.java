package backend.toolbox.EngineMonitor;

import backend.runtime.console.JGELConsole;
import backend.runtime.hooking.JGELHook;
import backend.runtime.hooking.JGELHookUpdater;
import backend.runtime.threading.JGELRunnable;
import backend.runtime.threading.JGELThreadManager;
import backend.toolbox.JGELBackEndUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Console;

public class EngineMonitor extends JFrame implements JGELRunnable, JGELHook {

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
                    JGELBackEndUtils.ParseToSystemIn(ConsoleInput.getText());
                    ConsoleInput.setText("");
                }
            }
        });
    }

    public static void main(String[] args){
        JGELThreadManager.createThread(new EngineMonitor(), "EngineMonitorThread");
    }

    @Override
    public void stop() {
        this.dispose();
        JGELHookUpdater.deregisterUpdateHook("EngineMonitorHook");

    }

    @Override
    public void run() {
        this.setContentPane(panelMain);
        this.pack();
        this.setVisible(true);
        JGELHookUpdater.registerUpdateHook(this, "EngineMonitorHook");
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
