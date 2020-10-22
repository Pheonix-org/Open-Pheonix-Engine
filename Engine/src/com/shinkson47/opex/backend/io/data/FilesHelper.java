package com.shinkson47.opex.backend.io.data;

import com.shinkson47.opex.backend.io.cjar.CJARMeta;
import com.shinkson47.opex.backend.runtime.errormanagement.EMSHelper;
import com.sun.istack.internal.NotNull;

import java.io.*;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipException;

public class FilesHelper {

	/**
	 * Serialises an object
	 * 
	 * @param f      - file to serialise to
	 * @param stream - object to writeout
	 */
	public static void writeOut(File f, Serializable stream) throws IOException {
		if(!f.exists()) f.createNewFile();
		FileOutputStream file = new FileOutputStream(f);
		ObjectOutputStream out;
		out = new ObjectOutputStream(file);
		out.writeObject(stream);
		out.close();
		file.close();
	}

	public static File getFile(Path path, String name) {
		return new File(path.toString() + name);
	}

	public static void ReadIn() {

	}

	public static boolean deleteDirectory(File directoryToBeDeleted) {
		File[] allContents = directoryToBeDeleted.listFiles();
		if (allContents != null) {
			for (File file : allContents) {
				deleteDirectory(file);
			}
		}
		return directoryToBeDeleted.delete();
	}

	/**
	 * Attempts to deserialize a file into the given object.
	 * 
	 * @param <T>    super type of object to cast to.
	 *
	 * @param file   - file to read from
	 * @param target - the object that will be replaced with the deserialised
	 *               object.
	 * @return the deserialized object in the type specified.
	 * @throws IOException            - Failiure to read from file system
	 * @throws ClassNotFoundException - No incomming object to read
	 * @throws ClassCastException     - Serialised object does not match type <T>
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T deserialize(File file, Object target)
			throws IOException, ClassNotFoundException, ClassCastException {
		FileInputStream fileStream = new FileInputStream(file);
		ObjectInputStream inStream = new ObjectInputStream(fileStream);
		Object o = inStream.readObject();
		inStream.close();
		fileStream.close();
		return (T) o;
	}

	/**
	 * @param file File to get extension of.
	 * @return Returns the characters found after a period (.) within the file name.
	 * @throws IndexOutOfBoundsException if file name does not contain a period, indicating no file extention.
	 */
	public static String getFileExtension(@NotNull File file) throws IndexOutOfBoundsException {
		int index = file.getName().lastIndexOf(".");
		return (index < 0) ? "" : file.getName().substring(index);
	}

	/**
	 * Removes illegal characters and white space from file paths
	 * To avoid side effect, this creates a new string instance..
	 * @param path path string to modify.
	 * @returns a new string instance containing the result.
	 */
    public static String cleanPath(String path) {
    	String clean = path;
		clean.replaceAll("(\\$|\\+|\\#|\\%|\\&|\\{|\\}|\\\\|\\<|\\>|\\*|\\?|\\/|\\$|\\!|\\'|\\\"|\\:|\\@|\\+|\\`|\\||\\=)*", "");
		clean.replaceAll("( )*", "");
		clean.replace("\\", "/");
		return clean;
	}


	/**
	 * Checks that the parsed path is valid, AND exists on disk, according to parameters.
	 *
	 * @apiNote Depending on parameters, this can produce side effects :
	 <blockquote>
				<br>The path provided is cleaned before being used. All white space and illegal characters are removed.

				<br><br>Files / directories are created on the disk, if params allow it.

	</blockquote>
	 * @param path - Path to validate
	 * @param fileOrDirectory - If true, path is treated as a file, otherwise as a directory.
	 * @param createIfMissing - If not on disk, should it be created?
	 * @return True if path is valid, AND location exists on the disk. False if path is invalid, or file is not created and does not exist.
	 * @throws IOException if the file should be created, but failed to do so
	 */
	public static boolean validatePath(String path, boolean fileOrDirectory, boolean createIfMissing) throws IOException {
		if (isEmpty(path)) return false;																				// RETURN FALSE: No pah provided
		path = cleanPath(path);																							// Clean w/s, and illegal characters from the path.

		try {
			Paths.get(path);																							// Test for a valid path
		} catch (InvalidPathException e){
			return false;																								// RETURN FALSE: input cannot be converted to a path
		}

		return assertOnDisk(new File(path), fileOrDirectory, createIfMissing);
	}


