package BackEnd.ErrorManagement;

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

import BackEnd.ErrorManagement.Exceptions.JGELGenericException;
import BackEnd.Events.Hooking.EventHook;

import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.FlowLayout;
import javax.swing.JSpinner;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 * Generalised internal debugging tool for JGEL contributors,
 * and developers using JGEL.
 * 
 * Can be accessed with the API Call 'Debug()'
 * 
 * @author gordie
 *
 */
public class JGELDebugger extends JFrame implements EventHook {
	private static final long serialVersionUID = 1L;
	private static JPanel contentPane = null;

	/**
	 * JGELDebugger runs externally to the runtime environment.
	 * This method invokes the JGEL Debbugging application to start.
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
				//HookUpdater.DeRegisterStatic(JGELDebugger.class.getSimpleName());
				//Logger.log("[@DDBG]");
			}
			@Override
			public void windowClosed(WindowEvent e) {
				contentPane = null;
			}
		});
		//TODO license check
		//Logger.log("[@DBG] JGEL's internal debugger was opened.");
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1218, 523);
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
		
		JTabbedPane ParentPane = new JTabbedPane(JTabbedPane.TOP);
		ParentPane.setBounds(5, 39, 1213, 464);
		contentPane.add(ParentPane);
		
		JLayeredPane EnvironmentPane = new JLayeredPane();
		ParentPane.addTab("Runtime Environment", null, EnvironmentPane, null);
		EnvironmentPane.setLayout(null);
		
		JLabel lblStartTime = new JLabel("Start MilliTime: ");
		lblStartTime.setBounds(6, 6, 322, 16);
		EnvironmentPane.add(lblStartTime);
		
		lblUpTime.setBounds(6, 34, 322, 16);
		EnvironmentPane.add(lblUpTime);
		
		JLabel lblDeveloper = new JLabel("Client developer: ");
		lblDeveloper.setBounds(340, 6, 322, 16);
		EnvironmentPane.add(lblDeveloper);
		
		JLabel lblClientName = new JLabel("Client name: ");
		lblClientName.setBounds(340, 34, 322, 16);
		EnvironmentPane.add(lblClientName);
		
		JLabel lblClientVersion = new JLabel("Client version: ");
		lblClientVersion.setBounds(340, 62, 322, 16);
		EnvironmentPane.add(lblClientVersion);
		
		JLabel lblJGELVersion = new JLabel("JGEL version: ");
		lblJGELVersion.setBounds(340, 90, 322, 16);
		EnvironmentPane.add(lblJGELVersion);
		
		JPanel panel = new JPanel();
		panel.setBounds(6, 297, 281, 115);
		EnvironmentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblHalt = new JLabel("Halt");
		lblHalt.setHorizontalAlignment(SwingConstants.CENTER);
		lblHalt.setBounds(108, 5, 65, 16);
		panel.add(lblHalt);
		
		JButton btnShutdownClientVia = new JButton("Shutdown client via JGEL");
		btnShutdownClientVia.setEnabled(false);
		btnShutdownClientVia.setBounds(40, 33, 198, 29);
		panel.add(btnShutdownClientVia);
		
		JButton btnForceExitVia = new JButton("Force exit via JRE");
		btnForceExitVia.setEnabled(false);
		btnForceExitVia.setBounds(40, 69, 198, 29);
		panel.add(btnForceExitVia);
		btnForceExitVia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		btnShutdownClientVia.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		JLayeredPane HookPane = new JLayeredPane();
		ParentPane.addTab("Event Hooks", null, HookPane, null);
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
				//HookUpdater.DeRegister(cmbRegisteredHooks.getSelectedIndex());
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
				//HookUpdater.PeakTime = 0L;
			}
		});
		btnClsPeak.setBounds(330, 258, 47, 29);
		EventHookPane.add(btnClsPeak);
		
		cmbDeactivatedHook.setBounds(6, 90, 184, 27);
		EventHookPane.add(cmbDeactivatedHook);
		
		JButton btnRestoreHook = new JButton(new AbstractAction("Restore Hook") {
			@Override
			public void actionPerformed(ActionEvent e) {
				//HookUpdater.Restore(cmbDeactivatedHook.getSelectedIndex());
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
		
		JPanel panel_4 = new JPanel();
		panel_4.setBounds(791, 11, 383, 377);
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
		
		JButton btnNewButton_2 = new JButton("Throw exception");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JGELEMS.HandleException(new JGELGenericException("Debug test exception."));
			}
		});
		btnNewButton_2.setBounds(6, 146, 293, 29);
		ErrorPane.add(btnNewButton_2);
		
		JCheckBox AllowEIS = new JCheckBox("Allow EIS");
		AllowEIS.setSelected(JGELEMS.getAllowEIS());
		AllowEIS.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JGELEMS.SetAllowEIS(AllowEIS.isSelected());
			}
		});
		AllowEIS.setBounds(6, 6, 358, 23);
		ErrorPane.add(AllowEIS);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(709, 6, 533, 463);
		ErrorPane.add(panel_2);
		panel_2.setLayout(null);
		
		JEditorPane editorPane = new JEditorPane();
		editorPane.setBounds(6, 46, 521, 361);
		panel_2.add(editorPane);
		editorPane.setText("Either there are not logs, or the code is broken. i'm guessing the latter.");
		editorPane.setEditable(false);
		
		JLabel lblLogging = new JLabel("Logging");
		lblLogging.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogging.setBounds(6, 18, 521, 16);
		panel_2.add(lblLogging);
		
		JButton btnWriteOutLog = new JButton("Write out log");
		btnWriteOutLog.setBounds(6, 428, 521, 29);
		panel_2.add(btnWriteOutLog);
		
		JEditorPane dtrpnThereAreNo = new JEditorPane();
		dtrpnThereAreNo.setEnabled(false);
		dtrpnThereAreNo.setText("There are no exceptions in EMS");
		dtrpnThereAreNo.setEditable(false);
		dtrpnThereAreNo.setBounds(309, 280, 388, 132);
		ErrorPane.add(dtrpnThereAreNo);
		
		JCheckBox ErrorNotif = new JCheckBox("Error Notifications");
		ErrorNotif.setSelected(JGELEMS.getErrNotif());
		ErrorNotif.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JGELEMS.SetAllowErrNofif(ErrorNotif.isSelected());
			}
		});
		ErrorNotif.setBounds(6, 41, 358, 23);
		ErrorPane.add(ErrorNotif);
		
		JSpinner ErrTollerance = new JSpinner();
		ErrTollerance.setValue(JGELEMS.getErrorTollerance());
		ErrTollerance.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				JGELEMS.SetErrorTollerance((int) ErrTollerance.getValue());
			}
		});
		ErrTollerance.setBounds(16, 76, 34, 26);
		ErrorPane.add(ErrTollerance);
		
		JLabel lblErrorTollerance = new JLabel("Error tollerance");
		lblErrorTollerance.setBounds(59, 81, 108, 16);
		ErrorPane.add(lblErrorTollerance);
		
		JLabel lblEmsMemory = new JLabel("EMS Memory");
		lblEmsMemory.setBounds(309, 252, 386, 16);
		ErrorPane.add(lblEmsMemory);
		
		JEditorPane dtrpnNoExceptionsHave = new JEditorPane();
		dtrpnNoExceptionsHave.setEnabled(false);
		dtrpnNoExceptionsHave.setText("No exceptions have been caught");
		dtrpnNoExceptionsHave.setEditable(false);
		dtrpnNoExceptionsHave.setBounds(6, 280, 293, 132);
		ErrorPane.add(dtrpnNoExceptionsHave);
		
		JLabel lblLogHistory = new JLabel("Exception Log history");
		lblLogHistory.setBounds(6, 252, 212, 16);
		ErrorPane.add(lblLogHistory);
		
		JLabel lblTolleranceRangeRemaining = new JLabel("Tollerance range remaining");
		lblTolleranceRangeRemaining.setBounds(6, 118, 177, 16);
		ErrorPane.add(lblTolleranceRangeRemaining);
		
		JButton btnClearEms = new JButton("Clear EMS");
		btnClearEms.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JGELEMS.ClearEMS();
			}
		});
		btnClearEms.setBounds(6, 187, 293, 29);
		ErrorPane.add(btnClearEms);
		
		JButton btnCauseEis = new JButton("Invoke EIS");
		btnCauseEis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JGELEMS.InvokeEIS();
			}
		});
		btnCauseEis.setBounds(6, 221, 293, 29);
		ErrorPane.add(btnCauseEis);
		
		JEditorPane editorPane_1 = new JEditorPane();
		editorPane_1.setText("There are no exceptions in EMS");
		editorPane_1.setEnabled(false);
		editorPane_1.setEditable(false);
		editorPane_1.setBounds(309, 34, 388, 206);
		ErrorPane.add(editorPane_1);
		
		JLabel lblWarnings = new JLabel("Warnings");
		lblWarnings.setBounds(309, 6, 386, 16);
		ErrorPane.add(lblWarnings);
		
		JLayeredPane WindowsPane = new JLayeredPane();
		ParentPane.addTab("JGEL API", null, WindowsPane, null);
		WindowsPane.setLayout(null);
		
		JEditorPane dtrpnNoCallsHave = new JEditorPane();
		dtrpnNoCallsHave.setText("No calls have been logged");
		dtrpnNoCallsHave.setEditable(false);
		dtrpnNoCallsHave.setBounds(6, 34, 521, 378);
		WindowsPane.add(dtrpnNoCallsHave);
		
		JLabel lblApiCallHistory = new JLabel("API Call history");
		lblApiCallHistory.setHorizontalAlignment(SwingConstants.CENTER);
		lblApiCallHistory.setBounds(6, 6, 521, 16);
		WindowsPane.add(lblApiCallHistory);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(539, 99, 175, 27);
		WindowsPane.add(comboBox);
		
		JButton btnNewButton_3 = new JButton("Invoke");
		btnNewButton_3.setBounds(539, 138, 175, 29);
		WindowsPane.add(btnNewButton_3);
		
		JLabel lblManualApiCall = new JLabel("Manual API Call");
		lblManualApiCall.setHorizontalAlignment(SwingConstants.CENTER);
		lblManualApiCall.setBounds(539, 71, 175, 16);
		WindowsPane.add(lblManualApiCall);
		
		JLabel lblApiCalls = new JLabel("API Call count: ");
		lblApiCalls.setBounds(539, 43, 175, 16);
		WindowsPane.add(lblApiCalls);
		
		Reg();
		
	}
	
	

	private void Reg() {
		//HookUpdater.RegisterNewHook(this);
	}

	public static void New() {
		if (contentPane != null) return;
		JGELDebugger frame = new JGELDebugger();
		frame.setVisible(true);	
	}
	
	
	//Variables for interacting with the debug front end.
	private String ConsoleLogText = "Either there are not logs, or the code is broken. i'm guessing the latter.";
	
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
//		//Update the log window
//			if (PreviousLog.size() < Logger.Logs.size()) {		//update only if there is new logs to display
//				
//				ConsoleLogText = "";						   	//Clear buffer
//				for (int i = 0; i < Logger.Logs.size(); i++) { 	//Add log history to buffer
//					PreviousLog.add(Logger.Logs.get(i));
//					ConsoleLogText+= Logger.Logs.get(i) + System.lineSeparator();
//				}
//				LogConsole.setText(ConsoleLogText);				//display buffer
//		}
//				
//		//Uptime		
//		upTime = System.currentTimeMillis() - Configuration.StartTime;
//		lblUpTime.setText("Up time: " + String.valueOf(upTime));
//				
//		//Update the Event updater tab
//		//lblQueueLength.setText(String.valueOf(HookUpdater.getHookCount()));
//		//lblRegisterQueue.setText(HookUpdater.getRegisterQueueName());
//		
//		//Event hook pane
//		if (cmbRegisteredHooks.getItemCount() != HookUpdater.getHookCount()) {	//Update update hook list if there is a difference
//			cmbRegisteredHooks.removeAllItems();								//Clear list
//			for (Object hook : HookUpdater.GetAllHooks()) {
//				cmbRegisteredHooks.addItem(hook.getClass().getSimpleName());	//Add every update hook to the cmb list
//			}
//		}
//		if (cmbDeactivatedHook.getItemCount() != HookUpdater.ArchivedHooks.size()) {	//Update update hook list if there is a difference
//			cmbDeactivatedHook.removeAllItems();								//Clear list
//			for (Object hook : HookUpdater.ArchivedHooks) {
//				cmbDeactivatedHook.addItem(hook.getClass().getSimpleName());	//Add every update hook to the cmb list
//			}
//		}
//		lblDeregisterqueue.setText("Deregister Queue:" + HookUpdater.getRemoveQueueName());
//		lblRegisterqueue.setText("Register Queue:" + HookUpdater.getQueueName());
//		lblLastKnownSize.setText("Last known hook count: " + HookUpdater.getHookCount());
//		lblMsSinceLast.setText("Ms since last loop start:" + String.valueOf(System.currentTimeMillis() - HookUpdater.LastLoopStart));
//		lblLastLoopStart.setText("Last loop start time:" + String.valueOf(HookUpdater.LastLoopStart));
//		lblPeakMs.setText("Peak Ms: " + String.valueOf(HookUpdater.PeakTime));
//		
//		if (Hypervisor.HookUpdaterThread != null) {
//			chckbxThreadAlive.setSelected(Hypervisor.HookUpdaterThread.isAlive());
//		} else {
//			chckbxThreadAlive.setSelected(false);
//		}
//		
//		AutoUpdate.setSelected(HookUpdater.DoAutoUpdate);
//		AutoRestart.setSelected(HookUpdater.DoAutoRestart);
//		
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
