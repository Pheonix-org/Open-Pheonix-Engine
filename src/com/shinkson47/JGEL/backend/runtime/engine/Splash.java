package backend.runtime.engine;

import backend.errormanagement.EMSHelper;
import backend.runtime.threading.JGELRunnable;
import frontend.windows.JGELWindow;
import frontend.windows.JGELWindowHelper;
import frontend.windows.rendering.ContentWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Splash implements JGELRunnable {
    public static JGELWindow splashScreenSuper = null;

    public static boolean isSplashVisible() {
        return splashVisible;
    }

    /*
     * monitoring isvisible caused swing to fail to display any and all JFrames -_-
     */
    private static boolean splashVisible = false;
    public static boolean presentationMode = false;
    private JPanel PanelSplash;
    private long StartTime = System.currentTimeMillis();
    private final long MIN_DISPLAY = 3L * 1000L;                                                                             //Three second default minimum display time.

    @Override
    public void stop() {

    }

    @Override
    public void run() {
        if (splashVisible) {StartTime = System.currentTimeMillis(); return;}                                            //Don't display a new window on a new call, just refresh the timer.

        splashVisible = true;
        splashScreenSuper = JGELWindowHelper.newWindow(null,"JGELSplashScreen", false);            //Create window and get static reference
        //Prepare Frame
        splashScreenSuper.setContentPane(PanelSplash);                                                                  //Use this class as the super frame
        splashScreenSuper.setUndecorated(true);                                                                         //Make frame borderless
        splashScreenSuper.pack();



        //Paint image to frame
        Image image = null;                                                                                             //Widen image's scope
        try { image = ImageIO.read(new File("./resources/JGEL.cjar/splash.png"));                                                  //Read splash image
        } catch (IOException e) {EMSHelper.handleException(e); splashScreenSuper.dispose(); return;}                    //Failed to read image, don't display, dispose and return.
        splashScreenSuper.setSize(image.getWidth(null), image.getHeight(null));                        //Responsively set size to splash image


        splashScreenSuper.setLocationRelativeTo(null);
        splashScreenSuper.setVisible(true);                                                                             //Early visibility required for graphics object

        Graphics splashPainter = splashScreenSuper.getGraphics();                                                       //Get Graphics from the frame to paint the image
        splashPainter.drawImage(image, 0,0,null);                                                        //Draw splash image to the graphics canvas
        splashPainter.setColor(Color.WHITE);
        splashPainter.setFont(Font.getFont("Monospaced"));
        splashPainter.drawString(JGEL.getGameSuper().getClass().getSimpleName() + " V" + JGEL.getGameSuper().VERSION() + ",",5,splashScreenSuper.getHeight() - 20);
        splashPainter.drawString("Powered by JGEL V" + JGEL.getEngineSuper().VERSION(),5,splashScreenSuper.getHeight() - 5);
        if (presentationMode) {splashPainter.drawString("JGEL is in Presentation Mode.", 5, splashScreenSuper.getHeight() - 35); return;} //Print presentation, and return; thus skipping closability. Splash will remain open.
        else {splashPainter.drawString("Engine is loading", 5, splashScreenSuper.getHeight() - 35);}
        while (!JGEL.isRunning()){}                                                                                     //Wait until engine is ready
        while (System.currentTimeMillis() - StartTime <= MIN_DISPLAY){}                                                 //Minimum display time
        splashVisible = false;
        presentationMode = false;
        splashScreenSuper.setVisible(false);                                                                            //hide
        splashScreenSuper.dispose();                                                                                    //dispose
        Thread.currentThread().interrupt();
    }
}
