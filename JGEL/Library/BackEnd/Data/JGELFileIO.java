package BackEnd.Data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Path;

public class JGELFileIO {
	
	public static void WriteOut(Path location, String name, Byte[] data) {
		
	}
	
	public static void WriteOut(Path location, String name, OutputStream stream) {
	
	}
	
	public static void WriteOut(Path location, String name, Serializable stream) {

	}
	
	public static File getFile(Path path, String name) {
		return new File(path.toString() + name);
	}
	
	public static void ReadIn() {
		
	}
	
	public static void DeSerialise() {
		
	}
}
