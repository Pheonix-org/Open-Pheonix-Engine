package com.Shinkson47.JGEL.FrontEnd.Window.Rendering;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class RenderLayer {
	public int PosX, PosY;
	public BufferedImage LayerGraphic = null;
	
	public RenderLayer(File file){
		try {
			LayerGraphic = ImageIO.read(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
	}
	
	public RenderLayer(BufferedImage image){
		LayerGraphic = image;
	}
	
	public BufferedImage GetLayer(int x, int y) {
		BufferedImage Layer = new BufferedImage(x,y,BufferedImage.TYPE_INT_ARGB);
		Graphics g = Layer.createGraphics();
		
		g.drawImage(LayerGraphic, PosX, PosY, null);
		return Layer;
	}
}
