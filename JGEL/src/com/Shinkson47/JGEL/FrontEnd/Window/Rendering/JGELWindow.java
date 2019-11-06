package com.Shinkson47.JGEL.FrontEnd.Window.Rendering;

import java.awt.Image;
import java.awt.image.BufferedImage;

import com.Shinkson47.JGEL.FrontEnd.Window.Rendering.UI.UIElement;
import com.Shinkson47.JGEL.FrontEnd.Window.Rendering.UI.UIRenderer;

public class JGELWindow {
	public int Width = 0, Height = 0;
	private StackRenderer stackRenderer = null;
	public UIRenderer uiRenderer = new UIRenderer();
	public JGELWindow(int width, int height) {
		Width = width;
		Height = height;
		stackRenderer = new StackRenderer(Width, Height);
	}

	public Image GetFrame() {
		BufferedImage bi = new BufferedImage(Width, Height, BufferedImage.TYPE_INT_RGB);
		bi.createGraphics().drawImage(stackRenderer.GetFrame(), 0, 0, null);	
		bi.createGraphics().drawImage(uiRenderer.GetFrame(), 0, 0, null);
		return bi;
	}
	
	public void AddUIElements(UIElement e) {
		uiRenderer.add(e);
	}
	
	public void RegisterRenderLayer(RenderLayer layer) {
		stackRenderer.stack.RegisterNewLayer(layer);
	}
}
