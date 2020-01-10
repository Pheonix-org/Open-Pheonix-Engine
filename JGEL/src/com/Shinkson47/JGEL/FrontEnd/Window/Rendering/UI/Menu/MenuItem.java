package com.Shinkson47.JGEL.FrontEnd.Window.Rendering.UI.Menu;

import sun.management.MethodInfo;

public class MenuItem {
	public String Text;
	public MethodInfo Action = null;
	
	public MenuItem(String text, MethodInfo action) {
		Text = text;
		Action = action;				
	}

}
