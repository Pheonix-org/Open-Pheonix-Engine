package com.Shinkson47.JGEL.FrontEnd.Window;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.Shinkson47.JGEL.BackEnd.Configuration.Configuration;
import com.Shinkson47.JGEL.FrontEnd.Window.Rendering.JGELWindow;

public class WindowManager {
	public static List<GameWindow> GameWindows = new ArrayList<GameWindow>();
	public static JFrame SwingParent = new JFrame();
	
	public static void newWindow() {
		GameWindows.add(new GameWindow());
		GameWindows.get(GameWindows.size() - 1).SetWindow(new JGELWindow(Configuration.DefaultResolutionX, Configuration.DefaultResolutionY));
	}
	
	/**
	 * Returns the game window using index of creation.
	 * 
	 * @param index
	 * @return null if index is out of range
	 * @return The gamewindow at the specified index.
	 */
	public static GameWindow getWindow(int index) {
		if (index < 0 || index > GameWindows.size()) {return null;}
		
		return GameWindows.get(index);
	}

	public static void CloseAll() {
		for (GameWindow window : GameWindows) {
			window.Close();
		}
	}
	
}
