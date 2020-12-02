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
    public static final Switch createSwitch = new Switch("create", "Creates an instruction class template. [name : String?]",0,1){

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

            // Prompt for the description of the instruction.
            final String InstrcutionDescription = Console.getParamString("Enter a short description of this instruction. (Help Strings)");
            //#endregion instruction meta


            //#region switches
            final ArrayList<TypeSpec> switches = new ArrayList<>();

            while (true){                                                       // Until the user breaks with NULL,
                final String name = Console.getParamString("Name for a new switch. DEFAULT for default switch, NULL to break when all switches are added.");
                if (name.equals("NULL"))
                    break;
                                                                                // Prompt for the metadata of this switch
                final String help = Console.getParamString("What does this switch do? (Help string)");
                final int minargs = Console.getParamInt("What's the MINIMUM number of arguments for this switch?");
                final int maxargs = Console.getParamInt("What's the MAXIMUM number of arguments for this switch?");


                MethodSpec constructor = MethodSpec.constructorBuilder()        // Create the constructor.
                        .addStatement("super(\"" + name + "\", \"" + help + "\", " + minargs +", " + maxargs + ")") // Parse meta to Switch super type.
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
            String constructorSuperStatement = "super(\""                           // parse super
                                                + InstructionName                   // The instruction's name,
                                                +"\", \""
                                                + InstrcutionDescription + "\"";    // description,
            for (TypeSpec s : switches)
                constructorSuperStatement += ", new " + s.name + "()";              // And every switch the instruction has.
            constructorSuperStatement += ")";
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
            File out = new File(Console.getParamString("Path to sources root for generation. i.e ./Engine/src"));

            try {
                JavaFile.builder(
                        Console.getParamString("Target Package. I.e com.shinkson47.OPEX....instructions"),
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
    };

    public static final Switch getParamSwitch = new Switch("testparam", "Invokes Console#getParam[bool; string; int]. [Option : String!]",1, 1){
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

    public static final Switch serializeSwitch = new Switch("serialize", "Serializes an instruction from the pool to disk. [Pool name : String!, key : String!]",2, 2){
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

            try {
                FilesHelper.writeOut(
                        new File(args[1] + instruction.getName() + ".inst"),
                        instruction
                );
            } catch (IOException e) {
                EMSHelper.handleException(e);
                return false;
            }

            return true;
        }
    };

    public static final Switch deserializeSwitch = new Switch("deserialize", "Deerializes an instruction from the disk to pool. [path : String!]",1,1){
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

    public INSTConsole() {
        super("console", "Controls for OPEX's console.", deserializeSwitch, serializeSwitch, createSwitch, getParamSwitch);
    }
}
