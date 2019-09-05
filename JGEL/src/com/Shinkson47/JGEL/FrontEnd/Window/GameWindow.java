package com.Shinkson47.JGEL.FrontEnd.Window;

import java.awt.List;
import java.util.ArrayList;

import javax.swing.JFrame;

import com.Shinkson47.JGEL.BackEnd.Configuration.Configuration;
import com.Shinkson47.JGEL.BackEnd.Configuration.DisplayMode;
import com.Shinkson47.JGEL.BackEnd.Input.KeyboardHooker;
import com.Shinkson47.JGEL.BackEnd.Updating.EventHook;
import com.Shinkson47.JGEL.BackEnd.Updating.HookUpdater;
import com.Shinkson47.JGEL.FrontEnd.Window.Rendering.JGELWindow;

public class GameWindow implements EventHook {
	private DisplayMode displayMode = DisplayMode.Windowed;
	private int GameX = 0, GameY = 0, GameWidth = 0, GameHeight = 0;
	public JFrame Window = new JFrame();
	public JGELWindow CurrentWindow = null;
	public ArrayList<WindowComponent> Components = new ArrayList<WindowComponent>();
	
	/**
	 * Create and display a GameWindow following the default configuration.
	 */
	public  GameWindow() {
		initalise();
	}
	
	/**
	 * Create and display a GameWindow with a custom size
	 * 
	 * TODO Single null value to follow configuration for value?
	 * @param Width Custom width 
	 * @param Height Custom height
	 */
	public  GameWindow(int Width, int Height) {
		GameWidth = Width;
		GameHeight = Height;
		initalise();
	}
	
	/**
	 * Create and display a GameWindow with a custom size in a custom location
	 * 
	 * 
	 * @param Width Custom Width
	 * @param Height Custom Height
	 * @param x Custom x position
	 * @param y Custom y position
	 */
	public GameWindow(int Width, int Height, int x, int y) {
		GameWidth = Width;
		GameHeight = Height;
		GameX = x;
		GameY = y;
		initalise();
	}
	
	public void SetDisplayMode(DisplayMode d) {
		//TODO
	}
	
	public void SetWindow(JGELWindow e) {
		CurrentWindow = e;
	}
	
	public void SetTitle(String s) {
		Window.setTitle(s);
	}
	
	public void SetVisible(boolean b) {
		Window.setVisible(b);
	}
	
	private void initalise() {
		Window = new JFrame();
		
		if (GameWidth == 0) GameWidth = Configuration.DefaultResolutionX;
		if (GameHeight == 0) GameHeight = Configuration.DefaultResolutionY;
		
		Window.addKeyListener(new KeyboardHooker());
		Window.setLocation(GameX, GameY);
		Window.setSize(GameWidth, GameHeight);
		Window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		HookUpdater.RegisterNewHook(this);
		
		
		Window.setVisible(true);
		Window.getGraphics().drawString("This window has not been updated.", 10, 40);
		Window.getGraphics().drawString("Ensure that HookUpdater.[Auto]UpdateAll() is called.", 10, 60);
	}

	@Override
	public void UpdateEvent() {
		if (CurrentWindow == null) {
			return;
		}
		
		Window.getGraphics().drawImage(CurrentWindow.GetFrame(), 0, 0, null);		
	}
}
