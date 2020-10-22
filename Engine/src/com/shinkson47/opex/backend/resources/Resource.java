package com.shinkson47.opex.backend.resources;

import com.shinkson47.opex.backend.resources.pools.Pool;
import com.shinkson47.opex.backend.resources.pools.ResourceID;

/**
 *
 */
public abstract class Resource {

    private ResourceID ID;

    public ResourceID getID() {
        return ID;
    }

    public Resource(ResourceID CJAR, Pool parentPool, String name) {
        ID = new ResourceID(CJAR, parentPool, name);
    }
}
