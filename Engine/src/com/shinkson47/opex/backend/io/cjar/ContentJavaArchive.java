package com.shinkson47.opex.backend.io.cjar;

import com.shinkson47.opex.backend.io.data.FilesHelper;
import com.shinkson47.opex.backend.runtime.environment.OPEX;
import com.shinkson47.opex.backend.runtime.environment.OPEXPresentation;
import com.shinkson47.opex.backend.runtime.errormanagement.EMSHelper;

import javax.swing.*;
import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Representation of a .cjar archive stored on a disk.
 *
 * Provides tools for creating, modifying and accessing files within
 * exported archived.
 * <br><br>
 * <p>For information on .cjar's, see the OPEX Developers Technical Manual
 * privided in the SDK.</p>
 *
 * @since 2020.7.13.A
 * @version 1.1
 * @author <a href="https://www.shinkson47.in">Jordan T. Gray</a>
 */
public final class ContentJavaArchive {

    /**
     * Used locally to prompt for input.
     */
    private static final JFileChooser chooser = createChooser();


    //#region package locations
    public static final String PKG_META = "/META-INF";

    public static final String PKG_ASSETS = "/ASSETS";
    public static final String PKG_LIBRARY = "/LIBRARY";
    public static final String PKG_CUSTOM = "/CUSTOM";

    public static final String PKG_AUDIO =            PKG_ASSETS + "/AUDIO";
    public static final String PKG_AUDIO_AMBIANCE =   PKG_AUDIO  + "/AMBIANCE";
    public static final String PKG_AUDIO_DIALOG =     PKG_AUDIO  + "/DIALOG";
    public static final String PKG_AUDIO_SFX =        PKG_AUDIO  + "/SFX";
    public static final String PKG_AUDIO_SOUNDTRACK = PKG_AUDIO  + "/SOUNDTRACK";

    public static final String PKG_FLOWS =            PKG_ASSETS + "/FLOWS";
    public static final String PKG_FLOWS_QUEST =      PKG_FLOWS  + "/QUEST";
    public static final String PKG_FLOWS_STORY =      PKG_FLOWS  + "/STORY";

    public static final String PKG_GFX =              PKG_ASSETS + "/GRAPHICAL";
    public static final String PKG_GFX_BG =           PKG_GFX    + "/BACKGROUND";
    public static final String PKG_GFX_GUI =          PKG_GFX    + "/GUI";
    public static final String PKG_GFX_MODEL =        PKG_GFX    + "/MODEL";
    public static final String PKG_GFX_SPRITE =       PKG_GFX    + "/SPRITE";
    public static final String PKG_GFX_TILE =         PKG_GFX    + "/TILE";

    public static final String PKG_HASH =             PKG_ASSETS + "/HASH";
    public static final String PKG_HASH_LANG =        PKG_HASH   + "/LANGUAGE";

    public static final String PKG_LIBRARY_COMMAND =  PKG_LIBRARY + "/COMMAND";
    public static final String PKG_LIBRARY_HOOK =     PKG_LIBRARY + "/HOOK";
    public static final String PKG_LIBRARY_THREAD =   PKG_LIBRARY + "/THREAD";

    /**
     * Contains cjar relative paths to all packages expected to be found inside a cjar file structure.
     */
    public static final ArrayList<String> ALL_CJAR_PACKAGES = new ArrayList<String>(Arrays.asList(
            PKG_META,
            PKG_ASSETS,
            PKG_LIBRARY,

            PKG_AUDIO,
            PKG_AUDIO_AMBIANCE,
            PKG_AUDIO_DIALOG,
            PKG_AUDIO_SFX,
            PKG_AUDIO_SOUNDTRACK,

            PKG_FLOWS,
            PKG_FLOWS_QUEST,
            PKG_FLOWS_STORY,

            PKG_GFX,
            PKG_GFX_BG,
            PKG_GFX_GUI,
            PKG_GFX_MODEL,
            PKG_GFX_SPRITE,
            PKG_GFX_TILE,

            PKG_LIBRARY_COMMAND,
            PKG_LIBRARY_HOOK,
            PKG_LIBRARY_THREAD,

            PKG_HASH,
            PKG_HASH_LANG,
            PKG_CUSTOM
    ));
    //#endregion

