package com.shinkson47.opex.frontend.window;

import java.awt.*;

import javax.swing.*;

import com.shinkson47.opex.backend.runtime.environment.OPEX;
import com.shinkson47.opex.backend.runtime.hooking.OPEXHook;
import com.shinkson47.opex.frontend.rendering.ContentWindow;
import com.shinkson47.opex.frontend.rendering.DisplayMode;
import com.shinkson47.opex.frontend.rendering.UpdateMode;

/**
 *
 */
public class OPEXWindow extends JFrame implements OPEXHook {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public DisplayMode displayMode = DisplayMode.Windowed;
	public UpdateMode updateMode = UpdateMode.Disabled;
	private int GameX = 0;
	private int GameY = 0;
	private int GameWidth = 0;
	private int GameHeight = 0;
	public ContentWindow CurrentWindow = null;
	private String WindowName = "Untitled";
	public Color WindowColor = Color.BLACK;
	
	/**
	 * @return the windowName
	 */
	public String getWindowName() {
		return WindowName;
	}

	/**
	 * @param windowName the windowName to set
	 */
	public void setWindowName(String windowName) {
		WindowName = windowName;
	}

	/**
	 * Create and display a GameWindow following the default configuration.
	 */
	public OPEXWindow(ContentWindow window, String name, boolean visible) {
		initalise(window, name, visible);
	}

	/**
	 * Create and display a GameWindow with a custom size
	 *
	 * TODO Single null value to follow configuration for value?
	 * 
	 * @param Width  Custom width
	 * @param Height Custom height
	 */
	public OPEXWindow(int Width, int Height, ContentWindow window, String name, boolean visible) {
		this(window, name, visible);
		GameWidth = Width;
		GameHeight = Height;
	}

	/**
	 * Create and display a GameWindow with a custom size in a custom location
	 * 
	 * @param Width  Custom Width
	 * @param Height Custom Height
	 * @param x      Custom x position
	 * @param y      Custom y position
	 */
	public OPEXWindow(int Width, int Height, int x, int y, ContentWindow window, String name, boolean visible) {
		this(Width, Height, window, name, visible);
		GameX = x;
		GameY = y;
	}

	public void setDisplayMode(DisplayMode d) {
		displayMode = d;
	}

	public void setWindow(ContentWindow e) {
		CurrentWindow = e;
	}

	@Override
	public void setTitle(String s) {
		setTitle(s);
	}

	private void initalise(ContentWindow window, String name, boolean visible) {
		//this.setIconImage(new ImageIcon("./icon.png").getImage());
		setWindow(window);

		if (GameWidth == 0)
			GameWidth = OPEXWindowHelper.getDefaultResolutionX();
		if (GameHeight == 0)
			GameHeight = OPEXWindowHelper.getDefaultResolutionY();

		// TODO Window.addKeyListener(new KeyboardHooker(CurrentWindow));
		setLocation(GameX, GameY);
		setSize(GameWidth, GameHeight);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		setVisible(visible);
		// getGraphics().drawString("This window has not been updated.", 10, 40);
		// getGraphics().drawString("This should not occour, and is likely OPEX's fault.
		// :(", 10, 60);
		// getGraphics().drawString("Trouble shooting should be started at the hook
		// updater, which is responible for updating stuff.", 10, 80);

		OPEX.getHookUpdater().registerUpdateHook(this, name);
	}

	@Override
	public void updateEvent() {
		if (updateMode == UpdateMode.Disabled) {
			return;
		}

		if (CurrentWindow == null) {
			return;
		}

		try {
			getGraphics().drawImage(CurrentWindow.getImage(), 0, 0, null);
		} catch (Exception e) {
			return; // something is throwing null pointers on the first frame of a new window, and i
					// cant for the fuckin' life of me figure out what's causing it cause of the
					// concurrent threading so this shit is here to stay.
		}
	}

	public void close() {
		setVisible(false);
		OPEX.getHookUpdater().deregisterUpdateHook(this.Name);
		dispose();
	}

	@Override
	public void enterUpdateEvent() {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitUpdateEvent() {
		// TODO Auto-generated method stub

	}
}