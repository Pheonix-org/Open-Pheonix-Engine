package com.shinkson47.opex.backend.runtime.entry;

import com.shinkson47.opex.backend.runtime.errormanagement.EMSHelper;
import com.shinkson47.opex.backend.runtime.errormanagement.exceptions.OPEXDisambiguationException;
import com.shinkson47.opex.backend.runtime.errormanagement.exceptions.OPEXStartFailure;
import com.shinkson47.opex.backend.runtime.console.Console;
import com.shinkson47.opex.backend.runtime.threading.OPEXGame;
import com.shinkson47.opex.backend.runtime.threading.OPEXThreadManager;
import com.shinkson47.opex.backend.toolbox.Version;
import com.shinkson47.opex.frontend.window.prefab.Splash;

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
		Console.externalLog("Game runnable started execution.");
		Splash.presentationMode = true;
		try {
			OPEXThreadManager.createThread(new Splash(), "OPEXPresentationSplash");									//Open splash screen in background.
		} catch (OPEXDisambiguationException e) {
			EMSHelper.handleException(e);
		}
	}


}