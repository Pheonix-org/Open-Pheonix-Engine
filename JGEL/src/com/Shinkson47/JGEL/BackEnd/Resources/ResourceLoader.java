package com.Shinkson47.JGEL.BackEnd.Resources;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import com.Shinkson47.JGEL.BackEnd.Operation.Diagnostics.Logger;

public class ResourceLoader {
	private Path ResourceFolder;
	
	public static void LoadResourceFolder(Path path) {
		if (!Files.exists(path)) {				
			//TODO								//Resources folder doesn't exsist error
		}
		
		if (!ResourcesFolderIsValid(path)) {	//does it's file structure match what is expected?
			//TODO								//Resource folder does not match the file structure error
		}		

		ImportResorceFolder(path);
	}
	
	public static boolean ResourcesFolderIsValid(Path path) {
		if (!ResourceSubFolderExsists(path, "Sprites")) return false;
		if (!ResourceSubFolderExsists(path, "Animations")) return false;
		if (!ResourceSubFolderExsists(path, "OtherGraphics")) return false;
		if (!ResourceSubFolderExsists(path, "SoundEffects")) return false;
		if (!ResourceSubFolderExsists(path, "BackgroundMusic")) return false;
		return true;
	}
	
	private static boolean ResourceSubFolderExsists(Path PathToCheck, String Extention) {
		return Files.exists(Paths.get(PathToCheck + "/" + Extention + "/")); 
	}
	
	/**
	 * Itterates through the resource folder and imports valid resources into the resource pool.
	 * @param SourceFolder the resource folder.
	 */
	private static void ImportResorceFolder(Path SourceFolder) {
		ResourcePool.UnloadAllResources(); 						//Clear the resource pool
		
		File folder = new File(SourceFolder + "/Sprites/");		//Start in the sprites folder
		File[] listOfFiles = folder.listFiles();				//Get a list of all files in sprites folder

		for (File file : listOfFiles) {							//For each file
			try {
				if(ImageIO.read(file) != null) {				//Test if valid image for sprite
					ResourcePool.Sprites.add(new Sprite(file));	//Import it to the pool as a sprite.
				}
			} catch (IOException e) {
				Logger.log("Encountered an invalid file in the Sprites folder!");
			}  
		}
		
		//Animations
		//OtherGraphics
		//SoundEffects
		//BGM
	}
}
