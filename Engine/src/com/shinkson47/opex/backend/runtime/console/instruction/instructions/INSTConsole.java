package com.shinkson47.opex.backend.runtime.console.instruction.instructions;

import com.shinkson47.opex.backend.io.data.FilesHelper;
import com.shinkson47.opex.backend.resources.pools.GlobalPools;
import com.shinkson47.opex.backend.runtime.console.Console;
import com.shinkson47.opex.backend.runtime.console.instruction.Instruction;
import com.shinkson47.opex.backend.runtime.console.instruction.Switch;
import com.shinkson47.opex.backend.runtime.errormanagement.EMSHelper;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * <h1></h1>
 * <br>
 * <p>
 *
 * </p>
 *
 * @author <a href="https://www.shinkson47.in">Jordan T. Gray on 29/11/2020</a>
 * @version 1
 * @since v1
 */
public class INSTConsole extends Instruction {
    public static final class createSwitch extends Switch {
        public createSwitch(){
            super("create", "Creates an instruction class template. [name : String?]",0,1);
        }

        private static final String DEFAULT_PATH = "./Engine/src";

        /**
         * Creates console instruction templates using javapoet.
         * @param args the command line arguments parsed for this switch.
         * @return true once created.
         */
        @Override
        protected boolean doAction(String[] args) {
            //#region instruction meta
            final String InstructionName = (args.length > 0) ?                      // Set the name of the instruction. If no name was provided, prompt for one.
                    args[0]
                    :
                    Console.getParamString("What's the name of this instruction? i.e what system does it control?")
            ;

            if (InstructionName.equals("")) {
                Console.instructionWrite("Instruction name cannot be empty!");
                return false;
            }

            // Prompt for the description of the instruction.
            final String InstrcutionDescription = Console.getParamString("Enter a short description of this instruction. (Help Strings)");
            //#endregion instruction meta


            //#region switches
            final ArrayList<TypeSpec> switches = new ArrayList<>();

            while (true){                                                       // Until the user breaks with NULL,
                Console.instructionWrite(
                  renderSpec(InstructionName, InstrcutionDescription, switches)
                );

                final String name = Console.getParamString("Name for a new switch. 'default' for default switch,'null' to break when all switches are added.");

                //#region input validation
                if (name.equalsIgnoreCase("null"))      // If null,
                    if (switches.size() < 1) {                     // confirm break if no switches
                        if (Console.confirm("WARNING : This instruction does not yet have any switches! Are you sure you want to break?"))
                            break;
                        continue;
                    } else
                        break;
                else
                    if (checkExists(name, switches) && !Console.confirm("WARNING : There is switch with this name already, overwrite it?"))            // Check if already exists
                        continue;
                //#endregion


                int minargs;
                int maxargs;

                while (true) {
                    // Prompt for the metadata of this switch
                    minargs = Console.getParamInt("What's the MINIMUM number of arguments for this switch?");
                    maxargs = Console.getParamInt("What's the MAXIMUM number of arguments for this switch?");

                    if (minargs <= maxargs && minargs >= 0)
                        break;

                    Console.instructionWrite("Those aren't valid argument counts! Check polarity, and ensure that MAX > MIN.");
                }


                String help;
                do {
                    help = Console.getParamString("What does this switch do? (Help string) - Remember to document your parameters!");

                    // Test if args are permitted. If so, but there is no [param docs] prompt to confirm to continue. If they don't want to continue, break - otherwise try again.
                } while (maxargs != 0 &&
                        !help.matches(".*\\[.*\\].*") &&
                        !Console.confirm("This switch accepts parameters, but you help string does not appear to contain parameter documentation! Continue anyway?")
                );



                MethodSpec constructor = MethodSpec.constructorBuilder()        // Create the constructor.
                        .addStatement("super(" +
                                        (!(name.toUpperCase().equals(Switch.DEFAULT_SWITCH_NAME)) ?                      // If the default was not chosen,
                                        "\"" + name + "\""                                                               // use the name string.
                                                :
                                        "Switch.DEFAULT_SWITCH_NAME")                                                   // Otherwise reference the constant.
                                + ", \"" + help + "\", " + minargs +", " + maxargs + ")") // Parse meta to Switch super type.
                        .addModifiers(Modifier.PUBLIC)
                        .build();

                MethodSpec doAction = MethodSpec.constructorBuilder()            // Construct the doAction override.
                        .setName("doAction")
                        .returns(boolean.class)                                  // Returns a boolean.
                        .addParameter(String[].class, "args")              // Accepts a string array named 'args'
                        .addModifiers(Modifier.PUBLIC)                           // Is public
                        .addAnnotation(Override.class)                           // Is annotated with 'Override'
                        .addJavadoc(help)                                        // Prefill javadocs with the relevant help string.
                        .addStatement("// TODO implement " + name + "'s functionality.") // Add a developmental TODO note
                        .addStatement("return false")                            // Add a default return statement to avoid compilation error.
                        .build();

                switches.add(                                                     // Construct complete switch
                        TypeSpec.classBuilder(name + "Switch")              // Switch is called <name>Switch
                                .addJavadoc(help)                                 // Document with relevant help string
                                .superclass(Switch.class)                         // Is a subtype of Switch
                                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL) // Is public static final
                                .addMethod(constructor)                           // Add the constructor
                                .addMethod(doAction)                              // Add the action override
                                .build()
                );
            }
            //#endregion switches

