package FrontEnd.Windows;

import java.awt.Color;

import javax.swing.JFrame;

import BackEnd.Runtime.Hooking.JGELHook;
import BackEnd.Runtime.Hooking.JGELHookUpdater;
import FrontEnd.Windows.Rendering.ContentWindow;
import FrontEnd.Windows.Rendering.DisplayMode;
import FrontEnd.Windows.Rendering.UpdateMode;

public class JGELWindow extends JFrame implements JGELHook  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private DisplayMode displayMode = DisplayMode.Windowed;
	public UpdateMode updateMode = UpdateMode.Disabled;
	private int GameX = 0, GameY = 0, GameWidth = 0, GameHeight = 0;
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
	public JGELWindow(ContentWindow window, String name) {
		initalise(window, name);
	}
	
	/**
	 * Create and display a GameWindow with a custom size
	 * 
	 * TODO Single null value to follow configuration for value?
	 * @param Width Custom width
	 * @param Height Custom height
	 */
	public JGELWindow(int Width, int Height, ContentWindow window, String name) {
		this(window, name);
		GameWidth = Width;
		GameHeight = Height;
	}
	
	/**
	 * Create and display a GameWindow with a custom size in a custom location
	 * @param Width Custom Width
	 * @param Height Custom Height
	 * @param x Custom x position
	 * @param y Custom y position
	 */
	public JGELWindow(int Width, int Height, int x, int y, ContentWindow window, String name) {
		this(Width, Height, window, name);	
		GameX = x;
		GameY = y;
	}
	
	public void SetDisplayMode(DisplayMode d) {
		displayMode = d;
	}
	
	public void SetWindow(ContentWindow e) {
		CurrentWindow = e;
	}
	
	public void SetTitle(String s) {
		setTitle(s);
	}
	
	public void SetVisible(boolean b) {
		setVisible(b);
	}
	
	private void initalise(ContentWindow window, String name) {
		SetWindow(window);
		
		if (GameWidth == 0) GameWidth = JGELWindowManager.getDefaultResolutionX();
		if (GameHeight == 0) GameHeight = JGELWindowManager.getDefaultResolutionY();
		
		//TODO Window.addKeyListener(new KeyboardHooker(CurrentWindow));
		setLocation(GameX, GameY);
		setSize(GameWidth, GameHeight);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setVisible(true);
		getGraphics().drawString("This window has not been updated.", 10, 40);
		getGraphics().drawString("This should not occour, and is likely JGEL's fault. :(", 10, 60);
		getGraphics().drawString("Trouble shooting should be started at the hook updater, which is responible for updating stuff.", 10, 0);
		
		JGELHookUpdater.RegisterUpdateHook(this, name);
	}

	@Override
	public void UpdateEvent() {
		if (updateMode == UpdateMode.Disabled) {
			return;
		}
		
		
		if (CurrentWindow == null) {
			return;
		}
		
		try {
			getGraphics().drawImage(CurrentWindow.getImage(), 0, 0, null);
		} catch (Exception e) {
			return;				//something is throwing null pointers on the first frame of a new window, and i cant for the fuckin' life of me figure out what's causing it cause of the concurrent threading so this shit is here to stay.
		}
	}

	public void Close() {
		setVisible(false);
		JGELHookUpdater.DeRegisterUpdateHook(this.Name);
	}

	@Override
	public void EnterUpdateEvent() {
		setBackground(WindowColor);
	}

	@Override
	public void ExitUpdateEvent() {
		// TODO Auto-generated method stub
		
	}
}