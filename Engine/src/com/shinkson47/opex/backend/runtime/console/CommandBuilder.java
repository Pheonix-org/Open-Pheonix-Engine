package com.shinkson47.opex.backend.runtime.console;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.TextField;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.shinkson47.opex.backend.runtime.console.instruction.IConsoleInstruction;
import com.shinkson47.opex.backend.runtime.errormanagement.EMSHelper;
import com.shinkson47.opex.frontend.window.OPEXWindowHelper;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.TypeSpec;

import com.shinkson47.opex.backend.io.data.FilesHelper;

/**
 * GUI only tool for creating OPEX command java classes
 *
 *
 * 600 lines of spagetti. Please, don't look. It works, that's all that matters.
 * 
 * @author gordie
 *
 */
@SuppressWarnings("FieldCanBeLocal")
public class CommandBuilder {

	private JFrame frmOPEXCommandBuilder;
	private static JTextField txtName;
	public static JTextField txtBriefHelp;
	private static JTextField txtSwitchName;
	private static JButton btnRemoveSwitch;
	private static JButton btnReviewExport;
	private static JTextArea txtrSwitchImports = new JTextArea();
	private static JList<String> listSwitches = new JList<String>();
	private static JTextArea txtrSwitchScript = new JTextArea();
	private static JCheckBox chkCmd = new JCheckBox("Also generate .OPEXCMD");
	private static Switch SelectedSwitch = new Switch();
	private JLabel lblSwitchName;
	private JLabel lblScript;
	private JLabel lblName;
	private JTabbedPane tabbedPane;
	private JPanel panelExport;
	private JPanel panelHelp;
	private JTextArea textrDescription;
	private JLabel lblDescription;
	private JList listReview;
	private TextField txtReviewLine;
	private Button btnRemoveHelp;
	private Button btnHelpNext;

	private static class Switch implements Serializable {
		public Switch() {
		}

		public Switch(String Name, String Imports, String Script, String Description) {
			name = Name;
			imports = Imports;
			script = Script;
			description = Description;
		}

		public String name;
		public String description;
		public String imports;
		public String script;

		public Switch copyOf() {
			return new Switch(name, imports, script, description);
		}
	};

	public static class InstructionBase implements Serializable {
		public String name = "name";
		public String BriefHelp = "brief help";
		public ArrayList<Switch> switches = new ArrayList<Switch>();
		protected String pkg = "package ?;";

		public void Reset() {
			switches.clear();
			name = "";
		}
	}

	public static InstructionBase Instruction = new InstructionBase();

