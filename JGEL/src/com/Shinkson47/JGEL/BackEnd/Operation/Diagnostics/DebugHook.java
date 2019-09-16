package com.Shinkson47.JGEL.BackEnd.Operation.Diagnostics;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import com.Shinkson47.JGEL.BackEnd.Configuration.Configuration;
import com.Shinkson47.JGEL.BackEnd.Operation.ErrorManagement.ErrorManager;
import com.Shinkson47.JGEL.BackEnd.Updating.EventHook;
import com.Shinkson47.JGEL.BackEnd.Updating.HookUpdater;
import com.Shinkson47.JGEL.FrontEnd.Window.WindowManager;
import java.awt.Font;

public class DebugHook implements EventHook, Runnable{
	public static DebugHook debugWindow;
	
	private JFrame frmJavaGamingEngine;
	private JEditorPane dtrpnLog = new JEditorPane();
	private JLabel lblSystemUptimeAt = new JLabel("System Uptime at test end:");
	private JButton btnTestLoopTime = new JButton("Test loop time");
	private JLabel lblAllUpdatesTake = new JLabel("All updates take:");
	private JLabel lblRemoveQueue = new JLabel("Deregister queue:");
	private JLabel lblHooksToUpdate = new JLabel("Hooks to update:");
	private JLabel lblHooksInQueue = new JLabel("Hooks in queue:");
	private JLabel lblRegisterQueue = new JLabel("Register queue:");
	private JCheckBox chkUpdate = new JCheckBox("Auto Update All?");
	private JCheckBox chkRestart = new JCheckBox("Auto Restart?");
	private JLabel lblQueueLength = new JLabel("");
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DebugHook window = new DebugHook();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public DebugHook() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		
		Logger.log("[JGEL] JGE Debug Hook opened!");
		
		frmJavaGamingEngine = new JFrame();
		frmJavaGamingEngine.setTitle("Java Gaming Engine Debug Hook");
		frmJavaGamingEngine.setResizable(false);
		frmJavaGamingEngine.setBounds(100, 100, 1106, 872);
		frmJavaGamingEngine.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmJavaGamingEngine.getContentPane().setLayout(null);

