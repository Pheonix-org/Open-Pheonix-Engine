package com.shinkson47.opex.frontend.rendering;


import com.shinkson47.opex.frontend.scene.Scene;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A displayable Content Window that contains a renderable game Scene.
 */
public class GameContentWindow extends ContentWindow {

    /**
     * Raster buffer.
     *
     * Contains the visual output of this content window.
     */
    private BufferedImage bufferedRaster;

    /**
     * The current Game scene to be displayed
     */
    private Scene currentScene;

    /**
     * Creates a new Game Content Window, with a specified view port size
     * @param width view port width
     * @param height view port height
     */
    public GameContentWindow(int width, int height) {
        super(width, height);
        bufferedRaster = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public void setScene(Scene scene){
        currentScene = scene;
    }


    @Override
    public Image getImage() {
        return null;
    }

    @Override
    public Graphics getGraphics() {
        return null;
    }
}