            //#region constructor statement
            String constructorSuperStatement = "super("                               // parse super
                                                + "INST" + InstructionName + ".class, "
                                                + '\"' + InstructionName              // The instruction's name,
                                                +"\", \""
                                                + InstrcutionDescription + "\")";     // description,
            //#endregion constructor statement

            //#region class
            TypeSpec newInstruction = TypeSpec.classBuilder("INST" + InstructionName) // Create a class named INST<name> which
                    .addJavadoc(InstrcutionDescription)                                     // Has default javadocs populated with the help string,
                    .superclass(Instruction.class)                                          // Is a sub class of Instruction,
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)                          // Is public and final
                    .addTypes(switches)                                                     // Contains all of it's switches,
                    .addMethod(MethodSpec.constructorBuilder()                              // and contains a constructor which
                            .addStatement(constructorSuperStatement)                        // has the crucial super statement,
                            .addModifiers(Modifier.PUBLIC)                                  // and is public.
                            .build())
                    .build();

            // Define the output package based on user prompt.

            final String path = Console.getParamString("Path to sources root for generation. Leave empty for " + DEFAULT_PATH);
            File out = new File(
                    (path.equals("")) ?
                    DEFAULT_PATH
                    : (path.endsWith("/")) ?
                            path.substring(0, path.length()-1)
                            :
                            path
            );

