package com.Shinkson47.JGEL.FrontEnd.Window.Rendering.Graphics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;



public class AnimatedRenderLayer extends RenderLayer{
	
	public AnimatedRenderLayer(BufferedImage image) {
		super(image);
	}

	private int frame = 0;
	private List<BufferedImage> Frames = new ArrayList<BufferedImage>();
	
	@Override
	public BufferedImage GetLayer(int x, int y) {
		BufferedImage Layer = new BufferedImage(x,y,BufferedImage.TYPE_INT_ARGB);
		Graphics g = Layer.createGraphics();
		
		if (frame > Frames.size() - 1) frame = 0;
				
		g.drawImage(Frames.get(frame), x, y, null);
		
		frame++;
		return Layer;
	}
	
	//TODO this needs testing.
	public void SetAnimationFromGIF(File input) {
		Frames.clear();
		try {
		    ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
		    ImageInputStream stream = ImageIO.createImageInputStream(input);
		    reader.setInput(stream);

		    int count = reader.getNumImages(true);
		    for (int index = 0; index < count; index++) {
		       Frames.add(reader.read(index));
		    }
		} catch (IOException ex) {
		    // An I/O problem has occurred TODO
		}
	}
	
}
