package com.Shinkson47.JGEL.FrontEnd.Window.Rendering;

import java.util.List;
import java.util.ArrayList;

public class RenderStack {
	public List<RenderLayer> Layers = new ArrayList<RenderLayer>();
	
	public void ClearAllLayers() {
		Layers.clear();
	}
	
	public void RegisterNewLayer(RenderLayer e) {
		Layers.add(e);
	}
}