	/**
	 * Asserts a files presence on the disk, according to parameters.
	 * @param target File to check
	 * @param fileOrDirectory - If true, target is treated as a file, otherwise as a directory.
	 * @param createIfMissing - If true AND target is not on disk, attempt to create it.
	 * @return False if not on disk, and it was not created. True if file exists, or was sucessfully created.
	 * @throws IOException An IO error occurred whilst creating the file, as requested.
	 */
	public static boolean assertOnDisk(File target, boolean fileOrDirectory, boolean createIfMissing) throws IOException {
		if (!target.exists()) {																							// If doesn't exist,
			if (createIfMissing) {																				// Should we create it?
				target.mkdirs();																						// If so, assert it's path's directories
				if (fileOrDirectory)																					// If it's a file, create a new file. Return success of creation.
					return target.createNewFile();
				return true;
			} else
				return false;																							// RETURN FALSE: Does not exist, and we shouldn't create it.
		} else
			return true;																								// RETURN TRUE: Is on disk.
	}

	/**
	 * @apiNote Does not account for white space. String " " is not empty.
	 * @param path string to check if empty
	 * @return true if object is null or blank.
	 */
	public static boolean isEmpty(String path) {
		return (path == null ||  path.equals(""));
	}

	public static boolean hasExtention(File file) {
		return isEmpty(getFileExtension(file));
	}

	// TODO test for exsisting presence
	public static void Copy(File toCopy, File destination) throws IOException {
		InputStream in = new BufferedInputStream(new FileInputStream(toCopy));
		OutputStream out = new BufferedOutputStream(new FileOutputStream(destination));

		byte[] buffer = new byte[1024];
		int lengthRead;
		while ((lengthRead = in.read(buffer)) > 0) {
			out.write(buffer, 0, lengthRead);
			out.flush();
		}
		out.close();
		in.close();
	}

	public static boolean WriteCJAR(File target, CJARMeta meta) {
		ArrayList<File> files = new ArrayList<>();
			   for (String pathToJar : meta.paths()) {
				File tobeJared = new File(target.getAbsolutePath() + pathToJar);
				files.add(tobeJared);
			}

		return WriteJAR(target, files);
	}

	//TODO there's litterally zero docs here.
	public static boolean WriteJAR(File target, ArrayList<File> files) {
		try {
			byte buffer[] = new byte[10240];

			// Open archive file
			Manifest manifest = new Manifest();
			manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");

			FileOutputStream stream = new FileOutputStream(target.getAbsolutePath());
			JarOutputStream out = new JarOutputStream(stream, manifest);

			for (File file : files) {
				if (!file.exists() || file.isDirectory()) continue; 													// Just in case...
				writeToJar(file, out, target);
			}

			out.finish();
			out.flush();
			out.close();
			stream.flush();
			stream.close();
		} catch (Exception ex) {
			EMSHelper.handleException(ex);
			return false;
		}
		return true;
	}

	private static void writeToJar(File source, JarOutputStream target, File root) throws IOException {
			String name = cleanPath(source.getAbsolutePath());

			if (source.isDirectory()) {
				if (!name.isEmpty()) {
					if (!name.endsWith("/"))
						name += "/";

					JarEntry entry = new JarEntry(name);
					entry.setTime(source.lastModified());
					target.putNextEntry(entry);
					target.finish();
					target.closeEntry();
				}

				for (File nestedFile: Objects.requireNonNull(source.listFiles()))
					writeToJar(nestedFile, target, root);

				return;
			}

			JarEntry entry = new JarEntry(root.toURI().relativize(new File(name).toURI()).getPath());
			entry.setTime(source.lastModified());
			try {
				target.putNextEntry(entry);
			} catch (ZipException e) {
				EMSHelper.handleException(e);
			}

			BufferedInputStream in = new BufferedInputStream(new FileInputStream(source));
			byte[] buffer = new byte[1024];
			while (true) {
				int count = in.read(buffer);
				if (count == -1)
					break;
				target.write(buffer, 0, count);
			}
			target.closeEntry();
	}
}
