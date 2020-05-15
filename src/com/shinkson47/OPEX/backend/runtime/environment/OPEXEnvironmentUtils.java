package com.shinkson47.OPEX.backend.runtime.environment;

public class OPEXEnvironmentUtils {
	/**
	 * Saves userdata, closes OPEX systems, closes client Then halts the Java
	 * Runtime
	 */
	public static void shutdown(int shutdownCause) {
		halt(shutdownCause);
	}

	/**
	 * Preemtively halts the java runtime.
	 *
	 * No data is saved.
	 */
	private static void halt(int shutdownCause) {
		Runtime.getRuntime().halt(shutdownCause);
	}

}
