import APIExample.ExampleThread;

public class JGEL2Example {

	public static void main(String[] args) {
		JGELAPI.CreateThread(new ExampleThread());
		JGELAPI.ForceDisposeThread(new ExampleThread());
	}
	
}
