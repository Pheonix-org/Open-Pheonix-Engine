package com.Shinkson47.JGEL.BackEnd.Configuration;

import java.awt.Font;

import com.Shinkson47.JGEL.FrontEnd.Window.Rendering.Theme;


/**
 * JGEL configuration file
 * 
 * Stores [and manages] configuration and data for JGEL and the game client. 
 * 
 * 
 * @author gordie
 *
 */
public class Configuration {
	
	//JGEL
	public static final float JGEL_VERSION = (float) 0.1;
	public static long StartTime = 0L;
	
	//GRAHPICS
	public static Theme theme = new Theme();
	public static int DefaultResolutionX = 800, DefaultResolutionY = 600;
	
	//Client
	public static String ClientName = "Not Specififed"; 
	public static String DeveloperName = "Not Specififed"; 
	public static float ClientVersion = (float) 0.0;
	public static String ResourceFolder = "./Resources";
}
