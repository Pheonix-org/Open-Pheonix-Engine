package backend.runtime;

public class JGELEnvironmentUtils {	
	/**
	 * Saves userdata, closes JGEL systems, closes client
	 * Then halts the Java Runtime
	 */
	public static void shutdown() {
		halt();
	}

	/**
	 * Preemtively halts the java runtime.
	 * 
	 * No data is saved.
	 */
	private static void halt() {
		Runtime.getRuntime().halt(0);
	}

}
