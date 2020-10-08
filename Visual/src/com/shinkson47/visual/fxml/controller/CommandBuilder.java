package com.shinkson47.visual.fxml.controller;

import com.shinkson47.opex.backend.io.data.FilesHelper;
import com.shinkson47.opex.backend.runtime.console.instructions.IConsoleInstruction;
import com.shinkson47.opex.backend.runtime.errormanagement.EMSHelper;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CommandBuilder extends Panel {

    public static final String COMMAND_PANEL_FXML = visual.LOCATION_PREFIX + "panels/Commands.fmxl";

    private static Switch SelectedSwitch = null;

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
    public ListView listReviewImports;
    public Button btnRemoveImport;
    public Button btnAddImport;
    public TextField txtEditImport;
    public CheckBox chkCmd;

    /**
     * Instruction instance currently being worked on.
     */
    public static InstructionBase Instruction = new InstructionBase();

    //#region boilerplates
    /**
     * Storage container for switch data.
     */
    private static class Switch implements Serializable {
        public Switch(String Name, String Imports, String Script, String Description) {
            name = Name;
            imports = Imports;
            script = Script;
            description = Description;
        }

        String name;
        String description;
        String imports;
        String script;

        @Override
        public Switch clone() {
            return new Switch(name, imports, script, description);
        }
    }

    /**
     * Storage container for instruction data.
     */
    public static class InstructionBase implements Serializable {
        List<String> ImportLines = new ArrayList<>();
        List<String> HelpLines = new ArrayList<String>();
        ArrayList<Switch> switches = new ArrayList<>();
        String name;
        String BriefHelp;
        String pkg;

        void Reset() {
            switches.clear();
            ImportLines.clear();
            HelpLines.clear();

            name = null;
            BriefHelp = null;
            pkg = null;
        }
    }
    //#endregion

    public CommandBuilder() {
        super(COMMAND_PANEL_FXML);
    }

    //#region build
    /**
     * Clears all imports in <c>this#Instruction</c>,
     * then adds every import found on switches, ignoring duplicates.
     */
    private CommandBuilder BuildImports() {
        Instruction.ImportLines.clear();
        for (Switch sw : Instruction.switches) {                                                                        //       for every switch
            if (sw.imports.equals(""))                                                                                  //       skip switch if it has no imports
                continue;

            String[] splitImports = sw.imports.split(";");                                                        //        split switches imports into a list

            for (String line : splitImports) {                                                                          // FOR:   Every import,
                if (line.equals(""))                                                                                    //        Ignore blank lines. Also skips final extra line, if last line has a ;.
                    continue;

                // STRING CLEANING.
                line = line.replaceAll("\n", "");
                line = line.replaceAll(";", "");
                line += ";";

                // REDACTED (Retrospect: not sure why.)
                // Import REGEX.
//				if (!line.matches("\\bimport\\b (?:(([a-z]|[A-Z])+|\\.([a-z]|[A-Z])+|([a-z]|[A-Z])\\.\\*)+);")) {
//					ShowDialogue("import '" + line + "' is not formatted correctly and was skipped.");
//					continue;
//				}

                if (!Instruction.ImportLines.contains(line))                                                             //        If the import exists,
                    Instruction.ImportLines.add(line);                                                                   //        Add the import to the instruction. Otherwise, skip.
            }
        }
        UpdateReviewImports(); // Set built import list to the ui to edit.
        return this;
    }

    /**
     * Compiles help text, and populates the active instruction.
     */
    protected void BuildHelp() {
        Instruction.HelpLines.clear();
        for (Switch s : Instruction.switches) {
            Instruction.HelpLines.add(s.name + " - " + s.description);
        }
        UpdateReviewHelp();
    }
    //#endregion

    //#region UX Update
    /**
     * Updates all UX elements on the Imports tab
     * to match the active instruction.
     * TODO completely possible for cast exceptions here. Need to check.
     * TODO update edit box.
     */
    private CommandBuilder UpdateReviewImports() {
        int i = listReviewImports.getSelectionModel().getSelectedIndex();                                                //        Get current position.
        listReviewImports.getItems().clear();
        listReviewImports.setItems((ObservableList) Instruction.ImportLines);
        listReviewImports.getSelectionModel().select(i);                                                                 //         Restore selected position to make update seamless to the user.
        return this;
    }

    /**
     * TODO
     */
    private void UpdateReviewHelp() {

    }

    /**
     * Asserts <c>listSwitches</c> matches <c>Instruction#Switches</c>
     */
    private CommandBuilder RefreshSwitches() {
        listSwitches.setItems(FXCollections.observableArrayList(Instruction.switches));
        listSwitches.refresh();
        return this;
    }

    //#endregion

    //#region JavaFX Events

    //#endregion

    //#region misc
    /**
     * Creates and adds a new, blank, switch to the active instruction.
     */
    private CommandBuilder AddSwitch() {
        Instruction.switches.add(new Switch("New Switch", "", "", ""));                  //         Create new switch.
        SelectedSwitch = Instruction.switches.get(Instruction.switches.size() - 1);                                     //         Select it, for convenience to the user.
        RefreshSwitches();                                                                                              //         Render it.
        return this;
    }
    //#endregion


    /**
     * Uses JavaPoet to construct the output class.
     */
    protected void Compile() {
        // define builders
        // enum
        TypeSpec.Builder ConsoleOptionsBuilder = TypeSpec.enumBuilder("ConsoleOptions").addModifiers(Modifier.PRIVATE);

        // parse
        MethodSpec.Builder parseBuilder = MethodSpec.methodBuilder("parse").addAnnotation(Override.class)
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
        MethodSpec.Builder HelpBuilder = MethodSpec.methodBuilder("help").addAnnotation(Override.class)
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

        JavaFile javaFile = JavaFile.builder(txtCmdPackage.getText(), InstructionClass).build();

        try {
            Path p = Paths.get("./");
            javaFile.writeTo(p);


            if (chkCmd.isSelected()) {
                p = Paths.get("./" + txtCmdPackage.getText() + Instruction.name.substring(0, 1).toUpperCase() + Instruction.name.substring(1)
                        + ".OPEXcmd");
                FilesHelper.writeOut(p.toFile(), com.shinkson47.opex.backend.runtime.console.CommandBuilder.Instruction);
            }
        } catch (IOException e) {
            EMSHelper.handleException(e);
        }
    }
}
