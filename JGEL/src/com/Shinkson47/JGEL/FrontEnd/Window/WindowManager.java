package com.Shinkson47.JGEL.FrontEnd.Window;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.Shinkson47.JGEL.BackEnd.Configuration.Configuration;
import com.Shinkson47.JGEL.FrontEnd.Window.Rendering.ContentWindow;

public class WindowManager {
	public static List<GameWindow> GameWindows = new ArrayList<GameWindow>();
	public static JFrame SwingParent = new JFrame();
	
	public static void newWindow(ContentWindow gw) {
		GameWindows.add(new GameWindow(gw));
		GameWindows.get(GameWindows.size() - 1).SetWindow(gw);
	}
	
	public static void CloseAll() {
		for (GameWindow window : GameWindows) {
			window.Close();
		}
	}
	
}
