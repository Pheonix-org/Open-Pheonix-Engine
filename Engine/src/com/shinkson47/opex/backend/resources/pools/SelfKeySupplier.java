package com.shinkson47.opex.backend.resources.pools;

/**
 * <h1></h1>
 * <br>
 * <p>
 *
 * </p>
 *
 * @author <a href="https://www.shinkson47.in">Jordan T. Gray on 09/12/2020</a>
 * @version 1
 * @since v1
 */
public interface SelfKeySupplier<T> extends KeySupplier<T> {
    String SupplyKey();
}