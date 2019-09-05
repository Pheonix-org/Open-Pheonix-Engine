package com.Shinkson47.JGEL.BackEnd.Input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.Shinkson47.JGEL.BackEnd.Operation.Diagnostics.EngineHook;

public class KeyboardHooker implements KeyListener{

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == 123) {
			EngineHook.Show();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}
