package com.shinkson47.opex.backend.runtime.environment;

import com.shinkson47.opex.backend.runtime.errormanagement.EMSHelper;
import com.shinkson47.opex.backend.runtime.errormanagement.exceptions.OPEXDisambiguationException;
import com.shinkson47.opex.backend.runtime.errormanagement.exceptions.OPEXStartFailure;
import com.shinkson47.opex.backend.runtime.console.Console;
import com.shinkson47.opex.backend.runtime.threading.OPEXGame;
import com.shinkson47.opex.backend.runtime.threading.ThreadManager;
import com.shinkson47.opex.backend.toolbox.Version;
import com.shinkson47.opex.frontend.window.prefabs.Splash;

/**
 * Default executable client.
 *
 *
 * Internal client. Displays splash screen after engine start.
 * Provides an executable class for a library with no client.
 * @since 2020.4.20.A
 * @author Jordan Gray
 */
public final class OPEXPresentation extends OPEXGame {

	/**
	 * Client version.
	 *
	 * @return inherited engine version.
	 */
	public Version version() {
		return OPEX.getEngineSuper().version();
	}

	/**
	 * Internal default fallback entry point.
	 *
	 * When no client is provided in the runtime, JRE will run this.
	 *
	 * @param args JRE command line arguments.
	 */
	public static void main(String args[]) {
		try {
			new OPEX(new OPEXPresentation());																			// Start OPEX with a new instance of this client.
		} catch (OPEXStartFailure e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {}																								// Nothing needs to happen in this thread, so this is left empty.


	/**
	 * Start runnable client.
	 *
	 * Starts a splash screen, same as intro splash screen.
	 */
	@Override
	public void run() {
		Console.externalLog("Game runnable started execution.");														// Payload thread was started.
		Splash.NotifyPresentationMode();																				// Notify splash screen to display in presentation mode.

		try {
			ThreadManager.createThread(new Splash(), "OPEXPresentationSplash");								// Open splash screen in new thread.
		} catch (OPEXDisambiguationException e) {
			EMSHelper.handleException(e);																				// A splash thread already exsists. Should never be possible.
		}
	}



}