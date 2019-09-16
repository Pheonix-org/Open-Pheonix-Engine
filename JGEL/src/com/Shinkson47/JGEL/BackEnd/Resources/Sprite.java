package com.Shinkson47.JGEL.BackEnd.Resources;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprite {
	public Image image;
	public String Name;
	
	public Sprite(File file) {
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			//TODO Error Manager
		}
		Name = file.getName();
	}
}
