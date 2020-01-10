package com.Shinkson47.JGEL.BackEnd.Operation.Diagnostics;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.Shinkson47.JGEL.BackEnd.Configuration.Configuration;
import com.Shinkson47.JGEL.BackEnd.Operation.ErrorManagement.ErrorManager;
import com.Shinkson47.JGEL.BackEnd.Operation.ErrorManagement.Hypervisor;
import com.Shinkson47.JGEL.BackEnd.Updating.EventHook;
import com.Shinkson47.JGEL.BackEnd.Updating.HookUpdater;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JGELDebugger extends JFrame implements EventHook {
	private static final long serialVersionUID = 1L;
	private static JPanel contentPane = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JGELDebugger frame = new JGELDebugger();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public JGELDebugger() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				HookUpdater.DeRegisterStatic(JGELDebugger.class.getSimpleName());
				Logger.log("[@DDBG]");
			}
			@Override
			public void windowClosed(WindowEvent e) {
				contentPane = null;
			}
		});
		//TODO license check
		Logger.log("[@DBG] JGEL's internal debugger was opened.");
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1352, 892);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(5, 5, 1342, 22);
		contentPane.add(menuBar);
		
		mnupdwarn.setHorizontalAlignment(SwingConstants.CENTER);
		mnupdwarn.setForeground(Color.RED);
		menuBar.add(mnupdwarn);
		
		JMenuItem mntmManuallyUpdate = new JMenuItem(new AbstractAction("Manually update the debugger") {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				UpdateEvent();
		    }
		});
		mnupdwarn.add(mntmManuallyUpdate);
		
		JMenuItem mntmReaddUpdateHook = new JMenuItem(new AbstractAction("Re-add the event hook") {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				Reg();
		    }
		});
		mntmReaddUpdateHook.setText("Add event hook");
		
		mnupdwarn.add(mntmReaddUpdateHook);
		
		LogConsole.setEditable(false);
		LogConsole.setBounds(15, 39, 513, 822);
		LogConsole.setText(ConsoleLogText);
		contentPane.add(LogConsole);
		
		JTabbedPane ParentPane = new JTabbedPane(JTabbedPane.TOP);
		ParentPane.setBounds(540, 39, 806, 825);
		contentPane.add(ParentPane);
		
		JLayeredPane ApplicationPane = new JLayeredPane();
		ParentPane.addTab("Application", null, ApplicationPane, null);
		ApplicationPane.setLayout(null);
		
		JLabel lblStartTime = new JLabel("Start MilliTime: " + Configuration.StartTime);
		lblStartTime.setBounds(6, 6, 322, 16);
		ApplicationPane.add(lblStartTime);
		
		lblUpTime.setBounds(6, 34, 322, 16);
		ApplicationPane.add(lblUpTime);
		
		JLabel lblDeveloper = new JLabel("Client developer: " + Configuration.DeveloperName);
		lblDeveloper.setBounds(340, 6, 322, 16);
		ApplicationPane.add(lblDeveloper);
		
		JLabel lblClientName = new JLabel("Client name: " + Configuration.ClientName);
		lblClientName.setBounds(340, 34, 322, 16);
		ApplicationPane.add(lblClientName);
		
		JLabel lblClientVersion = new JLabel("Client version: " + String.valueOf(Configuration.ClientVersion));
		lblClientVersion.setBounds(340, 62, 322, 16);
		ApplicationPane.add(lblClientVersion);
		
		JLabel lblJGELVersion = new JLabel("JGEL version: " + Configuration.JGEL_VERSION);
		lblJGELVersion.setBounds(340, 90, 322, 16);
		ApplicationPane.add(lblJGELVersion);
		
		JPanel panel = new JPanel();
		panel.setBounds(6, 658, 281, 115);
		ApplicationPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblHalt = new JLabel("Halt");
		lblHalt.setHorizontalAlignment(SwingConstants.CENTER);
		lblHalt.setBounds(108, 5, 65, 16);
		panel.add(lblHalt);
		
		JButton btnShutdownClientVia = new JButton("Shutdown client via JGEL");
		btnShutdownClientVia.setBounds(40, 33, 198, 29);
		panel.add(btnShutdownClientVia);
		
		JButton btnForceExitVia = new JButton("Force exit via JRE");
		btnForceExitVia.setBounds(40, 69, 198, 29);
		panel.add(btnForceExitVia);
		btnForceExitVia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Hypervisor.ForceExit();
			}
		});
		btnShutdownClientVia.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Hypervisor.Shutdown();
			}
		});
		
		JLayeredPane HookPane = new JLayeredPane();
		ParentPane.addTab("Hooks", null, HookPane, null);
		HookPane.setLayout(null);
		
		JPanel EventHookPane = new JPanel();
		EventHookPane.setBounds(6, 6, 383, 382);
		HookPane.add(EventHookPane);
		EventHookPane.setLayout(null);
		
		JLabel lblHookTitle = new JLabel("Event Hooks");
		lblHookTitle.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblHookTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblHookTitle.setBounds(6, 6, 371, 16);
		EventHookPane.add(lblHookTitle);
		
		cmbRegisteredHooks.setBounds(6, 46, 184, 27);
		EventHookPane.add(cmbRegisteredHooks);
		
		JButton btnNewButton = new JButton("Deregister Hook");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HookUpdater.DeRegister(cmbRegisteredHooks.getSelectedIndex());
				UpdateEvent();
			}
		});
		btnNewButton.setBounds(193, 45, 184, 29);
		EventHookPane.add(btnNewButton);
		chckbxThreadAlive.setEnabled(false);
		
		chckbxThreadAlive.setBounds(6, 112, 128, 23);
		EventHookPane.add(chckbxThreadAlive);
		AutoRestart.setEnabled(false);
		
		AutoRestart.setBounds(6, 142, 128, 23);
		EventHookPane.add(AutoRestart);
		AutoUpdate.setEnabled(false);
		
		
		AutoUpdate.setBounds(6, 172, 128, 23);
		EventHookPane.add(AutoUpdate);
		
		lblLastKnownSize.setBounds(6, 291, 371, 16);
		EventHookPane.add(lblLastKnownSize);
		
		lblRegisterqueue.setBounds(6, 319, 371, 16);
		EventHookPane.add(lblRegisterqueue);
		
		lblDeregisterqueue.setBounds(6, 347, 371, 16);
		EventHookPane.add(lblDeregisterqueue);
		
		lblMsSinceLast.setBounds(6, 235, 371, 16);
		EventHookPane.add(lblMsSinceLast);
		
		lblLastLoopStart.setBounds(6, 207, 371, 16);
		EventHookPane.add(lblLastLoopStart);
		
		lblPeakMs.setBounds(6, 263, 371, 16);
		EventHookPane.add(lblPeakMs);
		
		JButton btnClsPeak = new JButton(new AbstractAction("↻") {
			@Override
			public void actionPerformed(ActionEvent e) {
				HookUpdater.PeakTime = 0L;
			}
		});
		btnClsPeak.setBounds(330, 258, 47, 29);
		EventHookPane.add(btnClsPeak);
		
		cmbDeactivatedHook.setBounds(6, 90, 184, 27);
		EventHookPane.add(cmbDeactivatedHook);
		
		JButton btnRestoreHook = new JButton(new AbstractAction("Restore Hook") {
			@Override
			public void actionPerformed(ActionEvent e) {
				HookUpdater.Restore(cmbDeactivatedHook.getSelectedIndex());
			}
		});
		btnRestoreHook.setBounds(193, 89, 184, 29);
		EventHookPane.add(btnRestoreHook);
		
		JLabel lblActiveHooks = new JLabel("Active Hooks");
		lblActiveHooks.setHorizontalAlignment(SwingConstants.CENTER);
		lblActiveHooks.setBounds(6, 33, 371, 16);
		EventHookPane.add(lblActiveHooks);
		
		JLabel lblDeactivatedHooks = new JLabel("Deactivated Hooks");
		lblDeactivatedHooks.setHorizontalAlignment(SwingConstants.CENTER);
		lblDeactivatedHooks.setBounds(6, 73, 371, 16);
		EventHookPane.add(lblDeactivatedHooks);
		
		JList listUpdateTime = new JList();
		listUpdateTime.setBounds(396, 374, 371, -274);
		EventHookPane.add(listUpdateTime);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBounds(6, 396, 383, 377);
		HookPane.add(panel_3);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBounds(396, 396, 383, 377);
		HookPane.add(panel_4);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(396, 6, 383, 382);
		HookPane.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblKeyboardHooker = new JLabel("Keyboard Hooker");
		lblKeyboardHooker.setBounds(6, 5, 371, 16);
		lblKeyboardHooker.setHorizontalAlignment(SwingConstants.CENTER);
		lblKeyboardHooker.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		panel_1.add(lblKeyboardHooker);
		
		JLabel lblLastEvent = new JLabel("Last event:");
		lblLastEvent.setBounds(6, 33, 371, 16);
		panel_1.add(lblLastEvent);
		
		JLabel lblTriggeredBy = new JLabel("Triggered by:");
		lblTriggeredBy.setBounds(6, 61, 371, 16);
		panel_1.add(lblTriggeredBy);
		
		JLabel lblCurrentConfiguration = new JLabel("Current Configuration:");
		lblCurrentConfiguration.setBounds(6, 89, 371, 16);
		panel_1.add(lblCurrentConfiguration);
		
		JButton btnNewButton_1 = new JButton("Open remapper");
		btnNewButton_1.setBounds(6, 347, 371, 29);
		panel_1.add(btnNewButton_1);
		
		JLayeredPane ErrorPane = new JLayeredPane();
		ErrorPane.setLayout(null);
		ParentPane.addTab("Error Management", null, ErrorPane, null);
		
		JButton btnNewButton_2 = new JButton("Throw random exception");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ErrorManager.PreWarn("The debugger is about to simulate a random crash; the following error state is synthetic!");
				Random rnd = new Random();
				ErrorManager.Error(rnd.nextInt(ErrorManager.ErrorMessages.length), null);
			}
		});
		btnNewButton_2.setBounds(6, 6, 208, 29);
		ErrorPane.add(btnNewButton_2);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("Error manager halts JGEL on exception");
		chckbxNewCheckBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ErrorManager.ExceptionsHalt = chckbxNewCheckBox.isSelected();
			}
		});
		chckbxNewCheckBox.setSelected(true);
		chckbxNewCheckBox.setBounds(6, 47, 358, 23);
		ErrorPane.add(chckbxNewCheckBox);
		
		JLayeredPane WindowsPane = new JLayeredPane();
		WindowsPane.setLayout(null);
		ParentPane.addTab("New tab", null, WindowsPane, null);
		
		Reg();
		
	}
	
	private void Reg() {
		HookUpdater.RegisterNewHook(this);
	}

	public static void New() {
		if (contentPane != null) return;
		JGELDebugger frame = new JGELDebugger();
		frame.setVisible(true);	
	}
	
	
	//Variables for interacting with the debug front end.
	private String ConsoleLogText = "Either there are not logs, or the code is broken. i'm guessing the latter.";
	JEditorPane LogConsole = new JEditorPane();
	
	private long upTime = 0L;
	JLabel lblUpTime = new JLabel("Up time: " + upTime);
	
	JComboBox<String> cmbRegisteredHooks = new JComboBox<String>();
	JMenu mnupdwarn = new JMenu("JGELDebugger is not being updated!");
	JLabel lblDeregisterqueue = new JLabel("Deregister Queue:");
	JLabel lblRegisterqueue = new JLabel("Register Queue:");
	JLabel lblLastKnownSize = new JLabel("Last known hook count: ");
	JCheckBox chckbxThreadAlive = new JCheckBox("Thread Alive");
	JCheckBox AutoUpdate = new JCheckBox("Auto Update");
	JCheckBox AutoRestart = new JCheckBox("Auto Restart");
	JLabel lblMsSinceLast = new JLabel("Ms since last loop start:");
	JLabel lblLastLoopStart = new JLabel("Last loop start time:");
	JLabel lblPeakMs = new JLabel("Peak Ms");
	JComboBox<String> cmbDeactivatedHook = new JComboBox<String>();
	
	/**
	 * Log buffer for updating the debug log display only when nesacerry.
	 */
	private static List<String> PreviousLog = new ArrayList<String>();
	
	/**
	 * Update all appro' window elements
	 */
	@Override
	public void UpdateEvent() {
		//Update the log window
			if (PreviousLog.size() < Logger.Logs.size()) {		//update only if there is new logs to display
				
				ConsoleLogText = "";						   	//Clear buffer
				for (int i = 0; i < Logger.Logs.size(); i++) { 	//Add log history to buffer
					PreviousLog.add(Logger.Logs.get(i));
					ConsoleLogText+= Logger.Logs.get(i) + System.lineSeparator();
				}
				LogConsole.setText(ConsoleLogText);				//display buffer
		}
				
		//Uptime		
		upTime = System.currentTimeMillis() - Configuration.StartTime;
		lblUpTime.setText("Up time: " + String.valueOf(upTime));
				
		//Update the Event updater tab
		//lblQueueLength.setText(String.valueOf(HookUpdater.getHookCount()));
		//lblRegisterQueue.setText(HookUpdater.getRegisterQueueName());
		
		//Event hook pane
		if (cmbRegisteredHooks.getItemCount() != HookUpdater.getHookCount()) {	//Update update hook list if there is a difference
			cmbRegisteredHooks.removeAllItems();								//Clear list
			for (Object hook : HookUpdater.GetAllHooks()) {
				cmbRegisteredHooks.addItem(hook.getClass().getSimpleName());	//Add every update hook to the cmb list
			}
		}
		if (cmbDeactivatedHook.getItemCount() != HookUpdater.ArchivedHooks.size()) {	//Update update hook list if there is a difference
			cmbDeactivatedHook.removeAllItems();								//Clear list
			for (Object hook : HookUpdater.ArchivedHooks) {
				cmbDeactivatedHook.addItem(hook.getClass().getSimpleName());	//Add every update hook to the cmb list
			}
		}
		lblDeregisterqueue.setText("Deregister Queue:" + HookUpdater.getRemoveQueueName());
		lblRegisterqueue.setText("Register Queue:" + HookUpdater.getQueueName());
		lblLastKnownSize.setText("Last known hook count: " + HookUpdater.getHookCount());
		lblMsSinceLast.setText("Ms since last loop start:" + String.valueOf(System.currentTimeMillis() - HookUpdater.LastLoopStart));
		lblLastLoopStart.setText("Last loop start time:" + String.valueOf(HookUpdater.LastLoopStart));
		lblPeakMs.setText("Peak Ms: " + String.valueOf(HookUpdater.PeakTime));
		
		if (Hypervisor.HookUpdaterThread != null) {
			chckbxThreadAlive.setSelected(Hypervisor.HookUpdaterThread.isAlive());
		} else {
			chckbxThreadAlive.setSelected(false);
		}
		
		AutoUpdate.setSelected(HookUpdater.DoAutoUpdate);
		AutoRestart.setSelected(HookUpdater.DoAutoRestart);
		
	}
	//SECTION JGEL Debugger functions

	@Override
	public void EnterUpdateEvent() {
		mnupdwarn.setVisible(false);
	}

	@Override
	public void ExitUpdateEvent() {
		mnupdwarn.setVisible(true);
	}
}
