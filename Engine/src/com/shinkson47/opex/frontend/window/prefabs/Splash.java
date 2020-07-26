package com.shinkson47.opex.frontend.window.prefabs;

import com.shinkson47.opex.backend.runtime.errormanagement.EMSHelper;
import com.shinkson47.opex.backend.runtime.environment.OPEX;
import com.shinkson47.opex.backend.runtime.threading.IOPEXRunnable;
import com.shinkson47.opex.frontend.window.OPEXWindow;
import com.shinkson47.opex.frontend.window.OPEXWindowHelper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Splash implements IOPEXRunnable {
    public static OPEXWindow splashScreenSuper = null;

    public static boolean isSplashVisible() {
        return splashVisible;
    }

    /*
     * monitoring isvisible caused swing to fail to display any and all JFrames -_-
     */
    private static boolean splashVisible = false;
    protected static boolean presentationMode = false;

    /**
     * Notify splash screen to run in presentation mode.
     */
    public static void NotifyPresentationMode() {presentationMode = true;}

    private JPanel PanelSplash;
    private long StartTime = System.currentTimeMillis();
    private static final long MIN_DISPLAY = 1000L;                                                                      //default and minimum.
    private static long displayMultiplyer = 3L;

    /**
     * Sets the multiplier for minimum display length.
     *
     * Multiplier is equal to the minimum display time in seconds;
     * i.e multiplier 1L results 1 second minimum display time.
     *
     * minimum display time = (1000L * multiplier)
     *
     * Once minimum display time has passed, the splash will close the instant the
     * engine declares sucessfuls startup.
     *
     * If the engine completed before the splash has closed, it will wait for the splash
     * to close before executing post startup and client payloads.
     * @param multiplyer
     */
    public static void setDisplayMultiplyer(long multiplyer){
        displayMultiplyer = multiplyer;
    }

    @Override
    public void stop() {

    }

    @Override
    public void run() {
        if (splashVisible) {StartTime = System.currentTimeMillis(); return;}                                            //Don't display a new window on a new call, just refresh the timer.

        splashVisible = true;
        splashScreenSuper = OPEXWindowHelper.newWindow(null,"OPEXSplashScreen", false);            //Create window and get static reference
        //Prepare Frame
        splashScreenSuper.setContentPane(PanelSplash);                                                                  //Use this class as the super frame
        splashScreenSuper.setUndecorated(true);                                                                         //Make frame borderless
        splashScreenSuper.pack();



        //Paint image to frame
        Image image = null;                                                                                             //Widen image's scope
        try { image = ImageIO.read(new File("./rsc/frontend/visual/splash/splash.png"));                       //Read splash image
        } catch (IOException e) {EMSHelper.handleException(e); splashScreenSuper.dispose(); return;}                    //Failed to read image, don't display, dispose and return.
        splashScreenSuper.setSize(image.getWidth(null), image.getHeight(null));                        //Responsively set size to splash image


        splashScreenSuper.setLocationRelativeTo(null);
        splashScreenSuper.setVisible(true);                                                                             //Early visibility required for graphics object

        Graphics splashPainter = splashScreenSuper.getGraphics();                                                       //Get Graphics from the frame to paint the image
        splashPainter.drawImage(image, 0,0,null);                                                        //Draw splash image to the graphics canvas
        splashPainter.setColor(Color.darkGray);
        splashPainter.setFont(Font.getFont("Monospaced"));
        splashPainter.drawString(OPEX.getGameSuper().getClass().getSimpleName() + " V" + OPEX.getGameSuper().version().getValue() + ",",5,splashScreenSuper.getHeight() - 20);
        splashPainter.drawString("Powered by OPEX V" + OPEX.getEngineSuper().version().getValue(),5,splashScreenSuper.getHeight() - 5);
        if (presentationMode) {splashPainter.drawString("OPEX is in Presentation Mode.", 5, splashScreenSuper.getHeight() - 35); return;} //Print presentation, and return; thus skipping closability. Splash will remain open.
        else {splashPainter.drawString("Initialising Engine", 5, splashScreenSuper.getHeight() - 35);}
        StartTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - StartTime <= (MIN_DISPLAY * displayMultiplyer)){}                           //Minimum display time
        while (!OPEX.isRunning()){}                                                                                     //Wait until engine is ready
        splashVisible = false;
        presentationMode = false;
        splashScreenSuper.setVisible(false);                                                                            //hide
        splashScreenSuper.dispose();                                                                                    //dispose
        Thread.currentThread().interrupt();
    }
}
