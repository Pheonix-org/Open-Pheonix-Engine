package com.Shinkson47.JGEL.BackEnd.Input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.Shinkson47.JGEL.FrontEnd.Window.Rendering.ContentWindow;

public class KeyboardHooker implements KeyListener{
	ContentWindow ParentWindow = null; 
	
	public KeyboardHooker(ContentWindow parentWindow){
		ParentWindow = parentWindow;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		if (ValidConfig()) {
			ParentWindow.KeyboardConfig.Type(e.getKeyCode());
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (ValidConfig()) {
			ParentWindow.KeyboardConfig.Press(e.getKeyCode());
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (ValidConfig()) {
			ParentWindow.KeyboardConfig.Release(e.getKeyCode());
		}
	}
	
	private boolean ValidConfig() {
		if (ParentWindow == null) { return false; }
		
		return ParentWindow.KeyboardConfig != null;
	}
}
