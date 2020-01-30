package APIExample.Templates;

import BackEnd.Runtime.Threading.JGELRunnable;

public class ThreadTemplate implements JGELRunnable {

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
