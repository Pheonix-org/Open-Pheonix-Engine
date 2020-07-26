package com.shinkson47.opex.backend.io.cjar;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.FileSystemException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Hashtable;
import java.util.function.BiConsumer;

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
    /**
     * IO Tool to create a valid cjar archives.
     */
    public static void createCJAR(){

    }
    //#endregion
}
