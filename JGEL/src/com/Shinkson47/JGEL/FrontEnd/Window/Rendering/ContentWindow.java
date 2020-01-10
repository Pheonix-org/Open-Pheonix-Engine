package com.Shinkson47.JGEL.FrontEnd.Window.Rendering;

import java.awt.Image;
import java.util.ArrayList;

import com.Shinkson47.JGEL.BackEnd.Configuration.Configuration;
import com.Shinkson47.JGEL.BackEnd.Input.KeyInputConfiguration;
import com.Shinkson47.JGEL.FrontEnd.Window.WindowComponent;
import com.Shinkson47.JGEL.FrontEnd.Window.VisualStyles.ThemeBase;

/**
 * Defines what content is diplayed on a window, how it appears and how it is interacted with.
 * 
 * @author gordie
 *
 */
public class ContentWindow {
	
	//Size of the content window.
	public int Width, Height;
	
	//How the user can interact with the content
	public KeyInputConfiguration KeyboardConfig = Configuration.DefaultKeyConfig;
	
	//How the content appears
	public ThemeBase Theme = Configuration.DefaultTheme;
	
	//Content on the window
	public ArrayList<WindowComponent> Components = new ArrayList<WindowComponent>();


	public ContentWindow(int width, int height) {
		Width = width;
		Height = height;
	}

	public Image GetFrame() {
		return null;
	}
	
	public void RegisterWindowComponent(WindowComponent component) {
		Components.add(component);
	}
	
	
	
	public void AssignKeyConfig(KeyInputConfiguration Config) {
		KeyboardConfig = Config;
	}
}
