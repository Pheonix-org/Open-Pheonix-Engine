package com.shinkson47.opex.frontend.window;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.shinkson47.opex.backend.runtime.errormanagement.EMSHelper;
import com.shinkson47.opex.frontend.rendering.ContentWindow;

public class OPEXWindowHelper {

	/**
	 * Storage of all OPEXWindows under the WindowManager's controll.
	 */
	public static List<OPEXWindow> OPEXWindows = new ArrayList<>();

	/**
	 * Global generic swing parent. Used to parent all windows, including popups,
	 * dialogs, file windows, etc.
	 */
	private static final JFrame SwingParent = new JFrame();

	/**
	 * Default OPEXWindow Size. Used if no other size is specified, or requested
	 * size is not possible.
	 */
	private static int DefaultResolutionX = 400;
	private static int DefaultResolutionY = 400;

	/**
	 * @return the defaultResolutionX
	 */
	public static int getDefaultResolutionX() {
		return DefaultResolutionX;
	}

	/**
	 * @param defaultResolutionX the defaultResolutionX to set
	 */
	public static void setDefaultResolutionX(int defaultResolutionX) {
		DefaultResolutionX = defaultResolutionX;
	}

	/**
	 * @return the defaultResolutionY
	 */
	public static int getDefaultResolutionY() {
		return DefaultResolutionY;
	}

	/**
	 * @param defaultResolutionY the defaultResolutionY to set
	 */
	public static void setDefaultResolutionY(int defaultResolutionY) {
		DefaultResolutionY = defaultResolutionY;
	}

	/**
	 * Get a swing frame to use as a parent for creating new swing windows.
	 * 
	 * @return Static blank JFrame
	 */
	public static JFrame getSwingParent() {
		return SwingParent;
	}

	/**
	 * Create a new swing window, add it to the WindowManager's control, and assign
	 * it it parsed;
	 * 
	 * @param Content to display.
	 * @param name to use for the OPEX Window
	 *
	 *                      This method uses default window size parameters.
	 */
	public static OPEXWindow newWindow(ContentWindow Content, String name, boolean visible) {
		OPEXWindow window = new OPEXWindow(Content, name, visible);
		OPEXWindows.add(window);
		return window;
	}

	/**
	 * Returns the game window by name
	 *
	 * @param name of the window to get.
	 * @return the OPEXWindow at the specified index, null if there is no matching window
	 */
	public static OPEXWindow getWindow(String name) {
		for (OPEXWindow window : OPEXWindows) {
			if (window.getWindowName().equals(name)) {
				return window;
			}
		}

		EMSHelper.warn("Could not find a Window with the name '" + name + "'");
		return null;
	}

	public static void closeAll() {
		for (OPEXWindow window : OPEXWindows) {
			window.close();
		}
		OPEXWindows.clear();
	}

}
