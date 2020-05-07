package backend.runtime.engine;

import backend.runtime.console.JGELConsole;
import backend.runtime.threading.JGELGame;
import backend.runtime.threading.JGELThreadManager;

public class JGELPresentation implements JGELGame {
	@Override
	public String VERSION() {
		return JGEL.getEngineSuper().VERSION();
	}

	public static void main(String args[]) {
		// Start JGEL.
		new JGEL(new JGELPresentation());
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
		Splash.presentationMode = true;
		JGELThreadManager.createThread(new Splash(), "JGELPresentationSplash");									//Open splash screen in background.
	}


}