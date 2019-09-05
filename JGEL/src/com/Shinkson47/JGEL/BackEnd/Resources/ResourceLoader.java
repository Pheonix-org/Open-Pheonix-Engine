package com.Shinkson47.JGEL.BackEnd.Resources;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ResourceLoader {
	private Path ResourceFolder;
	
	public void LoadResourceFolder(Path path) {
		if (!Files.exists(path)) {				
												//Resources folder doesn't exsist error
		}
		
		if (!ResourcesFolderIsValid(path)) {	//does it's file structure match what is expected?
												//Resource folder does not match the file structure error
		}		
		
		//Import all
	}
	
	public boolean ResourcesFolderIsValid(Path path) {
		if (!ResourceSubFolderExsists(path, "Sprites")) return false;
		if (!ResourceSubFolderExsists(path, "Animations")) return false;
		if (!ResourceSubFolderExsists(path, "OtherGraphics")) return false;
		if (!ResourceSubFolderExsists(path, "SoundEffects")) return false;
		if (!ResourceSubFolderExsists(path, "BackgroundMusic")) return false;
		return true;
	}
	
	private boolean ResourceSubFolderExsists(Path PathToCheck, String Extention) {
		return Files.exists(Paths.get(PathToCheck + "/" + Extention + "/")); 
	}
	
	private void ImportResorceFolder(Path SourceFolder) {
		//Sprites
		//Animations
		//OtherGraphics
		//SoundEffects
		//BGM
	}
}
