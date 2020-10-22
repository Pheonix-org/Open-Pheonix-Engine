package com.shinkson47.opex.backend.resources.pools;

import com.shinkson47.opex.backend.io.cjar.ContentJavaArchive;
import com.shinkson47.opex.frontend.scene.Scene;

public class GlobalPools {

    public static final String CJAR_POOL_NAME = "CJARS";
    public static final String SCENE_POOL_NAME = "SCENES";

//    public static final String SCENE_POOL_NAME = "SFX";
//    public static final String SCENE_POOL_NAME = "AMBIENCE";
//    public static final String SCENE_POOL_NAME = "";
//
//    public static final Pool<ContentJavaArchive> CJAR = new Pool<>(CJAR_POOL_NAME);


    public static final Pool<ContentJavaArchive> CJAR = new Pool<>(CJAR_POOL_NAME);
    public static final Pool<Scene> SCENE_POOL = new Pool<>(SCENE_POOL_NAME);
}
