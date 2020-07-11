package com.shinkson47.opex.backend.toolbox.configuration;


/**
 *	OPEX configuration values.
 *
 * TODO Make this serializable.
 * @author Jordan Gray
 * @version 2020.7.6.A
 */
public interface OPEXConfig {

	// Error Management
	public static final byte ErrorMargin = 5;
	public static final boolean AllowEIS = true;
	public static final boolean AllowErrNotif = true;
	public static final boolean AllowCascadeDetection = true;
	public static final long MillisTollerance = 100;
	public static final byte CascadeTollerance = 3;
	public static final int DefaultResolutionX = 400;
	public static final int DefaultResolutionY = 400;

}
