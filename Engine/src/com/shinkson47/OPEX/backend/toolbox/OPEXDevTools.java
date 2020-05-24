package com.shinkson47.OPEX.backend.toolbox;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class OPEXDevTools {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					OPEXDevTools window = new OPEXDevTools();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public OPEXDevTools() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 784, 496);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JTabbedPane DevToolsPanes = new JTabbedPane(SwingConstants.TOP);
		DevToolsPanes.setBounds(10, 10, 772, 462);
		frame.getContentPane().add(DevToolsPanes);

		JPanel panel = new JPanel();
		DevToolsPanes.addTab("Thread Manager", null, panel, null);
		panel.setLayout(null);

		JLabel lblThreadCrumbheader = new JLabel("BackEnd.Threading.ThreadManager");
		lblThreadCrumbheader.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		lblThreadCrumbheader.setForeground(Color.GRAY);
		lblThreadCrumbheader.setBounds(6, 6, 739, 16);
		panel.add(lblThreadCrumbheader);

		JList listThreads = new JList();
		listThreads.setBounds(6, 50, 174, 150);
		panel.add(listThreads);

		JLabel lblRegisteredThreads = new JLabel("Registered Threads");
		lblRegisteredThreads.setHorizontalAlignment(SwingConstants.CENTER);
		lblRegisteredThreads.setForeground(Color.BLACK);
		lblRegisteredThreads.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		lblRegisteredThreads.setBounds(6, 34, 174, 16);
		panel.add(lblRegisteredThreads);

		JButton btnDeregisterThread = new JButton("Deregister Selected");
		btnDeregisterThread.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnDeregisterThread.setBounds(0, 203, 180, 29);
		panel.add(btnDeregisterThread);
	}
}
