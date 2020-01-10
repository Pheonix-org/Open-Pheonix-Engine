package com.Shinkson47.JGEL.FrontEnd.Window.Rendering.UI.Menu;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.List;

import com.Shinkson47.JGEL.BackEnd.Toolbox;
import com.Shinkson47.JGEL.BackEnd.Configuration.Configuration;
import com.Shinkson47.JGEL.FrontEnd.Window.Rendering.JGELWindow;
import com.Shinkson47.JGEL.FrontEnd.Window.Rendering.UI.UIElement;

public class Menu extends UIElement {
	public List<MenuItem> Items;
	public int Width = 100, X = 0, Y = 0;
	
	public Menu(int width) {
		Width = width;
	}
	
	/**
	 * Creates a menu with at a specified position.
	 * 
	 * Positions of 0 will be centred.
	 * 
	 * 
	 * @param width
	 * @param XPOS
	 * @param YPOS
	 */
	public Menu(int width, int XPOS, int YPOS) {
		Width = width;
		X = XPOS;
		Y = YPOS;
	}
	
	public Image Render(JGELWindow RenderSpace) {
		
		//MENU HEIGHT
		int height = 									//Height of menu is equal to
				(Configuration.theme.font.getSize() 	//The font height,
				+ (Configuration.theme.UIPadding * 2)	//Plus padding surrounding text above and below
				+ (Configuration.theme.UIBorder * 2))	//Plus the border of the item
				* Items.size();							//Times the number of items.
		
		if (height > RenderSpace.Height) {
			height = RenderSpace.Height - ((Configuration.theme.UIPadding * 2) + (Configuration.theme.UIBorder * 2));
		}
		
		//MENU POSITION
		if (X == 0)	{
			X = Toolbox.GetCenter(RenderSpace.Width, Width);
		}
		
		if (Y == 0) {
			Y = Toolbox.GetCenter(RenderSpace.Height, height);
		}
		
		//RENDER
		BufferedImage bi = new BufferedImage(Width, height, BufferedImage.TYPE_INT_RGB);
	
		//draw menu top
		//menu base
		//menu side borders
		
		//menu items
			//item border
			//item text
		//scroll indicator
		//cursor
		return null;
	}
}
