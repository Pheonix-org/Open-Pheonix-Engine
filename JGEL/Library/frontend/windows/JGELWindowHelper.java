package frontend.windows;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import backend.errormanagement.EMSHelper;
import frontend.windows.rendering.ContentWindow;

public class JGELWindowHelper{
	
	/**
	 * Storage of all JGELWindows under the WindowManager's controll.
	 */
	public static List<JGELWindow> JGELWindows = new ArrayList<JGELWindow>();
	
	/**
	 * Global generic swing parent. Used to parent all windows, including popups, dialogs, file windows, etc.
	*/
	private static final JFrame SwingParent = new JFrame();
	
	/**
	 * Default JGELWindow Size. Used if no other size is specified, or requested size is not possible.
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
<<<<<<< Updated upstream:JGEL/Library/frontend/windows/JGELWindowHelper.java
=======
	 *
>>>>>>> Stashed changes:JGEL/src/com/shinkson47/JGEL/frontend/windows/JGELWindowHelper.java
	 * @return Static blank JFrame
	 */
	public static JFrame getSwingParent() {
		return SwingParent;
	}	
	
	/**
<<<<<<< Updated upstream:JGEL/Library/frontend/windows/JGELWindowHelper.java
	 * Create a new swing window, add it to the WindowManager's control,
	 * and assign it it parsed;
=======
	 * Create a new swing window, add it to the WindowManager's control, and assign
	 * it it parsed;
	 *
>>>>>>> Stashed changes:JGEL/src/com/shinkson47/JGEL/frontend/windows/JGELWindowHelper.java
	 * @param ContentWindow to display.
	 * @param Name to use for the JGEL Window
	 * 
	 * This method uses default window size parameters.
	 */
	public static JGELWindow newWindow(ContentWindow Content, String name) {
		JGELWindow window = new JGELWindow(Content, name);
		JGELWindows.add(window);
		return window;
	}
	
	/**
	 * Returns the game window by name
	 * 
	 * @param name
	 * @return null if there is no matchine window
	 * @return The JGELWindow at the specified index.
	 */
	public static JGELWindow getWindow(String name) {
		for (JGELWindow window : JGELWindows) {
			if (window.getWindowName() == name) {
				return window;
			}
		}
		
		EMSHelper.warn("Could not find a Window with the name '" + name +"'");
		return null;
	}

	public static void closeAll() {
		for (JGELWindow window : JGELWindows) {
			window.close();
		}
		JGELWindows.clear();
	}
	
}