		frmJavaGamingEngine.setVisible(true);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 1106, 22);
		frmJavaGamingEngine.getContentPane().add(menuBar);
		
		JMenu mnAbout = new JMenu("About");
		menuBar.add(mnAbout);
		
		JMenuItem mmntAboutJgel = new JMenuItem("JGEL Version " + Configuration.JGEL_VERSION);
		mnAbout.add(mmntAboutJgel);
		
		JTabbedPane TabParent = new JTabbedPane(JTabbedPane.TOP);
		TabParent.setBounds(0, 22, 1100, 526);
		frmJavaGamingEngine.getContentPane().add(TabParent);
		
		JLayeredPane ApplcationTab = new JLayeredPane();
		TabParent.addTab("Application", null, ApplcationTab, null);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBounds(6, 6, 430, 468);
		ApplcationTab.add(panel_3);
		panel_3.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Java Gaming Engine");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(6, 6, 418, 16);
		panel_3.add(lblNewLabel_1);
		
		JLabel lblVersionText = new JLabel("Version");
		lblVersionText.setHorizontalAlignment(SwingConstants.LEFT);
		lblVersionText.setBounds(16, 34, 76, 16);
		panel_3.add(lblVersionText);
		
		JLabel lblUpTimeText = new JLabel("Up Time");
		lblUpTimeText.setHorizontalAlignment(SwingConstants.LEFT);
		lblUpTimeText.setBounds(16, 62, 110, 16);
		panel_3.add(lblUpTimeText);
		
		JLabel lblJGELVersion = new JLabel(String.valueOf(Configuration.JGEL_VERSION));
		lblJGELVersion.setHorizontalAlignment(SwingConstants.RIGHT);
		lblJGELVersion.setBounds(348, 34, 76, 16);
		panel_3.add(lblJGELVersion);
		
		JLabel label_4 = new JLabel("Not implementede");
		label_4.setHorizontalAlignment(SwingConstants.RIGHT);
		label_4.setBounds(301, 62, 123, 16);
		panel_3.add(label_4);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBounds(448, 6, 430, 474);
		ApplcationTab.add(panel_4);
		panel_4.setLayout(null);
		
		JLabel lblGamingClient = new JLabel("Game Client");
		lblGamingClient.setHorizontalAlignment(SwingConstants.CENTER);
		lblGamingClient.setBounds(6, 6, 418, 16);
		panel_4.add(lblGamingClient);
		
		JLabel lblNameText = new JLabel("Name");
		lblNameText.setHorizontalAlignment(SwingConstants.LEFT);
		lblNameText.setBounds(6, 34, 110, 16);
		panel_4.add(lblNameText);
		
		JLabel lblDeveloperText = new JLabel("Developer");
		lblDeveloperText.setHorizontalAlignment(SwingConstants.LEFT);
		lblDeveloperText.setBounds(6, 58, 110, 16);
		panel_4.add(lblDeveloperText);
		
		JLabel lblGameVersionText = new JLabel("Version");
		lblGameVersionText.setHorizontalAlignment(SwingConstants.LEFT);
		lblGameVersionText.setBounds(6, 84, 110, 16);
		panel_4.add(lblGameVersionText);
		
		JLabel lblUsesDrmText = new JLabel("Uses DRM");
		lblUsesDrmText.setHorizontalAlignment(SwingConstants.LEFT);
		lblUsesDrmText.setBounds(6, 112, 110, 16);
		panel_4.add(lblUsesDrmText);
		
		JLabel lblLiscenceText = new JLabel("Liscence");
		lblLiscenceText.setHorizontalAlignment(SwingConstants.LEFT);
		lblLiscenceText.setBounds(6, 141, 110, 16);
		panel_4.add(lblLiscenceText);
		
		JLabel lblGameName = new JLabel(Configuration.ClientName);
		lblGameName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblGameName.setBounds(177, 34, 247, 16);
		panel_4.add(lblGameName);
		
		JLabel lblGameDeveloper = new JLabel(Configuration.DeveloperName);
		lblGameDeveloper.setHorizontalAlignment(SwingConstants.RIGHT);
		lblGameDeveloper.setBounds(159, 58, 265, 16);
		panel_4.add(lblGameDeveloper);
		
		JLabel lblGameVersion = new JLabel(String.valueOf(Configuration.ClientVersion));
		lblGameVersion.setHorizontalAlignment(SwingConstants.RIGHT);
		lblGameVersion.setBounds(348, 84, 76, 16);
		panel_4.add(lblGameVersion);
		
		JLabel lblNotImplementsd = new JLabel("Not implemented");
		lblNotImplementsd.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNotImplementsd.setBounds(308, 112, 116, 16);
		panel_4.add(lblNotImplementsd);
		
		JLabel lblNotImplemented = new JLabel("Not implemented");
		lblNotImplemented.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNotImplemented.setBounds(266, 141, 158, 16);
		panel_4.add(lblNotImplemented);
		
		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBounds(890, 6, 183, 474);
		ApplcationTab.add(panel_2);
		
		JLabel lblActions = new JLabel("Actions");
		lblActions.setHorizontalAlignment(SwingConstants.CENTER);
		lblActions.setBounds(6, 6, 171, 16);
		panel_2.add(lblActions);
		
		JButton btnNewButton_1 = new JButton("Shutdown");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton_1.setBounds(6, 34, 171, 29);
		panel_2.add(btnNewButton_1);
		
		JButton btnForceExit = new JButton("Force Exit");
		btnForceExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(999);
			}
		});
		btnForceExit.setBounds(6, 63, 171, 29);
		panel_2.add(btnForceExit);
		
		JLayeredPane HypervisorPane = new JLayeredPane();
		TabParent.addTab("Hypervisor", null, HypervisorPane, null);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBounds(478, 6, 224, 195);
		HypervisorPane.add(panel_5);
		panel_5.setLayout(null);
		
		JLabel lblNewLabel_2 = new JLabel("Resources");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(6, 6, 212, 16);
		panel_5.add(lblNewLabel_2);
		
		JPanel panel_6 = new JPanel();
		panel_6.setLayout(null);
		panel_6.setBounds(6, 6, 224, 195);
		HypervisorPane.add(panel_6);
		
		JLabel lblHypervisor = new JLabel("Hypervisor");
		lblHypervisor.setHorizontalAlignment(SwingConstants.CENTER);
		lblHypervisor.setBounds(6, 6, 212, 16);
		panel_6.add(lblHypervisor);
		
		JPanel panel_7 = new JPanel();
		panel_7.setLayout(null);
		panel_7.setBounds(242, 6, 224, 195);
		HypervisorPane.add(panel_7);
		
		JLabel lblClient = new JLabel("Client");
		lblClient.setHorizontalAlignment(SwingConstants.CENTER);
		lblClient.setBounds(6, 6, 212, 16);
		panel_7.add(lblClient);
		
		JLayeredPane WindowManagerTab = new JLayeredPane();
		TabParent.addTab("Window Manager", null, WindowManagerTab, null);
		
		JPanel panel = new JPanel();
		panel.setBounds(6, 6, 437, 468);
		WindowManagerTab.add(panel);
		panel.setLayout(null);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(6, 34, 425, 27);
		panel.add(comboBox);
		
		JLabel lblNewLabel = new JLabel("Game Windows");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(6, 6, 425, 16);
		panel.add(lblNewLabel);
		
		JCheckBox chckbxIsVisible = new JCheckBox("Is Visible");
		chckbxIsVisible.setBounds(6, 85, 128, 23);
		panel.add(chckbxIsVisible);
		
		JLabel lblDisplayMode = new JLabel("Size:");
		lblDisplayMode.setBounds(16, 149, 118, 16);
		panel.add(lblDisplayMode);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setBounds(199, 117, 232, 27);
		panel.add(comboBox_1);
		
		JLabel label = new JLabel("Display Mode");
		label.setBounds(16, 121, 128, 16);
		panel.add(label);
		
		JLabel lblLocation = new JLabel("Location:");
		lblLocation.setBounds(16, 172, 118, 16);
		panel.add(lblLocation);
		
		JLabel lblXy = new JLabel("W0H0");
		lblXy.setBounds(209, 149, 118, 16);
		panel.add(lblXy);
		
		JLabel lblXy_1 = new JLabel("X0Y0");
		lblXy_1.setBounds(209, 172, 118, 16);
		panel.add(lblXy_1);
		
		JLabel lblAssignedWindow = new JLabel("Assigned Window:");
		lblAssignedWindow.setBounds(16, 200, 128, 16);
		panel.add(lblAssignedWindow);
		
		JLabel lblNull = new JLabel("Null");
		lblNull.setBounds(209, 200, 118, 16);
		panel.add(lblNull);
		
		JButton btnNewButton = new JButton("Close Game Window");
		btnNewButton.setBackground(Color.PINK);
		btnNewButton.setBounds(6, 435, 415, 27);
		panel.add(btnNewButton);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(455, 6, 618, 74);
		WindowManagerTab.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblNewWindow = new JLabel("Manage Windows");
		lblNewWindow.setBounds(6, 5, 606, 16);
		lblNewWindow.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lblNewWindow);
		
		JButton btnCloseAllWindows = new JButton("Close all windows");
		btnCloseAllWindows.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WindowManager.CloseAll();
			}
		});
		
		btnCloseAllWindows.setBackground(Color.PINK);
		btnCloseAllWindows.setBounds(6, 33, 606, 29);
		panel_1.add(btnCloseAllWindows);
		
		JInternalFrame GameWindowPreview = new JInternalFrame("Game Window Preview");
		GameWindowPreview.setBounds(455, 84, 618, 390);
		WindowManagerTab.add(GameWindowPreview);
		GameWindowPreview.setVisible(true);
		
		JLayeredPane ResourceManagerPane = new JLayeredPane();
		TabParent.addTab("\u0010Resource Manager", null, ResourceManagerPane, null);
		
		JPanel panel_9 = new JPanel();
		panel_9.setLayout(null);
		panel_9.setBounds(6, 6, 224, 195);
		ResourceManagerPane.add(panel_9);
		
		JLabel lblLoader = new JLabel("Loader");
		lblLoader.setHorizontalAlignment(SwingConstants.CENTER);
		lblLoader.setBounds(6, 6, 212, 16);
		panel_9.add(lblLoader);
		
		JPanel panel_10 = new JPanel();
		panel_10.setLayout(null);
		panel_10.setBounds(242, 6, 224, 195);
		ResourceManagerPane.add(panel_10);
		
		JLabel lblPool = new JLabel("Pool");
		lblPool.setHorizontalAlignment(SwingConstants.CENTER);
		lblPool.setBounds(6, 6, 212, 16);
		panel_10.add(lblPool);
		
		JLayeredPane EventHookPane = new JLayeredPane();
		TabParent.addTab("Hook Updater", null, EventHookPane, null);
		
		JPanel panel_11 = new JPanel();
		panel_11.setLayout(null);
		panel_11.setBounds(6, 6, 278, 327);
		EventHookPane.add(panel_11);
		
		JLabel lblUpdateLoop = new JLabel("Updater");
		lblUpdateLoop.setHorizontalAlignment(SwingConstants.CENTER);
		lblUpdateLoop.setBounds(6, 6, 266, 16);
		panel_11.add(lblUpdateLoop);
		
		btnTestLoopTime.setBounds(6, 34, 266, 29);
		panel_11.add(btnTestLoopTime);
		
		JLabel lblUseToFind = new JLabel("THIS WILL STOP THE EVENT UPDATER!");
		lblUseToFind.setForeground(Color.RED);
		lblUseToFind.setHorizontalAlignment(SwingConstants.CENTER);
		lblUseToFind.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		lblUseToFind.setBounds(6, 63, 266, 37);
		panel_11.add(lblUseToFind);
		
		JLabel label_1 = new JLabel("Use to find how long it takes to trigger all hooks.");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		label_1.setBounds(6, 49, 266, 37);
		panel_11.add(label_1);
		
		JLabel lblNewLabel_3 = new JLabel("System Uptime at test start:");
		lblNewLabel_3.setBounds(6, 95, 177, 16);
		panel_11.add(lblNewLabel_3);
		
		lblSystemUptimeAt.setBounds(6, 124, 177, 16);
		panel_11.add(lblSystemUptimeAt);
		

		lblAllUpdatesTake.setBounds(6, 139, 177, 16);
		panel_11.add(lblAllUpdatesTake);
		
		lblHooksToUpdate.setBounds(6, 111, 177, 16);
		panel_11.add(lblHooksToUpdate);
		
		JLabel lblUseTheApplication = new JLabel("Use the application tab to safely shutdown via JGE");
		lblUseTheApplication.setHorizontalAlignment(SwingConstants.CENTER);
		lblUseTheApplication.setForeground(Color.RED);
		lblUseTheApplication.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		lblUseTheApplication.setBounds(6, 152, 266, 29);
		panel_11.add(lblUseTheApplication);
		

		lblRemoveQueue.setBounds(6, 255, 154, 16);
		panel_11.add(lblRemoveQueue);
		
		chkRestart.setBounds(6, 272, 128, 23);
		panel_11.add(chkRestart);
		
		chkUpdate.setBounds(6, 298, 154, 23);
		panel_11.add(chkUpdate);
		
		lblRegisterQueue.setBounds(6, 235, 177, 16);
		panel_11.add(lblRegisterQueue);
		
		lblHooksInQueue.setBounds(6, 214, 177, 16);
		panel_11.add(lblHooksInQueue);
		
		JLabel lblDeregQueue = new JLabel("");
		lblDeregQueue.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDeregQueue.setBounds(172, 255, 100, 16);
		panel_11.add(lblDeregQueue);
		
		JLabel lblRegQueue = new JLabel("");
		lblRegQueue.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRegQueue.setBounds(172, 235, 100, 16);
		panel_11.add(lblRegQueue);
		
		lblQueueLength.setHorizontalAlignment(SwingConstants.RIGHT);
		lblQueueLength.setBounds(172, 214, 100, 16);
		panel_11.add(lblQueueLength);
		
		JPanel panel_12 = new JPanel();
		panel_12.setLayout(null);
		panel_12.setBounds(297, 6, 278, 327);
		EventHookPane.add(panel_12);
		
		JLabel lblHooks = new JLabel("Hooks");
		lblHooks.setHorizontalAlignment(SwingConstants.CENTER);
		lblHooks.setBounds(6, 6, 266, 16);
		panel_12.add(lblHooks);
		
		JLayeredPane KeyboardHookPane = new JLayeredPane();
		TabParent.addTab("Keyboard Hook", null, KeyboardHookPane, null);
		
		JLayeredPane MouseHookPane = new JLayeredPane();
		TabParent.addTab("Mouse Hook", null, MouseHookPane, null);
		
		JLayeredPane GamepadHookPane = new JLayeredPane();
		TabParent.addTab("Gamepad Hook", null, GamepadHookPane, null);
		
		JLayeredPane ErrorManagerPane = new JLayeredPane();
		TabParent.addTab("Error Manager", null, ErrorManagerPane, null);
		
		JButton btnThrowRandomError = new JButton("Throw random error");
		btnThrowRandomError.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Random rnd = new Random();
				ErrorManager.Error(rnd.nextInt(ErrorManager.ErrorMessages.length), null);
			}
		});
		
		btnThrowRandomError.setBounds(6, 6, 200, 29);
		ErrorManagerPane.add(btnThrowRandomError);
		
		JLayeredPane MiscPane = new JLayeredPane();
		TabParent.addTab("Misc", null, MiscPane, null);
		
		JPanel panel_8 = new JPanel();
		panel_8.setLayout(null);
		panel_8.setBounds(6, 6, 224, 195);
		MiscPane.add(panel_8);
		
		JLabel lblOpCodes = new JLabel("Op' Codes");
		lblOpCodes.setHorizontalAlignment(SwingConstants.CENTER);
		lblOpCodes.setBounds(6, 6, 212, 16);
		panel_8.add(lblOpCodes);
		
		dtrpnLog.setText("[Either there is no logs, or the code is broken. I'm guessing the latter.]");
		dtrpnLog.setBounds(10, 560, 1078, 274);
		frmJavaGamingEngine.getContentPane().add(dtrpnLog);
		
		HookUpdater.RegisterNewHook(this);
	}

	private static List<String> PreviousLog = new ArrayList<String>();
	@Override
	public void UpdateEvent() {
		//Update the log window
		if (PreviousLog.size() < Logger.Logs.size()) {
			String buffer = "";
			for (String prevline : PreviousLog) {
				buffer += prevline + System.lineSeparator();
			}
			
			for (int i = PreviousLog.size(); i < Logger.Logs.size(); i++) {
				PreviousLog.add(Logger.Logs.get(i));
				buffer += Logger.Logs.get(i) + System.lineSeparator();
			}
			dtrpnLog.setText(buffer);
		}
		
		//Update the Event updater tab
		lblQueueLength.setText(String.valueOf(HookUpdater.getHookCount()));
		lblRegisterQueue.setText(HookUpdater.getRegisterQueueName());
		
		//Update the uptime
		
	}

	
	@Override
	public void run() {
		debugWindow = new DebugHook();	
		debugWindow.initialize();
		while(true) {
			
		}
	}

	public static Thread DebugThread;
	public static void Show() {
		if (DebugThread != null) {
			if (DebugThread.isAlive()) {
				return;	
			}
		}
		
		DebugThread = new Thread(new DebugHook());	
		DebugThread.setName("Debug Thread");
		DebugThread.start();
	}
	
}
