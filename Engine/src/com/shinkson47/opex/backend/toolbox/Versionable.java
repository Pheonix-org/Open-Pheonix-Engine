package com.shinkson47.opex.backend.toolbox;

/**
 * Versionable abstract.
 *
 * implements an abstract 'version' method, with the return type of Version.
 *
 * @since 2020.7.11.A
 * @version 1
 * @author Jordan Gray
 */
public abstract class Versionable {
    public abstract Version version();
}