package com.shinkson47.opex.frontend.fxml;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * Abstraction for all Controller classes for FXML scenes.
 *
 * @author gordie
 * @version 1
 */
public abstract class FXMLController extends Parent {

    //#region properties
    /**
     * Path to the FXML definition this controller commands.
     */
    private String FXML = "";

    /**
     * Local FXML Loader for creating and loading FXML documents
     * into this scene.
     */
    protected FXMLLoader loader;

    /**
     * FXML controller to which this JFX scene is a child.
     * May be null.
     */
    private FXMLController parentController;

    /**
     * Anchor pane containing all UI content of this FXML scene.
     * For easy direct references to front end objects.
     */
    private AnchorPane anchorPane;

    //#endregion

    //#region getter / setter

    public String getFXML(){
        return FXML;
    }

    /**
     * returns the parent Controller cast to the most common parent; pos.
     * @return this#parentController cast to pos.
     */
    public <T> T castParent(){
        return (T) parentController;
    }

    /**
     * @return returns this#parentController
     */
    public FXMLController getParentController() {
        return parentController;
    }

    /**
     * @return the anchor pane object containing all content of this FXML scene.
     */
    public AnchorPane getAnchorPane(){
        return  anchorPane;
    }
    //#endregion

    //#region Constructor
    /**
     * Creates a new FXML controller.
     * Does NOT load FXML or create scene.
     * @param _FXML path to the FXML scene this controller commands.
     */
    public FXMLController (String _FXML){
        this(_FXML, null);
    }

    /**
     * Creates a new FXML controller.
     * Does NOT load FXML or create scene.
     * @param _FXML path to the FXML scene this controller commands.
     * @param _parentController the FXML controller to which this is a child.
     */
    public FXMLController (String _FXML, FXMLController _parentController) {
        FXML = _FXML;
        parentController = _parentController;
    }
    //#endregion

    //#region methods
    /**
     * Creates a new controlled anchorPane from an instance of FXML.
     *
     * - Creates loader
     *      - parses FXML (this.MENU_TILE_FXML)
     *      - creates an instance of controller.menuTile (No argument constructor)
     *      - creates UI elements from parsed FXML,
     *          - injecting elements with matching fx:id's into the controller.
     *      - registers event handlers
     *      - invokes initialise on the controller
     *      - returns UI hierarchy of the tile.
     * - Gets controller
     * - Sets item of representation
     *      - Updates UI elements accordingly
     * - returns resulting instance.
     *
     * @return A new instance of AnchorPane, following the FXML design for a menu tile that represents the provided item.
     */
    public FXMLController _clone() throws IOException {
        return create(FXML, parentController);
    }
    //#endregion


    //#region Static Utilities
    /**
     * Clones an fxml controller, and its scene, from scratch.
     * @param controller to copy.
     * @returns the cloned FXMLController.
     * Returned controller may not have a scene loaded, if the FXML data on the controller
     * parsed was not adequate.
     */
    public static FXMLController _clone(FXMLController controller) throws IOException {
        return create(controller.FXML, controller.parentController);
    }

    /**
     * Creates a new FXML Controller that's populated with the FXML scene,
     *
     * - Creates loader
     *      - parses FXML (this.MENU_TILE_FXML)
     *      - creates an instance of controller.menuTile (No argument constructor)
     *      - creates UI elements from parsed FXML,
     *          - injecting elements with matching fx:id's into the controller.
     *      - registers event handlers
     *      - invokes initialise on the controller
     *      - returns UI hierarchy of the tile.
     * - Gets controller
     * - Sets item of representation
     *      - Updates UI elements accordingly
     * - returns resulting instance.
     *
     * @return A new instance of AnchorPane, following the FXML design for a menu tile that represents the provided item.
     * @throws IOException
     */
    public static FXMLController create(String FXML, FXMLController _parentController) throws IOException, IllegalStateException {
        FXMLLoader loader = new FXMLLoader(FXMLController.class.getResource(FXML));                                           // Create a loader with the FXML reference.
        // I need to create an instance of this JUST to get the controller of the new pane. This should be statically available.
        AnchorPane ap = loader.load();                                                                                  // parse FXML to create AP. This must be done before getting the controller, otherwise this could be collapsed to contoller.ap = loader.getcontroller

        FXMLController controller = loader.getController();                                                             // Get controller to set the item the pane represents.
        controller.anchorPane = ap;                                                                                     // store the anchor pane and controller for future reference.
        controller.parentController = _parentController;

        controller.onCreate();
        return controller;                                                                                              // Return newly created menu tile.
    }

    /**
     * Overide with actions to perform after FXML creation.
     */
    public void onCreate() {

    }


    //#endregion
}
