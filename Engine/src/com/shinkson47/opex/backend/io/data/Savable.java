package com.shinkson47.opex.backend.io.data;

import java.io.Serializable;

/**
 * Abstract saveable object.
 *
 * Used for save data.
 * Is serializable.
 * @since 2020.7.11.A
 * @version 2
 * @author Jordan Gray
 */
public abstract class Savable implements Serializable {

	/**
	 * Save this object.
	 */
	public abstract void Save();
}
