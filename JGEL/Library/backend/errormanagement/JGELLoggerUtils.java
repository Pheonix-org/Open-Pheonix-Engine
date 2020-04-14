package backend.errormanagement;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import frontend.windows.JGELWindowHelper;

public class JGELLoggerUtils {

		/**
		 * Stores all logs since environment start.
		 */
		public static List<String> Logs = new ArrayList<String>();
		
		/**
		 * Creates a new log.
		 */
		public static void log(String log) { 
			Logs.add("[JGEL] " + log);
			
//			if (log.contains("[@AutoRemove]")) { //This shit is required to handle some weird ass threading bullcrap that java pulls. It's requried, just leave it be.
//				Logs.remove(Logs.size() - 1);
//				return;
//			}

			System.out.println(log);
		}

		/**
		 * Writes out all logs to file.
		 */
		public static void writeOut() {
			
			//DEFINE OUTPUT FILE
			int RetryCount = -1;
			
			while (RetryCount <= 5) {
				RetryCount++;
				File file = new File("./LogDump.log");								//Indicate the planned place to store the log.
				if (file.exists()) {												//If a log already exists, remove it.
					file.delete();
				}
				try {
					file.createNewFile();											//Create a new log file
					FileWriter writer = new FileWriter(file);						//Create a writer to write to the log file
				
					//OUTPUT LOGS INTO FILE
					for (String log : Logs) {										//For every log,			
						if (log == null || log.equals("null")) {continue;}			//(Ignore null or empty logs)
						writer.append(System.lineSeparator() + log);				//append the log to the file on a new line.
					}
					
					//CLOSE RESOURCES
					writer.append(System.lineSeparator() + System.lineSeparator());	//WS on file's end to separate call stack
					writer.close();													//Close the writer resource
					return;
				} catch (Exception e) {}
			}

			JOptionPane.showMessageDialog(JGELWindowHelper.getSwingParent(), "Log may not have been created, use the terminal.");
			EMSHelper.warn("");
		}

		public static void crashDump() {
			writeOut();	
		}
}
