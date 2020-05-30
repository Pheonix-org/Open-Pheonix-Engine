package com.shinkson47.visual.pallete

import com.shinkson47.visual.pallete.CJAR.tempVersion
import java.util.*
import javax.swing.*

/**
 *  Displayable JFrame used to prompt user for input of an OPEX formatted versioning number
 *
 *  //TODO support for semantic versioning.
 */
class VersionPicker : JFrame() {

    /**
     * Main content pane
     */
    private val panelMain: JPanel? = null;

    /**
     * Year int spinner
     */
    private val yearSpinner: JSpinner? = null;


    /**
     * month int spinner
     */
    private val monthSpinner: JSpinner? = null;

    /**
     * day int spinner
     */
    private val daySpinner: JSpinner? = null;

    /**
     * Cancel button. Closes this window instance without selecting a version.
     */
    private val btnCancel: JButton? = null;

    /**
     * Okay button. Closes this window instance and parses the selected version.
     */
    private val btnOkay: JButton? = null;

    /**
     * Unfiltered text box for inputting the
     *
     * //TODO document filter
     * //TODO class for build version.
     */
    private val txtBuild: JTextField? = null;

    /**
        Value picked by the user; only set if user clicks OKAY.

        Default "-1.-1.-1.X".
     */
    private var version: tempVersion = tempVersion(-1,-1,-1,"X"); get() = field;

    /**
     * Default constructor.
     */
    init {
        /*
            Action listeners
         */
        btnOkay?.addActionListener { version = tempVersion(yearSpinner?.value as Int, monthSpinner?.value as Int, daySpinner?.value as Int, txtBuild?.text.toString()); close(); }
        btnCancel?.addActionListener { close(); }

        /*
            Spinner models
         */
        yearSpinner?.model = YEAR_SPINNER_MODEL
        monthSpinner?.model = MONTH_SPINNER_MODEL
        daySpinner?.model = DAY_SPINNER_MODEL

        /*
            Display window
         */
        contentPane = panelMain                                                                                         //Set frame content to form
        pack()                                                                                                          //Pack frame to form
        isVisible = true                                                                                                //Make visible
        setLocationRelativeTo(null)                                                                                     //Centre on screen
    }

    /**
     * Hides and disposes of the displayable frame.
     */
    fun close(){
        if (isVisible) {                                                                                                //If visible,
            isVisible = false                                                                                           //Make invisible
            dispose()                                                                                                   //Dispose of frame
        }                                                                                                               //Else return.
    }

    /**
     * Container for the static, final models to filter the front end spinners.
     */
    companion object {
        val YEAR_SPINNER_MODEL: SpinnerModel = SpinnerNumberModel(Calendar.getInstance()[Calendar.YEAR], 1970, Int.MAX_VALUE, 1)
        val MONTH_SPINNER_MODEL: SpinnerModel = SpinnerNumberModel(Calendar.getInstance()[Calendar.MONTH], 1, 12, 1)
        val DAY_SPINNER_MODEL: SpinnerModel = SpinnerNumberModel(Calendar.getInstance()[Calendar.DAY_OF_MONTH], 1, 32, 1)
    }
}