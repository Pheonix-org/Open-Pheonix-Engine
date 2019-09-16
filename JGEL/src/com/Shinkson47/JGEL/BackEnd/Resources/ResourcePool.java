package com.Shinkson47.JGEL.BackEnd.Resources;

import java.util.ArrayList;
import java.util.List;

public class ResourcePool {
	public static List<Sprite> Sprites = new ArrayList<Sprite>();
	public static List<Animation> Animations = new ArrayList<Animation>();
	
	public static void UnloadAllResources() {
		Sprites = new ArrayList<Sprite>();
		Animations = new ArrayList<Animation>();
		
	}
	
	
}
