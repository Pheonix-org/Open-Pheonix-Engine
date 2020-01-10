package com.Shinkson47.JGEL.BackEnd.Configuration;

import java.awt.Font;

import com.Shinkson47.JGEL.FrontEnd.Window.Rendering.Theme;

import com.Shinkson47.JGEL.BackEnd.Configuration.Defaults.DefaultKeyBindings;
import com.Shinkson47.JGEL.BackEnd.Input.KeyInputConfiguration;
import com.Shinkson47.JGEL.FrontEnd.Window.VisualStyles.ThemeBase;
import com.Shinkson47.JGEL.FrontEnd.Window.VisualStyles.Themes.PureBlack;

/**
 * JGEL runtime configuration file
 * 
 * Stores [and manages] configuration and data for JGEL and the game client.
 * 
 *  TODO custom configurations 
 * 
 * @author gordie
 *
 */
public class Configuration {
	
	//JGEL
	public static final float JGEL_VERSION = (float) 0.1;
	public static long StartTime = 0L;
	
	//INPUT
	public static KeyInputConfiguration DefaultKeyConfig = new DefaultKeyBindings();

	//GRAHPICS

	public static ThemeBase DefaultTheme = new PureBlack();
	public static Color Background = Color.BLACK, Foreground = Color.WHITE;

	public static Theme theme = new Theme();

	public static int DefaultResolutionX = 800, DefaultResolutionY = 600;
	
	//Client
	public static String ClientName = "Not Specififed"; 
	public static String DeveloperName = "Not Specififed"; 
	public static float ClientVersion = (float) 0.0;
	public static String ResourceFolder = "./Resources";
}
