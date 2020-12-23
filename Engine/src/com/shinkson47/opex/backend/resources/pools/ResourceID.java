package com.shinkson47.opex.backend.resources.pools;

import com.sun.xml.internal.bind.v2.model.core.ID;

/**
 *
 */
public class ResourceID {

    public static final char ID_SEPERATOR = '_';

    private String resourceName;
    private Pool parentPool;
    private ResourceID sourceCJAR;
    private String publicID;

    public ResourceID(ResourceID _sourceCJAR, Pool pool, String _resourceName){
        parentPool = pool;
        sourceCJAR = _sourceCJAR;
        resourceName = _resourceName;
        updateID();
    }

    private String updateID() {
        return publicID = sourceCJAR.getResourceName() + ID_SEPERATOR +
                    parentPool.getName() + ID_SEPERATOR
                    + resourceName;
    }

    public String getResourceName() {
        return resourceName;
    }

    public Pool getParentPool() {
        return parentPool;
    }

    public ResourceID getSourceCJAR() {
        return sourceCJAR;
    }

    public String getPublicID() {
        return publicID;
    }
}
