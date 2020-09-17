package com.shinkson47.visual.fxml.controller;

import com.shinkson47.opex.backend.runtime.errormanagement.EMSHelper;
import com.shinkson47.opex.frontend.fxml.FXMLController;
import com.shinkson47.visual.fxml.panels.PanelLoadList;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Controller for the main OPEX Visual Window.
 *
 * @author gordie
 * @version 1.0.0
 */
public class Main extends FXMLController {

    //#region FXML references
    /**
     * Tile Panel containing a list of all panels available.
     */
    @FXML
    public TilePane PANEL_TAB_LIST;

    /**
     * View to display actual visual panels in.
     */
    @FXML
    public AnchorPane PANEL_VIEW;
    //#endregion

    /**
     * Current global Main controller instance.
     * May be null before startup is complete.
     */
    public static Main Main;

    /**
     * Location of the FXML document for the Main OPEX Visual window
     */
    static final String MAIN_FXML = BootableVisual.LOCATION_PREFIX + "main.fxml";

    /**
     * List of all available panels.
     * <p>
     * final; prevents reloading or concurrent instances of panels.
     */
    public static final ArrayList<FXMLController> PANELS = createPanels();

    private static ArrayList<FXMLController> createPanels() {
        ArrayList<FXMLController> panels = new ArrayList<>();

        for (PanelLoadList panel : PanelLoadList.values()) {
            try {
                panels.add(FXMLController.create(BootableVisual.LOCATION_PREFIX + "panels/" + panel.name() + ".fxml", Main));
            } catch (IOException e) {
                EMSHelper.handleException(e);
            }
        }
        return panels;
    }

    /**
     * List of all tabs for all available panels.
     */
    public static final ArrayList<FXMLController> TABS = createTabs();

    private static ArrayList<FXMLController> createTabs() {
        ArrayList<FXMLController> tabs = new ArrayList<>();
        for (FXMLController panel : PANELS) {
            try {
                tabs.add(FXMLController.create(PanelTab.PANEL_TAB_FXML, Main));
            } catch (IOException e) {
                EMSHelper.handleException(e);
            }
        }
        return tabs;
    }


    public Main() {
        super(MAIN_FXML);
    }

    public void firstRender(){
        renderTabs();
        selectPanel(0);
    }

    /**
     * Brings focus to the panel at <c>index</c>
     * Call is ignored with no effect if index is not in range.
     * @param index of the panel to activate.
     */
    public void selectPanel(int index) {
        if (index < 0 || index > PANELS.size()) return;
        PANEL_VIEW.getChildren().clear();
        PANEL_VIEW.getChildren().add(PANELS.get(index));
    }

    /**
     * Clears any existing elements, then adds all tab to the tab pane.
     */
    private void renderTabs() {
        ObservableList<Node> children = PANEL_TAB_LIST.getChildren();
        children.clear();
        for (FXMLController tab : TABS)
            children.add(tab.getAnchorPane());
    }

}