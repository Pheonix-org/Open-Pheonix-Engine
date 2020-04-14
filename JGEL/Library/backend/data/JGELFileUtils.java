package backend.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Path;

public class JGELFileUtils {
	
	/**
	 * Serialises an object
	 * @param f - file to serialise to
	 * @param stream - object to writeout
	 */
	public static void writeOut(File f, Serializable stream) throws IOException {
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
	
	/**
	 * Attempts to deserialize a file into the given object.
	 * @param <T> super type of object to cast to.
	 * 
	 * @param file - file to read from
	 * @param target - the object that will be replaced with the deserialised object.
	 * @return the deserialized object in the type specified.
	 * @throws IOException - Failiure to read from file system
	 * @throws ClassNotFoundException - No incomming object to read
	 * @throws ClassCastException - Serialised object does not match type <T> 
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T deserialize(File file, Object target) throws IOException, ClassNotFoundException, ClassCastException {
		 FileInputStream fileStream = new FileInputStream(file); 
         ObjectInputStream inStream = new ObjectInputStream(fileStream); 
         Object o = inStream.readObject(); 
         inStream.close(); 
         fileStream.close();
         return (T) o;  
	}
}
