package backend.runtime.engine;

import java.awt.Color;

import backend.runtime.console.JGELConsole;
import backend.runtime.threading.JGELGame;
import frontend.windows.JGELWindow;
import frontend.windows.JGELWindowHelper;
import frontend.windows.rendering.ContentWindow;

public class Test implements JGELGame {

	public static void main(String args[]) {
		// Start JGEL.
		new JGEL(new Test());

		// Content windows are used to encapsulate jgel display components. This is not
		// yet implemented.
		ContentWindow content = new ContentWindow();

		// This test uses java's Graphics.
		JGELWindow window = JGELWindowHelper.newWindow(content, "Main Window");

		// Just allows the window a second to open before drawing;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		window.getGraphics().setColor(Color.BLACK);
		window.getGraphics().fillRect(100, 100, 100, 100);
	}

	@Override
	public void stop() {
		// Nothing needs to happen in this thread, so this is left empty.
	}

	@Override
	public void run() {
		// there's not game loop, so this message is just to notify that the main game
		// thread was started.
		JGELConsole.externalLog("Game runnable started execution.");
	}

}