package com.shinkson47.opex.frontend.scene;

import com.shinkson47.opex.backend.resources.Resource;
import com.shinkson47.opex.backend.resources.pools.GlobalPools;
import com.shinkson47.opex.backend.resources.pools.Pool;
import com.shinkson47.opex.backend.resources.pools.ResourceID;
import com.shinkson47.opex.backend.resources.pools.sceneobject.ISceneObjectPoolAccessor;
import com.shinkson47.opex.frontend.rendering.Rendered.RenderedFrame;
import com.shinkson47.opex.frontend.rendering.renderable.Renderable;
import com.shinkson47.opex.frontend.scene.layer.SceneLayer;
import jdk.nashorn.internal.objects.Global;

/**
 * An in-game scene.
 *
 * Contains all SceneLayers within any given scene.
 *
 * @author <a href="https://www.shinkson47.in">Jordan T. Gray</a>
 * @version 1
 */
public class Scene extends Resource implements ISceneObjectPoolAccessor, Renderable<RenderedFrame> {

    public Scene(ResourceID CJAR, String name) {
        super(CJAR, GlobalPools.SCENE_POOL, name);
    }


    //#region layers
    /**
     * All layers in this scene.
     *
     * Layer keys are numeric values from 1, indicating layer order -
     * where layer 1 represents the bottom most layer.
     */
    private Pool<SceneLayer> layers = new Pool<>(super.getID() + "_" + GlobalPools.SCENE_POOL_NAME);



    /**
     * Adds a new layer to the top of the render stack.
     *
     * @apiNote Add layers in the order you wish them to appear in the scene.
                The first layer will bottommost, the newest will appear on top.
                The order of layers cannot be modified.
     * @param layer The layer to add.
     */
    public void addLayer(SceneLayer layer) {
        layers.put(String.valueOf(layers.size() + 1), layer);
    }
    //#endregion


    /**
     * Renders the scene
     * @return the frame rendered from this scene.
     */
    @Override
    public RenderedFrame render() {
        return null;
    }
}
