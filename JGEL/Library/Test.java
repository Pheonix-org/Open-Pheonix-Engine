import java.awt.Color;

import BackEnd.Runtime.Console.JGELConsole;
import BackEnd.Runtime.Threading.JGELGame;
import FrontEnd.Windows.JGELWindow;
import FrontEnd.Windows.JGELWindowManager;
import FrontEnd.Windows.Rendering.ContentWindow;

public class Test implements JGELGame {

	public static void main(String args[]) {
		//Start JGEL.
		new JGEL(new Test());
		
		//Content windows are used to encapsulate jgel display components. This is not yet implemented.
		ContentWindow content = new ContentWindow();
		
		//This test uses java's Graphics.
		JGELWindow window = JGELWindowManager.newWindow(content, "Main Window");
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		window.getGraphics().setColor(Color.BLACK);
		window.getGraphics().fillRect(100, 100, 100, 100);
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		JGELConsole.ExternalLog("Game runnable started execution.");
	}


}