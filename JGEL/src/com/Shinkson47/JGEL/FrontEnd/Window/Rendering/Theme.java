package com.Shinkson47.JGEL.FrontEnd.Window.Rendering;

import java.awt.Color;
import java.awt.Font;

public class Theme {
	public Color Background = Color.BLACK, Foreground = Color.WHITE, Accent = Color.GRAY, Text = Color.cyan, Error = Color.RED;
	public Font font = new Font("Serif", java.awt.Font.BOLD, 18);
	public int UIPadding = 4;
	public int UIBorder = 2;
	
	/**
	 * Creates a theme with the default colours
	 */
	public Theme() {
		
	}
	
	/**
	 * Creates a theme with just specified colours. other params are default.
	 * 
	 * A Null parameter is a null colour.
	 * 
	 * @param BG Window background colour
	 * @param FG Window foreground colour, such as ui.
	 * @param ACC Accent colour
	 * @param TXT Text colour
	 * @param ERR Error items, such as error text or missing textures.
	 */
	public Theme(Color BG, Color FG, Color ACC, Color TXT, Color ERR) {
		setThemeColour(BG,FG,ACC,TXT,ERR);
	}
	
	/**
	 * Creates a theme with all parameters
	 *
	 * 
	 * @param BG Window background colour
	 * @param FG Window foreground colour, such as ui.
	 * @param ACC Accent colour
	 * @param TXT Text colour
	 * @param ERR Error items, such as error text or missing textures.
	 * @param font The name of a system or imported font to use.
	 * @param fontstyle	Italic, bold etc. Use AWT.Font.Bold
	 * @param fontsize Font size in px
	 */
	public Theme(Color BG, Color FG, Color ACC, Color TXT, Color ERR, String fontname, int fontstyle, int fontsize, int padding, int border) {
		setThemeColour(BG,FG,ACC,TXT,ERR);
		font = new Font(fontname, fontstyle, fontsize);
		UIPadding = padding;
		UIBorder = border;
	}
	
	private void setThemeColour(Color BG, Color FG, Color ACC, Color TXT, Color ERR) {
		Background = BG;
		Foreground = FG;
		Accent = ACC;
		Text = TXT;
		Error = ERR;
	}
	
	
	
}
