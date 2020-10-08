package com.shinkson47.opex.backend.io.cjar;

import com.shinkson47.opex.backend.io.data.FilesHelper;
import com.shinkson47.opex.backend.runtime.environment.OPEX;
import com.shinkson47.opex.backend.runtime.environment.OPEXPresentation;
import com.shinkson47.opex.backend.runtime.errormanagement.EMSHelper;
import jdk.nashorn.internal.scripts.JO;

import javax.swing.*;
import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipException;

/**
 * Object reference to a loaded CJAR
 *
 * @since 2020.7.13.A
 * @version 1
 *
 */
public class ContentJavaArchive {

    /**
     * The file on disk that this instance has loaded.
     */
    private File fileOnDisk = null;

    /**
     *
     */
    static JFileChooser chooser = new JFileChooser();

    public static void removePreExport(File parent, String target, CJARMeta meta) {
        File removeTarget = new File(parent + target);

        if(!removeTarget.exists()) {
            JOptionPane.showMessageDialog(null, "Target does not exist!");
            meta.paths.remove("/" + target);
            return;
        }

        if (meta.paths.contains(target)) {
            if (removeTarget.isDirectory()){
                if (ALL_CJAR_PACKAGES.contains(target.endsWith("/") ? target.substring(0,target.length() - 1) : target)) {
                    JOptionPane.showMessageDialog(null, "Cannot remove required packages!");
                    return;
                } else
                    if(removeTarget.listFiles() != null || Objects.requireNonNull(removeTarget.listFiles()).length != 0)
                        for(File file : removeTarget.listFiles())
                            removePreExport(parent, "/" + parent.toURI().relativize(file.toURI()).getPath(), meta);

                    // No children, safe to delete.
                    meta.paths.remove(parent.toURI().relativize(removeTarget.toURI()).getPath());
                    FilesHelper.deleteDirectory(removeTarget);
             }else
                removeTarget.delete();  // remove as file


            meta.paths.remove(target);
        } else {
            JOptionPane.showMessageDialog(null, "Target is not registered in CJAR meta, ignoring!");
        }
    }


    /**
     * Gets the reference to file this instance has loaded.
     *
     * @return this.fileOnDisk.
     */
    public File getFileOnDisk() { return fileOnDisk; }

    //#region Load
    public ContentJavaArchive(Path location) throws FileNotFoundException {
        loadCJAR(location);
    }

    public ContentJavaArchive(File file) throws FileNotFoundException {
        loadCJAR(file);
    }

    /**
     * Validates and loads content archive from path
     * @param path CJAR to load.
     */
    private void loadCJAR(Path path) throws FileNotFoundException {
        fileOnDisk = new File(path.toString());                                                                         // Fetch file on disk
        loadCJAR(fileOnDisk);                                                                                           // Parse to load file.
    }

    /**
     * Validates and loads content archive from file
     * @param file to load
     */
    private void loadCJAR(File file) throws FileNotFoundException {
        // Assert is a valid file.
        if (!fileOnDisk.exists() || fileOnDisk.isDirectory()) throw new FileNotFoundException("Path provided does not point to a file.");

        // validate is CJAR
        String fileExtention = fileOnDisk.getName().substring(fileOnDisk.getName().lastIndexOf(".")).toLowerCase();
        if (!(fileExtention == ".cjar" || fileExtention == ".jar")) throw new FileNotFoundException("File provided is not extended with .jar or .cjar");

        // Load contents

    }


    //#endregion





    //#region CJAR Factory
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

    public static final String CJAR_META_DATA =        PKG_META   + "/meta.cjar";
    public static final String CJAR_LOADSCRIPT =       PKG_META   + "/loadscript.ls";

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

    /**
     * Prompts name, directory then creates a new CJAR.
     */
    public static void createCJAR(){
        String input = JOptionPane.showInputDialog(null, "CJAR Name");
        // Don't mine my regex lmao.
        input.replaceAll("(\\$|\\+|\\#|\\%|\\&|\\{|\\}|\\\\|\\<|\\>|\\*|\\?|\\/|\\$|\\!|\\'|\\\"|\\:|\\@|\\+|\\`|\\||\\=)*", "");
        input.replaceAll("( )*", "");

        if(input.equals("null") || input.equals("")){
            JOptionPane.showMessageDialog(null, "No name given.");
            return;
        }
        createCJAR(input);
    }

