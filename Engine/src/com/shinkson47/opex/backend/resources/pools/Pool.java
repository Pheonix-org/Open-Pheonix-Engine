package com.shinkson47.opex.backend.resources.pools;

import com.shinkson47.opex.backend.io.cjar.CJARMeta;
import com.shinkson47.opex.backend.io.cjar.ContentJavaArchive;
import jdk.management.resource.ResourceRequest;

import java.util.Hashtable;

/**
 * Stores item of type <i>T</i>, with an identifier.
 * @param <T>
 */
public class Pool<T> extends Hashtable<String, T> {

    private String Name;


    public Pool(String PoolName){
        Name = PoolName;
    }

    public String getName(){
        return Name;
    }

    /**
     * Tests if a resource ID exists within this pool.
     *
     * @param id Resource to look for.
     * @return true if exists, else false.
     */
    public boolean resourceExsists(ResourceID id){
        return super.getOrDefault(id, null) == null;
    }

//    @Override
//    public synchronized T put(ContentJavaArchive source, String key, T value) {
//        return super.put(ResourceID.toResourceID(this, ), value);
//    }
}
