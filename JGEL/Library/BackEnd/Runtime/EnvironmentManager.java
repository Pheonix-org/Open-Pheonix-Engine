package BackEnd.Runtime;

public class EnvironmentManager {
	
	/**
	 * Saves userdata, closes JGEL systems, closes client
	 * Then halts the Java Runtime
	 */
	public static void Shutdown() {
		Halt();
	}

	/**
	 * Preemtively halts the java runtime.
	 * 
	 * No data is saved.
	 */
	private static void Halt() {
		Runtime.getRuntime().halt(0);
	}

}
