package BackEnd.Data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Path;

import BackEnd.ErrorManagement.JGELEMS;
import Tools.CommandBuilder.InstructionBase;

public class JGELFileIO {
	
	public static void WriteOut(Path location, String name, Byte[] data) {
		
	}
	
	public static void WriteOut(Path location, String name, OutputStream stream) {
	
	}
	
	/**
	 * Serializes an object
	 * @param f
	 * @param stream
	 */
	public static void WriteOut(File f, Serializable stream) {
		try {
			FileOutputStream file = new FileOutputStream(f); 
	        ObjectOutputStream out;
			
			out = new ObjectOutputStream(file);  
	        out.writeObject(stream); 
	          
	        out.close(); 
	        file.close();
	        
		} catch (IOException e) {
			JGELEMS.HandleException(e);
		} 
    
	}
	
	public static File getFile(Path path, String name) {
		return new File(path.toString() + name);
	}
	
	/**
	 * Attempts to deserialize a file into the given object.
	 * @param <T> super type of object to cast to.
	 * 
	 * @param file
	 * @param target - the object that will be replaced with the deserialised object.
	 * @return null if failed to deserialize
	 * @return the deserialized object in the type specified.
	 */
	public static <T extends Serializable> T DeSerialize(File file, Object target) {
		try {
		 FileInputStream fileStream = new FileInputStream(file); 
         ObjectInputStream inStream = new ObjectInputStream(fileStream); 
         Object o = inStream.readObject(); 
         inStream.close(); 
         fileStream.close();
         return (T) o;  
	} catch (Exception e) {
		JGELEMS.HandleException(e);
	} 
	return null;
	}
}