    //#region file locations
    public static final String CJAR_META_DATA =        PKG_META   + "/meta.cjar";
    public static final String CJAR_LOADSCRIPT =       PKG_META   + "/loadscript.ls";
    //#endregion

    //#region properties, getters, setters
    /**
     * When instantiated, this contains the location of an exported CJAR.
     */
    private File fileOnDisk = null;

    /**
     * Gets the reference to file this instance has loaded.
     * @return this.fileOnDisk.
     */
    public File getFileOnDisk() { return fileOnDisk; }
    //#endregion

    //#region constructor
    /**
     * Creates an instance loaded from an exported .cjar file, as determined by <i>location</i>
     * @param location Location of the .CJAR file to load.
     * @throws FileNotFoundException if the path provided cannot be found.
     */
    public ContentJavaArchive(Path location) throws IOException {
        this(new File(location.toString()));
    }

    /**
     * Creates an instance loaded from an exported .cjar file, as determined by <i>location</i>
     * @param file File representing a .cjar on disk.-
     * @throws FileNotFoundException if the path provided cannot be found.
     */
    public ContentJavaArchive(File file) throws IOException {
        loadLocal(file);
    }
    //#endregion

    //#region Load
    public static ContentJavaArchive Load(File file) throws IOException {
        return new ContentJavaArchive(file);
    }

    /**
     * A Local method that populates this instance from a file on disk.
     *
     * Validates archive, then loads content archive from file
     * @throws FileNotFoundException if the file does not extends .cjar or .jar, OR is a directory OR is does not exist.
     */
    private void loadLocal(File file) throws IOException {
        // Assert is a valid file.
        if (!file.exists() || file.isDirectory()) throw new IOException("Path provided does not point to a file.");

        // validate is CJAR
        String fileExtention = FilesHelper.getFileExtension(file);
        if (!(fileExtention.equals(".cjar") || fileExtention.equals(".jar")))
            throw new IOException("File provided is not extended with .jar or .cjar");

        fileOnDisk = file;
        // TODO Load contents
    }
    //#endregion

    //#region CJAR Factory

    /**
     * Extends API Call to create a new CJAR, with an unknown name / location.
     * @apiNote This call implements in-line UI prompts to gather the missing fields.
     */
    public static void createCJAR(){
        String input = JOptionPane.showInputDialog(null, "CJAR Name");                          // Prompt the user for the name of the package
        input = FilesHelper.cleanPath(input);                                                                           // Clean user input
        if(FilesHelper.isEmpty(input)){                                                                                 // Check for no valid input
            WarnCreateIgnore("User did not provide a name.");
            return;
        }
        createCJAR(input);
    }


    /**
     * Extends API Call to create a new CJAR, with an unknown location.
     * @apiNote This call implements an in-line UI prompt to gather the missing location
     */
    public static void createCJAR(String name){
        chooser.showOpenDialog(null);                                                                            // Prompt user for target parent
        File selectedDir = chooser.getSelectedFile();                                                                   // get target parent
        if (selectedDir != null)
            createCJAR(selectedDir.toPath(), name, true);
        else
            EMSHelper.warn("no target parent folder was selected.");
    }

    /**
     * Creates a CJAR based on params.
     * Does not cleanup generated structure.
     * @param parent folder to create skeleton and export in.
     * @param name name of the archive
     * @param export Export after creation?
     */
    public static void createCJAR(Path parent, String name, boolean export){
        createCJAR(parent, name, null, export, false);
    }


