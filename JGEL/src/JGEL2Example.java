import APIExample.ExampleThread;
import BackEnd.Runtime.Threading.JGELThreadManager;

public class JGEL2Example {

	public static void main(String[] args) {
		JGELAPI.CreateThread(new ExampleThread(), "Test");
		JGELAPI.ForceDisposeThread(JGELThreadManager.GetThread("Test"));
	}	
}
