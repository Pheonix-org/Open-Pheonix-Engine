package APIExample;

import BackEnd.Runtime.Threading.JGELThread;

public class ExampleThread implements JGELThread {

	@Override
	public void run() {
		while (true) { //Persistently keep thread alive.
			
		}
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		int i = 1; // test break point
	}

}
