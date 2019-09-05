package com.Shinkson47.JGEL.BackEnd.Operation.Diagnostics;

import java.util.ArrayList;
import java.util.List;

public class Logger {

	private static List<String> Logs = new ArrayList<String>();
	
	public static void log(String log) {
		Logs.add(log);
		System.out.println(log);
	}
	
}