            try {
                final String pkg = Console.getParamString("Target Package. I.e com.shinkson47.OPEX....instructions");

                JavaFile.builder(
                        (pkg.equals(""))?
                            "com.shinkson47.opex.backend.runtime.console.instruction.instructions"
                                :
                                pkg
                        ,
                        newInstruction
                ).build()                                                                    // Build final class
                        .writeTo(out);                                                       // Dump to file
                Console.instructionWrite("Instruction should've been written to disk. If the root path is correct, you should see the instruction in 'list' after a restart.");
            } catch (IOException e) {
                EMSHelper.handleException(e);
            }
            //#endregion class
            return true;
        }

        private String renderSpec(String instructionName, String instrcutionDescription, ArrayList<TypeSpec> switches) {
            String swtcs = "";
            for (TypeSpec s : switches)
                swtcs += Console.NL_INDENTED + s.name;

            return Console.barMessage(instructionName) +
                    Console.NL_INDENTED + instrcutionDescription +
                    Console.NL_INDENTED +
                    Console.NL_INDENTED + Console.barMessage("Switches") +
                    swtcs
                    + Console.NL_INDENTED;
        }

        private boolean checkExists(String name, ArrayList<TypeSpec> switches) {
            for (TypeSpec c : switches)
                if (c.name.equals(name + "Switch"))
                    return true;
            return false;
        }


    };

    public static final class getParamSwitch extends Switch {
        public getParamSwitch(){
            super("testparam", "Invokes Console#getParam[bool; string; int]. [Option : String!]",1, 1);
        }

        /**
         * Invokes Console#getParam...
         * @param args the command line arguments parsed for this switch.
         * @return true.
         */
        @Override
        protected boolean doAction(String[] args) {
            Console.instructionWrite(String.valueOf(Console.getSpecifiedValue(args[0], "test")));
            return true;
        }
    };

    public static final class serializeSwitch extends Switch {
        public serializeSwitch(){
            super("serialize", "Serializes an instruction from the pool to disk. [instruction name: String!, file path : String!]",2, 2);
        }
        /**
         * Invokes Console#getParam...
         * @param args the command line arguments parsed for this switch.
         * @return true.
         */
        @Override
        protected boolean doAction(String[] args) {
            if (args[0].equals("all")) {
                for (String key : GlobalPools.INSTRUCTION_POOL.keySet())
                    Console.parse("console serialize " + key + " " + args[1]);

                return true;
            }

            Instruction instruction = GlobalPools.INSTRUCTION_POOL.get(args[0]);
            if (instruction == null) {
                Console.instructionWrite(args[0] + " does not exist in the instruction pool!");
                return false;
            }

            final String path = (args[1].endsWith("/")) ? args[1] : args[1] + "/";
            final String name = path + instruction.getName() + ".inst";
            try {

                FilesHelper.writeOut(
                        new File(name),
                        instruction
                );
                Console.instructionWrite("Serialized " + instruction.getName() + " to " + name + "!");
            } catch (IOException e) {
                EMSHelper.handleException(e);
                Console.instructionWrite("Failed to serialize to disk!");
                return false;
            }

            return testSerialization(instruction.getName(), name);
        }

        private boolean testSerialization(String name, String filename) {
            Console.instructionWrite("Performing serialization auto-test, hang tight!");
            Console.parse("pool remove INSTRUCTION_POOL " + name);

            if (GlobalPools.INSTRUCTION_POOL.get(name) != null) {
                Console.instructionWrite("[TEST FAILED] Could not assert that the instruction was removed from the pool!");
                return false;
            }
            else
                Console.instructionWrite("Asserted that "+ name + " was deleted!");

            Console.parse("console deserialize " + filename);

            if (GlobalPools.INSTRUCTION_POOL.get(name) == null) {
                Console.instructionWrite("[TEST FAILED] Could not assert that the instruction was loaded from the disk!");
                return false;
            }
            else
                Console.instructionWrite("Asserted that "+ name + " was deserialized!");

            Console.instructionWrite("Performing an unmonitored execution of '" + name + "' : ");
            Console.instructionWrite(Console.NL_INDENTED + Console.barMessage("TEST EXECUTION") + Console.NL_INDENTED);
            Console.parse(name);
            Console.instructionWrite(Console.barMessage("EXECUTION END"));
            Console.instructionWrite("[TEST PASSED] Successfully deleted and deserialized!");
            return true;
        }
    };

    public static final class deserializeSwitch extends Switch {
        public deserializeSwitch(){
            super("deserialize", "Deserializes an instruction from the disk to pool. [path : String!]",1,1);
        }

         /**
         * Invokes Console#getParam...
         * @param args the command line arguments parsed for this switch.
         * @return true.
         */
        @Override
        protected boolean doAction(String[] args) {
            try {
                Instruction instruction = FilesHelper.deserialize(new File(args[0]), null);
                GlobalPools.INSTRUCTION_POOL.put(instruction.getName(), instruction);
            } catch (IOException | ClassNotFoundException e) {
                EMSHelper.handleException(e);
                return false;
            }
            return true;
        }
    };

    public static final class resetSwitch extends Switch {
        public resetSwitch(){
            super("reset", "Removes all instructions, and scans the environment for instructions to add them all again.",0,0);
        }
        /**
         * Invokes Console#getParam...
         * @param args the command line arguments parsed for this switch.
         * @return true.
         */
        @Override
        protected boolean doAction(String[] args) {
            GlobalPools.INSTRUCTION_POOL.clear();
            Console.loadConsoleInstructions();
            Console.instructionWrite(GlobalPools.INSTRUCTION_POOL.size() + " instructions loaded to pool.");
            return true;
        }
    };


    public INSTConsole() {
        super(INSTConsole.class, "console", "Controls for OPEX's console." +
                        Console.NL_INDENTED + "Use 'list' and 'help' to get started." +
                        Console.NL_INDENTED + "To learn more about the console, visit:" +
                        Console.NL_INDENTED + "https://github.com/Pheonix-org/Open-Pheonix-Engine/wiki/Command-Line-Interface");
    }
}
