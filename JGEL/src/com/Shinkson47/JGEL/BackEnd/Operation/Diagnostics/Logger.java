package com.Shinkson47.JGEL.BackEnd.Operation.Diagnostics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.Shinkson47.JGEL.FrontEnd.Window.WindowManager;

public class Logger {

	public static List<String> Logs = new ArrayList<String>();
	
	public static void log(String log) {
		Logs.add(log);
		System.out.println(log);
	}

	public static void CreateCrashLog() {
		
		//DEFINE OUTPUT FILE
		File file = new File("./LatestCrash.log");							//Indicate the planned place to store the log.
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
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(WindowManager.SwingParent, "Log may not have been created, use the terminal.");
		}
	}
	
}