    /**
     * Prompts directory, then creates a new CJAR.
     */
    public static void createCJAR(String name){
        chooser.setDialogTitle("Parent Directory to create a uncompressed CJAR.");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //
        // disable the "All files" option.
        //
        chooser.setAcceptAllFileFilterUsed(false);
        //
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            createCJAR(chooser.getSelectedFile().toPath(), name, true);
        }
        else {
            return;
        }
    }

    /**
     * Creates a CJAR based on params.
     * Does not cleanup generated structure.
     * @param parent
     * @param name
     * @param export
     */
    public static void createCJAR(Path parent, String name, boolean export){
        createCJAR(parent, name, null, export, false);
    }


    /**
     * Creates a new CJAR.
     *
     * @param parent directory to create the CJAR in. i.e Selecting the Desktop folder, this would create <code>name.cjar</code> on the desktop.
     * @param name Title of the CJAR package.
     * @param ls loadscript class to serialize into this cjar. May be null if no script is required.
     * @param export If true, generated CJAR structure is automatically exported to a .cjar archive.
     * @param cleanup If true, after export the uncompressed file structure will be deleted.
     */
    public static void createCJAR(Path parent, String name, loadscript ls, boolean export, boolean cleanup){
        try {
            // Meta data storage for CJAR.
            CJARMeta meta = new CJARMeta(OPEX.getGameSuper().version(), OPEX.getEngineSuper().getClass().getSimpleName());

            // CREATE FILE STRUCTURE
            File target = new File(parent.toString() + "/" + name + "/");

            if (target.exists()) {
                JOptionPane.showMessageDialog(null, "Target already exists! Will not override existing.");
                return;
            } else {
                if (!target.mkdirs()) {                                                                                 //         Confirm disk write access
                    JOptionPane.showMessageDialog(null, "Failed to create target directory, check names, paths and disk access.");
                    return;                                                                                             // RETURN: Failed to create root.
                } else {
                    for(String pkg : ALL_CJAR_PACKAGES){                                                                // BEGIN:  For every expected package,
                        AddFilePreExport(null, target, pkg, meta);
                        File file = new File(target + pkg);                                                    //         Create a directory.
                        file.mkdirs();

                        meta.paths.add("/" + target.toURI().relativize(file.toURI()).getPath());                        // Add relative file path to file log.
                    }
                }
            }

            // CREATE FILES
            //Request manual files.
            new CJARFilePrompt(target, meta);


            if (ls != null){
                AddFilePreExport(ls, target, CJAR_LOADSCRIPT, meta);
            }

            // CREATE META DATA FILE
            AddFilePreExport(meta, target, CJAR_META_DATA, meta);

            // EXPORT TO ARCHIVE & DELETE TEMP DATA
            if(export)
                if (Export(target, meta))
                    if(cleanup)
                        FilesHelper.deleteDirectory(target);                                                                // IF export was successful, remove temp data.
        } catch (IOException e) {
            EMSHelper.handleException(e);
            JOptionPane.showMessageDialog(null, "Failed to generate CJAR, likely a disk access issue.");
        }
    }

    /**
     * Copies a file on disk into the uncompressed CJAR structure.
     * @param toCopy File to copy.
     * @param targetStruct Target CJAR Structure root.
     * @param dest Destination relative to CJAR structure (i.e this#PKG_AUDIO_SFX)
     * @param meta Meta data of the CJAR.
     * @throws IOException if a read / write problem is encountered.
     */
    protected static void addFilePreExport(File toCopy, File targetStruct, String dest, CJARMeta meta) throws IOException {
        File copied = new File(targetStruct + dest +"/" + toCopy.getName());
        if(copied.exists()){
            JOptionPane.showMessageDialog(null, "Target already exists! To overwrite, remove first.");
            return;
        }

        if(toCopy.isDirectory()){
            String loc;
            if(!dest.startsWith(PKG_CUSTOM))
                loc = PKG_CUSTOM + "/" + toCopy.getName();
            else
                loc = dest + "/" + toCopy.getName();

            File custom = new File(targetStruct + loc);
            custom.mkdirs();

            meta.paths.add("/" + targetStruct.toURI().relativize(custom.toURI()).getPath());

            for(File file : Objects.requireNonNull(toCopy.listFiles())){
                if (file.isDirectory())
                    addFilePreExport(file, targetStruct, loc, meta);
                else
                    addFilePreExport(file, targetStruct, loc, meta);
            }

            return;
        }

        InputStream in = new BufferedInputStream(new FileInputStream(toCopy));
        OutputStream out = new BufferedOutputStream(new FileOutputStream(copied));

        byte[] buffer = new byte[1024];
        int lengthRead;
        while ((lengthRead = in.read(buffer)) > 0) {
            out.write(buffer, 0, lengthRead);
            out.flush();
        }
        out.close();
        in.close();
        meta.paths.add("/" + targetStruct.toURI().relativize(copied.toURI()).getPath());
    }

    /**
     *
     * @param targetStruct Target file structure
     * @param dest target package (e.g this#PKG_AUDIO_SFX)
     * @param meta Metadata storage for the CJAR. File will be logged in the meta data.
     */
    protected static void AddFilePreExport(Serializable serialData, File targetStruct, String dest, CJARMeta meta) throws IOException {
        File fullTarget = new File(targetStruct + dest);

        if(!fullTarget.exists())
            if (fullTarget.getName().contains(".")){
                fullTarget.createNewFile();
            } else {
                fullTarget.mkdirs();
            }


        // DEST IS DIR. IGNORE SERIAL, CREATE DIR. PLACEHOLDER.
        if(fullTarget.isDirectory()){
                new File(fullTarget + "/." + fullTarget.getName() + ".ph").createNewFile();                   // Create a place holder. Empty dirs are ignored
                meta.paths.add(dest + "/." + fullTarget.getName() + ".ph");
                return;
        }

        // DEST IS FILE
        if (fullTarget.isFile()) {                                                                                      // Ensure parent folders exsist.
            FilesHelper.writeOut(fullTarget, serialData);                                                               // Serialize to file.
            meta.paths.add("/" + targetStruct.toURI().relativize(fullTarget.toURI()).getPath());                        // When added, add to meta.
        }
    }

    /**
     *
     * @param target
     * @param meta
     * @return
     */
    private static boolean Export(File target, CJARMeta meta) {
        try {
            byte buffer[] = new byte[10240];

            // Open archive file
            Manifest manifest = new Manifest();
            manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");

            FileOutputStream stream = new FileOutputStream(target.getAbsolutePath() + ".cjar");
            JarOutputStream out = new JarOutputStream(stream, manifest);

            for (String pathToJar : meta.paths) {
                File tobeJared = new File(target.getAbsolutePath() + pathToJar);
                if (tobeJared == null || !tobeJared.exists() || tobeJared.isDirectory()) continue; // Just in case...
                writeToJar(tobeJared, out, target);
            }
        } catch (Exception ex) {
            EMSHelper.handleException(ex);
            return false;
        }
        return true;
    }


    private static void writeToJar(File source, JarOutputStream target, File root) throws IOException
    {
        BufferedInputStream in = null;
        try
        {
            String name = source.getPath().replace("\\", "/");
            if (source.isDirectory())
            {
                if (!name.isEmpty())
                {
                    if (!name.endsWith("/"))
                        name += "/";
                    JarEntry entry = new JarEntry(name);
                    entry.setTime(source.lastModified());
                    target.putNextEntry(entry);
                    target.closeEntry();
                }
                for (File nestedFile: source.listFiles())
                    writeToJar(nestedFile, target, root);
                return;
            }

            JarEntry entry = new JarEntry(root.toURI().relativize(new File(name).toURI()).getPath());
            entry.setTime(source.lastModified());
            try {
                target.putNextEntry(entry);
            } catch (ZipException e) {

            }
            in = new BufferedInputStream(new FileInputStream(source));

            byte[] buffer = new byte[1024];
            while (true)
            {
                int count = in.read(buffer);
                if (count == -1)
                    break;
                target.write(buffer, 0, count);
            }
            target.closeEntry();
        }
        finally
        {
            if (in != null)
                in.close();
        }
    }



    public static void main(String[] args) {
        OPEXPresentation.main(new String[]{});
        JOptionPane.showMessageDialog(null, "WAIT FOR OPEX TO BOOT INTO PRESENTATION MODE BEFORE ENTERING A NAME!");
        while(!OPEX.isRunning());
        createCJAR();
    }
    //#endregion
}
