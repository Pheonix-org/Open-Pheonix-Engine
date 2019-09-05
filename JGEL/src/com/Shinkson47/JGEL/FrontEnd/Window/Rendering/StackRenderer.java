package com.Shinkson47.JGEL.FrontEnd.Window.Rendering;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import com.Shinkson47.JGEL.BackEnd.Configuration.Configuration;

public class StackRenderer{
	public RenderStack stack = null;
	public int Width, Height;
	
	public StackRenderer(int gameWidth, int gameHeight) {
		Width = gameWidth;
		Height = gameHeight;
	}
	
	public void RegisterRenderStack(RenderStack Stack) {
		stack = Stack;
	}

	/**
	 * Renders a frame using render stacks of render layers.
	 * 
	 * @return the frame.
	 */
	public Image GetFrame() {
		BufferedImage frame = new BufferedImage(Width, Height, BufferedImage.TYPE_INT_ARGB);	//Create a buffered image to represent the window we're drawing into.
		Graphics graphics = frame.getGraphics();												//Get the graphics to modify the image
		
		graphics.setColor(Configuration.Background);											//Set the background color
		graphics.fillRect(0, 0, Width, Height);													
		
		if (stack != null) {																	//Don't render the stack if there's no stack.
			for (RenderLayer layer : stack.Layers) {											//Otherwise, get the image from each layer in the frame
				graphics.drawImage(layer.GetLayer(Width, Height), 0, 0, null);					//And draw them to the buffered frame.
			}
		}
		
		return frame;																			//Return the frame.
	}
 }
