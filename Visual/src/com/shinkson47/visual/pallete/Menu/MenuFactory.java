package com.shinkson47.visual.pallete.Menu;

import com.shinkson47.visual.OPEXVisual;

import javax.swing.*;
import java.awt.*;

//TODO Engine bar creation
public class MenuFactory {
    public static JMenuBar CreateMenuBar(OPEXVisual parent) {
        JMenuBar bar = new JMenuBar();
        bar.add(new CJARMenu(parent));
        bar.add(new WindowMenu(parent));
        return bar;
    }
}
