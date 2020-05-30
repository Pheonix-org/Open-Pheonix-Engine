package com.shinkson47.visual.pallete.Menu

import com.shinkson47.visual.OPEXVisual
import javax.swing.JCheckBoxMenuItem
import javax.swing.JMenu
import javax.swing.JMenuItem

class WindowMenu(parent: OPEXVisual) : JMenu("Window") {
    init {
        val packItem = JMenuItem("pack")
        packItem.addActionListener { parent.DisplayWindow.pack() }
        super.add(packItem)

        val resizableCheck = JCheckBoxMenuItem("Resizable")
        resizableCheck.addActionListener { parent.DisplayWindow.isResizable = resizableCheck.isSelected }
        super.add(resizableCheck)


        val closeItem = JMenuItem("Close")
        closeItem.addActionListener { parent.stop() }
        super.add(closeItem)

        val autoPackCheck = JCheckBoxMenuItem("Auto pack")
        autoPackCheck.addActionListener { parent.autopack = autoPackCheck.isSelected }
        autoPackCheck.isSelected = true;
        super.add(autoPackCheck);
    }
}