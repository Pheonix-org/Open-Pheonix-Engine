package com.shinkson47.opex.frontend.rendering.renderable;

import com.shinkson47.opex.frontend.rendering.Rendered.Rendered;

public interface Renderable<T extends Rendered> {
    public T render();
}