    /**
     * Creates a new CJAR, according to parameters.
     *
     * Shows UI Tool to prompt user for population.
     *
     * @apiNote Call is ignored if the file cannot be created.
     * @param parent directory to create the CJAR in. i.e Selecting the Desktop folder, this would create <code>name.cjar</code> on the desktop.
     * @param name Title of the CJAR package.
     * @param ls loadscript class to serialize into this cjar. May be null if no script is required.
     * @param export If true, generated CJAR structure is automatically exported to a .cjar archive.
     * @param cleanup If true, after export the uncompressed file structure will be deleted.
     */
    public static void createCJAR(Path parent, String name, loadscript ls, boolean export, boolean cleanup){
        try {
            CJARMeta meta = new CJARMeta(OPEX.getGameSuper().version(), OPEX.getEngineSuper().getClass().getSimpleName()); // Meta data storage for CJAR.
            File target = createSkeleton(parent, name, meta);                                                           // Create file structure

            if (target == null){
                WarnCreateIgnore("Skeleton was not created.");
                return;
            }

            new CJARFilePrompt(target, meta);                                                                           // Prompt user to modify and add files.

            if (ls != null)                                                                                             // If a loadscript was provided, serialize it.
                SerializePreExport(ls, target, CJAR_LOADSCRIPT, meta);

            SerializePreExport(meta, target, CJAR_META_DATA, meta);                                                       // Serialize meta.

            if(export)                                                                                                  // If an export was requested,
                if (Export(target, meta)) {                                                                              // Export the target
                    if (cleanup)                                                                                         // IF export was successful, remove temp data if requested.
                        FilesHelper.deleteDirectory(target);
                } else throw new IOException("Failed to export to .cjar!");
        } catch (IOException e) {
            EMSHelper.handleException(e);
            WarnCreateIgnore("Failed to generate CJAR, likely a disk access issue, or an old export already exists.");
        }
    }

    private static File createSkeleton(Path parent, String name, CJARMeta meta) throws IOException {
        File target = new File(parent.toString() + "/" + name + "/");                                           // Identify target

        if (target.exists()){
            WarnCreateIgnore("Structure already exists, stopping to avoid corruption.");
            return null;
        }

        if (!FilesHelper.assertOnDisk(target, false, true)){
            WarnCreateIgnore("Could not assert that the skeleton structure parent is on disk.");
            return target;
        }

        for(String pkg : ALL_CJAR_PACKAGES){                                                                             // BEGIN:  For every expected package,
              addFilePreExport(null, target, pkg, meta);                                                          //         Create a directory
        }

        return target;                                                                                                    // RETURN generated skeleton structure
    }

    /**
     * Serializes a file to the structure.
     * @param targetStruct Target file structure
     * @param dest target package (e.g this#PKG_AUDIO_SFX)
     * @param meta Metadata storage for the CJAR. File will be logged in the meta data.
     */
    protected static void SerializePreExport(Serializable serialData, File targetStruct, String dest, CJARMeta meta) throws IOException {
        File fullTarget = new File(targetStruct + dest);
        FilesHelper.writeOut(fullTarget, serialData);                                                                       // Serialize to file.
        meta.paths.add("/" + targetStruct.toURI().relativize(fullTarget.toURI()).getPath());
    }

    /**
     * Copies an existing file on disk into the uncompressed CJAR structure.
     * @param toCopy File to copy. If null, Destination will be asserted, and no file will be produced.
     * @param targetStruct Target CJAR Structure root.
     * @param dest Destination relative to CJAR structure (i.e this#PKG_AUDIO_SFX)
     * @param meta Meta data of the CJAR.
     * @throws IOException if a read / write problem is encountered.
     */
    protected static void addFilePreExport(File toCopy, File targetStruct, String dest, CJARMeta meta) throws IOException {
        File copied = new File(targetStruct + dest + "/" +
                ((toCopy != null) ? toCopy.getName() : ""));

        if (toCopy == null) {
            if (!FilesHelper.assertOnDisk(copied, copied.isDirectory(), true))
                JOptionPane.showMessageDialog(null, "Could not assert target on disk.");
            else
                try {
                    createPlaceHolder(targetStruct, copied, meta);
                } catch (IOException e) {
                    WarnCreateIgnore("Failed to create a placeholder, it may already exist.");
                }
            return;
        }

        if (toCopy.getAbsolutePath().equals(targetStruct.getAbsolutePath())){
            WarnCreateIgnore("Tried to add skeleton to self!");
            return;
        }

        if(toCopy.isDirectory()){                                                                                       // Custom package.
            String loc =                                                                                                // Set destination to CUSTOM if it isn't already.
                    ((!dest.startsWith(PKG_CUSTOM)) ?
                            PKG_CUSTOM
                            :
                            dest)
                    + "/" + toCopy.getName();

            File custom = new File(targetStruct + loc);                                                        // Define target custom package
            FilesHelper.assertOnDisk(custom, false, true);                                    // Assert target package on disk
            meta.paths.add("/" + targetStruct.toURI().relativize(custom.toURI()).getPath());                            // Index

            createPlaceHolder(targetStruct, custom, meta);

            for(File file : Objects.requireNonNull(toCopy.listFiles())){
                    addFilePreExport(file, targetStruct, loc, meta);                                                    // Recursively add files / sub-packages
            } // TODO will this add sub-packages INSIDE the target custom package?
            return;                                                                                                     // Don't copy.
        }


        FilesHelper.Copy(toCopy, copied);                                                                               // Add as file to selected package.
        meta.paths.add("/" + targetStruct.toURI().relativize(copied.toURI()).getPath());                                // index.
    }

