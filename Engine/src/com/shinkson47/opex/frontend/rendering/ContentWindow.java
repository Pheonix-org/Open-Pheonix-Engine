package com.shinkson47.opex.frontend.rendering;

import java.awt.Graphics;
import java.awt.Image;

/**
 * Abstract representation of a window that can be displayed within an OPEXWindow.
 */
public abstract class ContentWindow {

	public ContentWindow(int width, int height) {

	}

	/**
	 * Render and return what this content window represents
	 */
	public abstract Image getImage();

	/**
	 * Return the graphics that would manipulate the output image of this content window.
	 * @return
	 */
	public abstract Graphics getGraphics();
}
