package com.shinkson47.opex.backend.resources.pools;

/**
 * <h1></h1>
 * <br>
 * <p>
 *
 * </p>
 *
 * @author <a href="https://www.shinkson47.in">Jordan T. Gray on 07/12/2020</a>
 * @version 1
 * @since v1
 */
public interface KeySupplier<T>  {
    /**
     * <h2>Accepts <i>T</i>, and produces a string that will represent it's key.</h2>
     * @return A value that may be used as a Pool Key to represent that item.
     */
    String SupplyKey(T item);
}
