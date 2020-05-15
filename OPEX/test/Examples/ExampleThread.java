package Development;

import BackEnd.Runtime.Threading.OPEXRunnable;

public class ExampleThread implements OPEXRunnable {

	@Override
	public void run() {
	    boolean running = true;
	    while(running) {
	        if (Thread.interrupted()) {
	            return;
	        }
	    }
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		Thread.currentThread().interrupt();
		int i = 1; // test break point
	}

}