	/**
	 * Launch as standalone
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					CommandBuilder window = new CommandBuilder();
					window.frmOPEXCommandBuilder.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CommandBuilder() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frmOPEXCommandBuilder = new JFrame();
		frmOPEXCommandBuilder.setType(Type.UTILITY);
		frmOPEXCommandBuilder.setTitle("OPEX Command Builder");
		frmOPEXCommandBuilder.setResizable(false);
		frmOPEXCommandBuilder.setBounds(100, 100, 398, 552);
		frmOPEXCommandBuilder.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		tabbedPane = new JTabbedPane(SwingConstants.TOP);
		frmOPEXCommandBuilder.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		JPanel panelCreation = new JPanel();
		tabbedPane.addTab("Create", null, panelCreation, null);
		tabbedPane.setEnabledAt(0, true);
		panelCreation.setLayout(null);

		txtName = new JTextField();
		txtName.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				Instruction.name = txtName.getText().toLowerCase();
			}
		});
		txtName.setToolTipText(
				"What the users will type to activate this command. Not case sensitive. Single word is best practice.");
		txtName.setText("name");
		txtName.setBounds(6, 6, 130, 26);
		panelCreation.add(txtName);
		txtName.setColumns(10);

		txtBriefHelp = new JTextField();
		txtBriefHelp.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				CommandBuilder.Instruction.BriefHelp = CommandBuilder.txtBriefHelp.getText();
			}
		});
		txtBriefHelp.setToolTipText("A short overview of what this command is");
		txtBriefHelp.setText("Brief Help");
		txtBriefHelp.setBounds(6, 52, 351, 26);
		panelCreation.add(txtBriefHelp);
		txtBriefHelp.setColumns(10);
		listSwitches.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				listSwitches.grabFocus();
			}
		});
		listSwitches.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int i = listSwitches.getSelectedIndex();
				if (i == -1) {
					return;
				}

				SelectedSwitch = Instruction.switches.get(i);
				txtrSwitchImports.setText(SelectedSwitch.imports);
				txtSwitchName.setText(SelectedSwitch.name);
				txtrSwitchScript.setText(SelectedSwitch.script);
				textrDescription.setText(SelectedSwitch.description);
			}
		});

		listSwitches.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		listSwitches.setBounds(6, 90, 192, 135);
		panelCreation.add(listSwitches);

		JLabel lblSwitches = new JLabel("Switches");
		lblSwitches.setBounds(16, 75, 61, 16);
		panelCreation.add(lblSwitches);

		txtSwitchName = new JTextField();
		txtSwitchName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String str = txtSwitchName.getText().toLowerCase();
				SelectedSwitch.name = str;
				RenderSwitches();
			}
		});
		txtSwitchName.setText("No Switch");
		txtSwitchName.setBounds(210, 120, 134, 26);
		panelCreation.add(txtSwitchName);
		txtSwitchName.setColumns(10);

		JButton btnAddSwitch = new JButton("Add New Switch");
		btnAddSwitch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AddSwitch();
				selectLastSwitch();
			}
		});
		btnAddSwitch.setBounds(210, 196, 134, 29);
		panelCreation.add(btnAddSwitch);

		btnRemoveSwitch = new JButton("Remove Selected");
		btnRemoveSwitch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = listSwitches.getSelectedIndex();
				if (i == -1) {
					return;
				}
				Instruction.switches.remove(i);
				RenderSwitches();
				selectLastSwitch();
			}
		});

		btnRemoveSwitch.setBounds(210, 161, 134, 29);
		panelCreation.add(btnRemoveSwitch);

		btnReviewExport = new JButton("Review");
		btnReviewExport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!Instruction.pkg.matches("\\bpackage\\b ([a-z]|[A-Z]|[0-9]|\\.)+;")) {
					ShowDialogue("Package definition is not formatted correctly.");
					return;
				}
				BuildHelp();
				ReviewStage = 0;
				tabbedPane.setSelectedIndex(1);
			}
		});
		btnReviewExport.setBounds(1, 460, 356, 26);
		panelCreation.add(btnReviewExport);
		txtrSwitchImports.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				CommandBuilder.SelectedSwitch.imports = txtrSwitchImports.getText();
				RenderSwitches();
			}
		});
		txtrSwitchImports.setWrapStyleWord(true);
		txtrSwitchImports.setRows(10);
		txtrSwitchImports.setLineWrap(true);
		txtrSwitchImports.setText("No Switch");
		txtrSwitchImports.setBounds(8, 253, 349, 36);
		panelCreation.add(txtrSwitchImports);
		txtrSwitchScript.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				SelectedSwitch.script = txtrSwitchScript.getText();
				RenderSwitches();
			}
		});
		txtrSwitchScript.setLineWrap(true);
		txtrSwitchScript.setWrapStyleWord(true);
		txtrSwitchScript.setRows(100);
		txtrSwitchScript.setText("No Switch");
		txtrSwitchScript.setBounds(6, 383, 351, 75);
		panelCreation.add(txtrSwitchScript);

		lblSwitchName = new JLabel("Imports");
		lblSwitchName.setBounds(6, 237, 92, 16);
		panelCreation.add(lblSwitchName);

		lblScript = new JLabel("Script");
		lblScript.setBounds(6, 355, 92, 16);
		panelCreation.add(lblScript);

		lblName = new JLabel("Name");
		lblName.setBounds(211, 92, 92, 16);
		panelCreation.add(lblName);

		textrDescription = new JTextArea();
		textrDescription.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				SelectedSwitch.description = textrDescription.getText();
				RenderSwitches();
			}
		});
		textrDescription.setWrapStyleWord(true);
		textrDescription.setText("No Switch");
		textrDescription.setRows(10);
		textrDescription.setLineWrap(true);
		textrDescription.setBounds(6, 307, 349, 36);
		panelCreation.add(textrDescription);

		lblDescription = new JLabel("Description");
		lblDescription.setBounds(6, 291, 92, 16);
		panelCreation.add(lblDescription);

		txtPackage = new JTextField();
		txtPackage.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				Instruction.pkg = txtPackage.getText();
			}
		});
		txtPackage.setToolTipText("breadcrumb location of your instruction package.");
		txtPackage.setText("backend.runtime.console.instructions");
		txtPackage.setColumns(10);
		txtPackage.setBounds(6, 29, 351, 26);
		panelCreation.add(txtPackage);

		btnLoadOPEXcmd = new JButton("Load OPEXcmd");
		btnLoadOPEXcmd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("OPEX Instruction File", "OPEXcmd");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						Instruction = FilesHelper.<InstructionBase>deserialize(chooser.getSelectedFile(),
								Instruction);
					} catch (ClassNotFoundException | ClassCastException | IOException e1) {
						EMSHelper.handleException(e1);
					}
					RenderSwitches();
					txtName.setText(Instruction.name);
					txtBriefHelp.setText(Instruction.BriefHelp);
					txtPackage.setText(Instruction.pkg);
				}
			}
		});
		btnLoadOPEXcmd.setBounds(241, 6, 117, 29);
		panelCreation.add(btnLoadOPEXcmd);

		panelHelp = new JPanel();
		tabbedPane.addTab("Review", null, panelHelp, null);
		panelHelp.setLayout(null);

		Button btnAddHelp = new Button("Insert New Line");
		btnAddHelp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = listReview.getSelectedIndex();
				if (i == -1) {
					i = 0;
				}
				reviewWorkingList.add(i + 1, "New Line");
				UpdateReviewList();
				listReview.setSelectedIndex(i + 1);
			}
		});
		btnAddHelp.setBounds(10, 410, 179, 29);
		panelHelp.add(btnAddHelp);

		btnRemoveHelp = new Button("Delete Selected Line");
		btnRemoveHelp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				reviewWorkingList.remove(listReview.getSelectedIndex());
				UpdateReviewList();
			}
		});
		btnRemoveHelp.setBounds(195, 410, 179, 29);
		panelHelp.add(btnRemoveHelp);

		listReview = new JList();
		listReview.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int i = listReview.getSelectedIndex();
				if (i == -1) {
					return;
				}
				txtReviewLine.setText(reviewWorkingList.get(i));
			}
		});
		listReview.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		listReview.setBounds(10, 6, 361, 359);
		panelHelp.add(listReview);

		txtReviewLine = new TextField();
		txtReviewLine.addTextListener(new TextListener() {
			@Override
			public void textValueChanged(TextEvent e) {
				reviewWorkingList.set(listReview.getSelectedIndex(), txtReviewLine.getText());
				UpdateReviewList();
			}
		});
		txtReviewLine.setBackground(Color.WHITE);
		txtReviewLine.setBounds(10, 375, 361, 29);
		panelHelp.add(txtReviewLine);

		btnHelpNext = new Button("Next");
		btnHelpNext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switch (ReviewStage) {
				case 0:
					BuildImports();
					ReviewStage++;
					break;
				case 1:
					tabbedPane.setSelectedIndex(2);
					ReviewStage++;
					break;
				}

			}
		});
		btnHelpNext.setBounds(10, 451, 357, 29);
		panelHelp.add(btnHelpNext);
		tabbedPane.setEnabledAt(1, false);

		panelExport = new JPanel();
		tabbedPane.addTab("Export", null, panelExport, null);
		panelExport.setLayout(null);

		JButton btnExportjava = new JButton("Export .Java");
		btnExportjava.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BuildClass();

			}
		});
		btnExportjava.setBounds(6, 455, 365, 29);
		panelExport.add(btnExportjava);
		chkCmd.setSelected(true);

		chkCmd.setBounds(6, 425, 365, 23);
		panelExport.add(chkCmd);
		tabbedPane.setEnabledAt(2, false);
	}

	protected void BuildClass() {
		// define builders
		// enum
		TypeSpec.Builder ConsoleOptionsBuilder = TypeSpec.enumBuilder("ConsoleOptions").addModifiers(Modifier.PRIVATE);

		// parse
		Builder parseBuilder = MethodSpec.methodBuilder("parse").addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC).returns(void.class).addParameter(String[].class, "args")
				.beginControlFlow("switch (OPEXConsole.getParamString(\"Switch to perform\", ConsoleOptions.class))");

		// name
		MethodSpec NameMethod = MethodSpec.methodBuilder("name").addAnnotation(Override.class).returns(String.class)
				.addModifiers(Modifier.PUBLIC).addStatement("return $S", Instruction.name).build();

		// brief help
		MethodSpec BHelpMethod = MethodSpec.methodBuilder("briefHelp").addAnnotation(Override.class)
				.returns(String.class).addModifiers(Modifier.PUBLIC).addStatement("return $S", Instruction.BriefHelp)
				.build();

		// help
		Builder HelpBuilder = MethodSpec.methodBuilder("help").addAnnotation(Override.class)
				.addModifiers(Modifier.PUBLIC).addStatement("OPEXConsole.Write(\"Arguments:\")");

		// populate switches
		for (Switch s : Instruction.switches) {
			HelpBuilder.addStatement("OPEXConsole.Write(\"" + s.name + " - " + s.description + "\")");

			ConsoleOptionsBuilder.addEnumConstant(s.name);

			parseBuilder.addCode("case " + s.name + ":").addCode(s.script).addStatement("break");
		}
		parseBuilder.endControlFlow(); // End switch control flow now switches have been added

		// Compile specs from builders
		MethodSpec HelpMethod = HelpBuilder.build();
		TypeSpec consoleOptions = ConsoleOptionsBuilder.build();
		MethodSpec ParseMethod = parseBuilder.build();

		// define class
		TypeSpec InstructionClass = TypeSpec.classBuilder(Instruction.name)
				.addModifiers(Modifier.PUBLIC, Modifier.FINAL).addSuperinterface(IConsoleInstruction.class)
				.addType(consoleOptions).addMethod(ParseMethod).addMethod(NameMethod).addMethod(BHelpMethod)
				.addMethod(HelpMethod).build();

		JavaFile javaFile = JavaFile.builder(txtPackage.getText(), InstructionClass).build();

		try {
			Path p = Paths.get("./");
			javaFile.writeTo(p);
			

			if (chkCmd.isSelected()) {
				p = Paths.get("./" + txtPackage.getText() + Instruction.name.substring(0, 1).toUpperCase() + Instruction.name.substring(1)
						+ ".OPEXcmd");
				FilesHelper.writeOut(p.toFile(), CommandBuilder.Instruction);
			}
		} catch (IOException e) {
			ShowDialogue("Failed to write file .Java" + e.getMessage());
			e.printStackTrace();
		}

		ShowDialogue("Finished Exporting to ./!");

	}

	protected void selectLastSwitch() {
		listSwitches.setSelectedIndex(Instruction.switches.size() - 1);
	}

	private static void ShowDialogue(String t) {
		JOptionPane.showMessageDialog(OPEXWindowHelper.getSwingParent(), t);
	}

	public static void AddSwitch() {
		Instruction.switches.add(new Switch("New Switch", "", "", ""));
		SelectedSwitch = new Switch();
		RenderSwitches();
	}

	private static void RenderSwitches() {
		String[] s = new String[Instruction.switches.size()];

		int itt = 0;
		for (Switch sw : Instruction.switches) {
			s[itt] = sw.name;
			itt++;
		}
		listSwitches.setListData(s);
		listSwitches.updateUI();
	}

	protected List<String> reviewWorkingList;

	protected void reviewSetWorkingList(List<String> i) {
		reviewWorkingList = i;
		UpdateReviewList();
	}

	protected int ReviewStage = 0;
	protected List<String> HelpLines = new ArrayList<String>();

	protected void BuildHelp() {
		HelpLines.clear();
		for (Switch s : Instruction.switches) {
			HelpLines.add(s.name + " - " + s.description);
		}
		reviewSetWorkingList(HelpLines);
		UpdateReviewList();
	}

	@SuppressWarnings("unchecked")
	private void UpdateReviewList() {
		int i = listReview.getSelectedIndex();
		listReview.setListData(reviewWorkingList.toArray());
		listReview.setSelectedIndex(i);
	}

	protected List<String> ImportLines = new ArrayList<String>();
	private JTextField txtPackage;
	private JButton btnLoadOPEXcmd;

	protected void BuildImports() {
		for (Switch sw : Instruction.switches) { // for every switch

			if (sw.imports.equals("")) { // skip switch if it has no imports
				continue;
			}

			String[] splitimports = sw.imports.split(";"); // split switches imports into a list

			for (String setline : splitimports) {
				if (setline.equals("")) { // skip final extra line, if last line has a ;
					continue;
				}

				// Filter and clean

				setline = setline.replaceAll("\n", "");
				setline = setline.replaceAll(";", "");
				setline += ";";

//				if (!setline.matches("\\bimport\\b (?:(([a-z]|[A-Z])+|\\.([a-z]|[A-Z])+|([a-z]|[A-Z])\\.\\*)+);")) {
//					ShowDialogue("import '" + setline + "' is not formatted correctly and was skipped.");
//					continue;
//				}
//				;

				if (ImportLines.size() == 0) {
					ImportLines.add(setline);
					continue;
				}

				boolean add = true;// this ugly wart is disgusting but it's the only work around i can think of.
				for (String testline : ImportLines) { // Does import already exsist?
					if (testline.equals(setline)) {
						add = false; // Yes, skip import
						break;
					}
				}
				if (add) {
					ImportLines.add(setline); // No, add it
				}
			}
		}
		reviewSetWorkingList(ImportLines); // Set built import list to the ui to edit.
	}
}
