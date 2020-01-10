package com.Shinkson47.JGEL.FrontEnd.Window;

import javax.swing.JFrame;

import com.Shinkson47.JGEL.BackEnd.Configuration.Configuration;
import com.Shinkson47.JGEL.BackEnd.Configuration.DisplayMode;
import com.Shinkson47.JGEL.BackEnd.Input.KeyboardHooker;
import com.Shinkson47.JGEL.BackEnd.Updating.EventHook;
import com.Shinkson47.JGEL.BackEnd.Updating.HookUpdater;
import com.Shinkson47.JGEL.FrontEnd.Window.Rendering.ContentWindow;

public class GameWindow implements EventHook {
	private DisplayMode displayMode = DisplayMode.Windowed;
	private int GameX = 0, GameY = 0, GameWidth = 0, GameHeight = 0;
	public JFrame Window = new JFrame();
	public ContentWindow CurrentWindow = null;

	
	/**
	 * Create and display a GameWindow following the default configuration.
	 */
	public GameWindow(ContentWindow window) {
		initalise(window);
	}
	
	/**
	 * Create and display a GameWindow with a custom size
	 * 
	 * TODO Single null value to follow configuration for value?
	 * @param Width Custom width
	 * @param Height Custom height
	 */
	public  GameWindow(int Width, int Height, ContentWindow window) {
		GameWidth = Width;
		GameHeight = Height;
		initalise(window);
	}
	
	/**
	 * Create and display a GameWindow with a custom size in a custom location
	 * @param Width Custom Width
	 * @param Height Custom Height
	 * @param x Custom x position
	 * @param y Custom y position
	 */
	public GameWindow(int Width, int Height, int x, int y, ContentWindow window) {
		GameWidth = Width;
		GameHeight = Height;
		GameX = x;
		GameY = y;
		initalise(window);
	}
	
	public void SetDisplayMode(DisplayMode d) {
		//TODO
	}
	
	public void SetWindow(ContentWindow e) {
		CurrentWindow = e;
	}
	
	public void SetTitle(String s) {
		Window.setTitle(s);
	}
	
	public void SetVisible(boolean b) {
		Window.setVisible(b);
	}
	
	private void initalise(ContentWindow window) {
		Window = new JFrame();
		SetWindow(window);
		
		if (GameWidth == 0) GameWidth = Configuration.DefaultResolutionX;
		if (GameHeight == 0) GameHeight = Configuration.DefaultResolutionY;
		
		Window.addKeyListener(new KeyboardHooker(CurrentWindow));
		Window.setLocation(GameX, GameY);
		Window.setSize(GameWidth, GameHeight);
		Window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		HookUpdater.RegisterNewHook(this);
		
		Window.setVisible(true);
		Window.getGraphics().drawString("This window has not been updated.", 10, 40);
		Window.getGraphics().drawString("This should not occour, and is likely JGEL's fault. :(", 10, 60);
		Window.getGraphics().drawString("Trouble shooting should be started at the hook updater, which is responible for updating stuff.", 10, 0);
	}

	@Override
	public void UpdateEvent() {
		if (CurrentWindow == null) {
			return;
		}
		
		try {
			Window.getGraphics().drawImage(CurrentWindow.GetFrame(), 0, 0, null);
		} catch (Exception e) {
			return;				//Something is throwing null pointers on the first frame of a new window, and i cant for the fuckin' life of me figure out what's causing it cause of the concurrent threading so this shit is here to stay.
		}
	}

	public void Close() {
		Window.setVisible(false);
		HookUpdater.DeRegister(this);
	}

	@Override
	public void EnterUpdateEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ExitUpdateEvent() {
		// TODO Auto-generated method stub
		
	}
}
