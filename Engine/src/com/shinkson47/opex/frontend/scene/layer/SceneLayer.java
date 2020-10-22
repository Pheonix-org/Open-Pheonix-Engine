package com.shinkson47.opex.frontend.scene.layer;

import com.shinkson47.opex.frontend.rendering.Rendered.RenderedLayer;
import com.shinkson47.opex.frontend.rendering.renderable.RenderableLayer;
import com.shinkson47.opex.frontend.scene.object.SceneObject;

public abstract class SceneLayer<T extends SceneObject> implements RenderableLayer {


    @Override
    public RenderedLayer render() {
        return null;
    }
}
