package APIExample;

import BackEnd.Runtime.Threading.JGELRunnable;

public class ExampleThread implements JGELRunnable {

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
