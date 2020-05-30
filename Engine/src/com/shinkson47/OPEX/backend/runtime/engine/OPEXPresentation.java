package com.shinkson47.OPEX.backend.runtime.engine;

import com.shinkson47.OPEX.backend.errormanagement.exceptions.OPEXStartFailure;
import com.shinkson47.OPEX.backend.runtime.console.OPEXConsole;
import com.shinkson47.OPEX.backend.runtime.threading.OPEXGame;
import com.shinkson47.OPEX.backend.runtime.threading.OPEXThreadManager;
import com.shinkson47.OPEX.backend.toolbox.Version;
import com.shinkson47.OPEX.frontend.window.prefab.Splash;

public class OPEXPresentation implements OPEXGame {
	@Override
	public Version VERSION() {
		return OPEX.getEngineSuper().VERSION();
	}

	public static void main(String args[]) {
		// Start OPEX.
		try {
			new OPEX(new OPEXPresentation());
		} catch (OPEXStartFailure e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		// Nothing needs to happen in this thread, so this is left empty.
	}

	@Override
	public void run() {
		// there's not game loop, so this message is just to notify that the main game
		// thread was started.
		OPEXConsole.externalLog("Game runnable started execution.");
		Splash.presentationMode = true;
		OPEXThreadManager.createThread(new Splash(), "OPEXPresentationSplash");									//Open splash screen in background.
	}


}