    private static void createPlaceHolder(File packagePath, File dest, CJARMeta meta) throws IOException {
        new File( dest + "/." + dest.getName() + ".ph").createNewFile();
        meta.paths.add("/" + packagePath.toURI().relativize(dest.toURI()).getPath() + "." + dest.getName() + ".ph");
    }

    /**
     * Exports the provided CJAR to disk.
     * @param target Target file for the export.
     * @param meta metadata of the CJAR to export
     * @return true of export was sucessfull.
     */
    private static boolean Export(File target, CJARMeta meta) {
        if (!FilesHelper.isEmpty(FilesHelper.getFileExtension(target))) {
            WarnCreateIgnore("No file extention should be provided.");
            return false;
        }
        return FilesHelper.WriteCJAR(target, meta);
    }


    //#endregion

    //#region static utilities

    //TODO this can be abstracted.
    public static JFileChooser createChooser(){
        JFileChooser c = new JFileChooser();
        c.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        c.setDialogTitle("Parent Directory to create a uncompressed CJAR.");
        return c;
    }

    /**
     * Attempts to remove the requested file from the provided skeleton structure.
     * @param parent target skeleton structure root
     * @param target file to remove
     * @param meta metadata of the target CJAR
     */
    public static void removePreExport(File parent, String target, CJARMeta meta) {
        File removeTarget = new File(parent + target);                                                              // Define target

        if(!removeTarget.exists()) {                                                                                          // if it doesn't exist,
            WarnCreateIgnore("Target does not exist!");                                                                    // warn
            meta.paths.remove("/" + target);                                                                               // Remove index
            return;                                                                                                           // don't continue.
        }

        if (meta.paths.contains(target)) {                                                                                    // If is indexed,
            if (removeTarget.isDirectory()) {                                                                                 // If is a package,
                if (ALL_CJAR_PACKAGES.contains(target.endsWith("/") ? target.substring(0, target.length() - 1) : target)) {   // Check if it's a default package
                    WarnCreateIgnore("Cannot remove required packages!");                                                  // warn
                    return;                                                                                                   // don't continue.
                } else {
                    if (Objects.requireNonNull(removeTarget.listFiles()).length != 0)                                      // Else, it must be a removable directory. Does it have children?
                        for (File file : removeTarget.listFiles())                                                                // Remove them recursively.
                            removePreExport(parent, "/" + parent.toURI().relativize(file.toURI()).getPath(), meta);

                }

                meta.paths.remove("/" + parent.toURI().relativize(removeTarget.toURI()).getPath());                           // No children, safe to delete directory
                FilesHelper.deleteDirectory(removeTarget);                                                              // delete dir.
            } else if(FilesHelper.getFileExtension(removeTarget).equals(".ph") && !removeTarget.getAbsolutePath().contains(PKG_CUSTOM + "/")){// If target is a placeholder header AND isn't in the CUSTOM package
                WarnCreateIgnore("That index appears to be a non custom package .ph header. These should not be removed.");  // warn, return and ignore.
            } else {
                removeTarget.delete();                                                                                          // remove as a safe to delete file
                meta.paths.remove(target);                                                                                      // remove index.
            }
        } else {
            WarnCreateIgnore("Target is not registered in CJAR meta, ignoring!");
        }
    }
    //#endregion

    /**
     * Warn that a factory call was ignored.
     * @param s warning message
     */
    private static void WarnCreateIgnore(String s) {
        EMSHelper.warn("A call within the CJAR Factory was ignored; " + s);
    }

    public static void main(String[] args) {
        OPEXPresentation.main(new String[]{});
        JOptionPane.showMessageDialog(null, "Wait for OPEX to boot before entering a name!");
        while(!OPEX.isRunning()); // Wait for engine to boot to avoid Cache deadlock
        createCJAR();
        OPEX.Stop();
    }

}
