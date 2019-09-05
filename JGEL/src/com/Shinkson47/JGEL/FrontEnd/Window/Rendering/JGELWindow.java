package com.Shinkson47.JGEL.FrontEnd.Window.Rendering;

import java.awt.Image;

public class JGELWindow {
	private StackRenderer stackRenderer = null;

	public JGELWindow(int Width, int Height) {
		stackRenderer = new StackRenderer(Width, Height);
	}

	public Image GetFrame() {
		return stackRenderer.GetFrame();
	}
	
	public void RegisterRenderLayer(RenderLayer layer) {
		stackRenderer.stack.RegisterNewLayer(layer);
	}
}
