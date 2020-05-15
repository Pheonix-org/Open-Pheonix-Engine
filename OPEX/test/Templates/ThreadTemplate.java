package Development.Templates;

import BackEnd.Runtime.Threading.OPEXRunnable;

public class ThreadTemplate implements OPEXRunnable {

	private static Boolean run = true;
	
	@Override
	public void run() {
		run = true;
		while (!Thread.currentThread().isInterrupted() &&  run) {
			//Do a thing.
		}
	}

	@Override
	public void stop() {
		run = false;
		Thread.currentThread().interrupt();
	}

}
