package com.shinkson47.opex.backend.resources.pools;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * <h2>A named pool if values identified by keys implemented with a HashTable</h2>
 * Stores item of type <i>T</i>, with an identifier.
 * <br>
 * Supports getting and putting values using {@link ArrayList}'s and {@link KeySupplier}'s
 *
 * @param <T>
 */
public class Pool<T> extends Hashtable<String, T> {

    /**
     * <h2>The name of this pool.</h2>
     */
    private String Name;

    //#region construction
    public Pool(String PoolName){
        Name = PoolName;
    }

    public String getName(){
        return Name;
    }
    //#endregion construction

    /**
     * <h2>Tests if a resource ID exists within this pool.</h2>
     *
     * @param id Resource to look for.
     * @return true if exists, else false.
     */
    public boolean resourceExsists(ResourceID id){
        return super.getOrDefault(id, null) == null;
    }

    /**
     * <h2>Puts all values in an array list</h2>
     * Uses the provided {@link KeySupplier} to generate keys.
     * @param supplier The {@link KeySupplier} that generates keys for <i>T</i>
     * @param list The list of items to <i>put</i>.
     */
    public void putArrayList(KeySupplier<T> supplier, ArrayList<T> list){
        for (T item : list)
            put(supplier.SupplyKey(item), item);
    }

    /**
     * <h2>Creates an array list containing all values in the pool.</h2>
     * Does not include keys.
     * @return A new array list containing every value in this pool.
     */
    public ArrayList<T> valuesAsArrayList(){
        return new ArrayList<>(values());
    }
}
