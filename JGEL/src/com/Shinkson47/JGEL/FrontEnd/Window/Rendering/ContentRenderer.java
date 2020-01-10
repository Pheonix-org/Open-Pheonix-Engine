package com.Shinkson47.JGEL.FrontEnd.Window.Rendering;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import com.Shinkson47.JGEL.FrontEnd.Window.WindowComponent;

public class ContentRenderer {
	
	public static Image GetFrame(ContentWindow window){
		
		BufferedImage image = new BufferedImage(window.Width, window.Height, BufferedImage.TYPE_INT_ARGB);
		Graphics RenderGraphics = image.createGraphics();
		
		for (WindowComponent c : window.Components) {
		RenderComponent(c, RenderGraphics);
		}
		
		return null;
		
	}
	
	private static void RenderComponent(WindowComponent c, Graphics g) {
		switch(c.getClass().getSimpleName()) {
			case "WindowMenu":
			case "RenderStack":
			default:
				
		}
	}

}
