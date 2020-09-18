package com.shinkson47.visual.fxml.controller;

import com.shinkson47.opex.backend.runtime.errormanagement.EMSHelper;
import com.shinkson47.opex.frontend.fxml.FXMLController;
import com.shinkson47.visual.fxml.panels.PanelLoadList;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.TilePane;
import sun.plugin.dom.exception.InvalidStateException;

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
    public ScrollPane PANEL_VIEW;
    //#endregion

    public static PanelTab currentTabController;

    /**
     * Current global Main controller instance.
     * @apiNote BEFORE FXMLMain#PostLoad, THIS REFERENCES THE DUMMY CONTROLLER, NOT MAIN.
     */
    public static Main Main = new Main();

    /**
     * Location of the FXML document for the Main OPEX Visual window
     */
    static final String MAIN_FXML = visual.LOCATION_PREFIX + "main.fxml";

    /**
     * List of all available panels.
     * <p>
     * final; prevents reloading or concurrent instances of panels.
     */
    protected static ArrayList<Panel> PANELS;

    public static ArrayList<Panel> getPANELS(){
        return PANELS;
    }

    private static ArrayList<Panel> createPanels() {
        ArrayList<Panel> panels = new ArrayList<>();

        for (PanelLoadList panel : PanelLoadList.values()) {
            try {
                panels.add(((Panel) FXMLController.create(visual.LOCATION_PREFIX + "panels/" + panel.name() + ".fxml", Main))
                            .setPanelName(panel.name()));
            } catch (IOException | InvalidStateException e) {
                EMSHelper.handleException(e);
            }
        }
        return panels;
    }

    /**
     * List of all tabs for all available panels.
     */
    protected static ArrayList<PanelTab> TABS;

    public static ArrayList<PanelTab> getTABS(){
        return TABS;
    }

    /**
     * @apiNote Relies on population of this#PANELS. Ensure createPanels has been correctly invoked first.
     * @return
     */
    private static ArrayList<PanelTab> createTabs() {
        ArrayList<PanelTab> tabs = new ArrayList<>();
        for (Panel panel : PANELS) {
            try {
                tabs.add(
                        ((PanelTab) FXMLController.create(PanelTab.PANEL_TAB_FXML, Main))
                            .setText(panel.getPanelName()));
            } catch (IOException e) {
                EMSHelper.handleException(e);
            }
        }
        return tabs;
    }

    public Main() {
        super(MAIN_FXML);
    }



    /**
     * Brings focus to the panel at <c>index</c>
     * Call is ignored with no effect if index is not in range.
     * @param index of the panel to activate.
     */
    public void selectPanel(int index) {
        if (index < 0 || index > PANELS.size()) return;
        if (currentTabController != null)   // Only useful on first call. Can this be improved?
            currentTabController.getAnchorPane().setDisable(false);//TODO this can be abstracted

        PANEL_VIEW.setContent((PANELS.get(index).getAnchorPane()));
        currentTabController = TABS.get(index);
        currentTabController.getAnchorPane().setDisable(true); //TODO this can be abstracted
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

    public void postLoad(){
        PANELS = createPanels();
        TABS = createTabs();
        renderTabs();
        selectPanel(0);
    }
}