package com.shinkson47.visual.pallete.Menu

import com.shinkson47.visual.OPEXVisual
import com.shinkson47.visual.pallete.CJAR.Editor
import javax.swing.JMenu
import javax.swing.JMenuItem

/**
 * Classification of the dropdown menu for the CJAR dropdown menu.
 */
class CJARMenu(parent: OPEXVisual) : JMenu("CJAR") {
    /**
     * Creates a new CJAR menu.
     */
    init {

        /*
            New
         */
        val NewItem = JMenuItem("New")
        NewItem.addActionListener {
            parent.tabberCJAREditor.addTab("New CJAR", Editor())
            parent.switchCards("cardCJARManager", parent.btnCJAR);
            //TODO create and load new cjar folder structure
        }
        super.add(NewItem)

        /*
            Open
         */
        val OpenItem = JMenuItem("Open")
        OpenItem.addActionListener { /*TODO open cjar or folder structure root*/ }
        super.add(OpenItem)

        /*
            Verify
         */
        val VerifyItem = JMenuItem("Verify file structure")
        VerifyItem.addActionListener { /*TODO verify folder structure and files*/ }

        val VerifyMetaItem = JMenuItem("Verify Meta")
        VerifyMetaItem.addActionListener { /*TODO verify meta serial*/ }

        val VerifyMenu = JMenu("Verify")
        VerifyMenu.add(VerifyMetaItem)
        VerifyMenu.add(VerifyItem)
        super.add(VerifyMenu)

        /*
            Export
         */
        val ExportMenu = JMenu("Export")
        val ComressItem = JMenuItem("Comress")
        ComressItem.addActionListener {
            //TODO guide (maybe implement?) cvf cjar creation
            //Check files have generated correctly by user.
        }
        val ComressSignItem = JMenuItem("Compress and sign")
        ComressSignItem.addActionListener {
            //TODO compress as above, then sign.
        }
        ExportMenu.add(ComressItem)
        ExportMenu.add(ComressSignItem)
        super.add(ExportMenu)


        /*
            Close
         */
        val Close = JMenu("Close")
        val CloseAllItem = JMenuItem("Close All Editors")
        CloseAllItem.addActionListener {
            //TODO Close all editor windows
        }
        val CloseCurrentItem = JMenuItem("Close Current")
        CloseCurrentItem.addActionListener {
            //TODO Close selected editor window
        }
        Close.add(CloseCurrentItem)
        Close.add(CloseAllItem)
        super.add(Close)
    }
}