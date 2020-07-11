package com.shinkson47.opex.backend.io.cjar;

import com.shinkson47.opex.backend.runtime.environment.OPEX;
import com.shinkson47.opex.backend.toolbox.Version;

import java.io.Serializable;


/**
 * Serializable meta data container for CJAR's.
 *
 * A filled instance is serialized and stored within a cjar.
 */
public class ContentJavaArchive implements Serializable {

    /**
     * Determines the minimum engine version required.
     *
     * May be made obselete by using cjar version.
     */
    public Version minimumOPEXVersion = null;

    /**
     * Version of the cjar package standard.
     */
    public static final String cjarVersion = "2020.6.23.A";

    public Version minimumGameVersion = null;

    /**
     * Name of the main class which this package is intended for,
     * i.e the game a dlc is intended for.
     *
     * Name must be simple class name used in the OPEX engine,
     * of the class which extends OPEXGame.
     */
    public String mainClassName = "";

    public ContentJavaArchive(Version EngineVersion, Version GameVersion, String MainClassName){
        minimumOPEXVersion = EngineVersion;
        minimumGameVersion = GameVersion;
        mainClassName = MainClassName;
    }

    public ContentJavaArchive(Version GameVersion, String MainClassName){
        this(OPEX.getEngineSuper().VERSION(), GameVersion, MainClassName);
    }

    public ContentJavaArchive(){
        this(null, "");
    }

}
