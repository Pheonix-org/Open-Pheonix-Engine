package com.Shinkson47.JGEL.FrontEnd.Window.Rendering.Menu;

import java.lang.reflect.Method;

public class MenuItem {
	public Method Action = null; 
	public String Text = null;
	
	public MenuItem(String text, Method action) {
		Action = action;
		Text = text;
	}
}
