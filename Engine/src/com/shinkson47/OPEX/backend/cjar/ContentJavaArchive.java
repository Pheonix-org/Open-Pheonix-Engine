package com.shinkson47.OPEX.backend.cjar;

import com.shinkson47.OPEX.backend.runtime.engine.OPEX;
import com.sun.xml.internal.ws.api.pipe.Engine;
import sun.applet.Main;

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
    public String minimumOPEXVersion = "X.X.X.X.X";

    /**
     * Version of the cjar package standard.
     */
    public static final String cjarVersion = "2020.6.23.A";

    public String minimumGameVersion = "X.X.X.X.X";

    /**
     * Name of the main class which this package is intended for,
     * i.e the game a dlc is intended for.
     *
     * Name must be simple class name used in the OPEX engine,
     * of the class which extends OPEXGame.
     */
    public String mainClassName = "";

    public ContentJavaArchive(String EngineVersion, String GameVersion, String MainClassName){
        minimumOPEXVersion = EngineVersion;
        minimumGameVersion = GameVersion;
        mainClassName = MainClassName;
    }

    public ContentJavaArchive(String GameVersion, String MainClassName){
        this(OPEX.getEngineSuper().VERSION(), GameVersion, MainClassName);
    }

    public ContentJavaArchive(){
        this("", "");
    }

}
