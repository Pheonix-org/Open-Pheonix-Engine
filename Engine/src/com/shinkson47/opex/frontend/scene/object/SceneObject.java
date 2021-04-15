package com.shinkson47.opex.frontend.scene.object;

import com.shinkson47.opex.frontend.rendering.Rendered.Rendered;
import com.shinkson47.opex.frontend.rendering.renderable.Renderable;
import java.io.Closeable;
import java.io.IOException;

public final class SceneObject implements Renderable, Closeable{
    @Override
    public void close() throws IOException {

    }

    @Override
    public Rendered render() {
        return null;
    }
}
