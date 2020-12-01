package com.shinkson47.opex.backend.toolbox;

import java.io.Serializable;


/**
 * Temporary classification for OPEX standardised date versioning.
 *
 * @see . OPEXDocs - Versioning standard.
 *
 * Versions are specified in the following format:
 *        [year][month][day][daily build]
* i.e:   2020.5.12.A, 2020.5.12.B .. 2020.5.12.AA, 2020.5.12.AB etc.
 *
 * //TODO support for semantic versioning
 * //TODO Version superclass, date version, semantic versioning sub class
 */
public class Version implements Serializable {

    public Version(int year, int month, int day, String build) {
        setYear(year);
        setMonth(month);
        setDay(day);
        setBuild(build);
    }

    /**
     * Value of the version
     */
    private String value = "0.0.0.x";

    /**
     * version year.
     * 
     * Settable within the range 1970 to Int.max.
     * Setting out of range will result in no effect.
     */
    private int year;
    //get() = year; set(value) { if (YEAR_RANGE.inRange(value)) field = value; update() }

    /**
     * version month
     */
    private int month;

    /**
     * version day
     */
    private int day;


    /**
     * Version build string
     */
    private String build;
    //


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        if (YEAR_RANGE.inRange(year)) {
            this.year = year;
            update();
        }
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        if (MONTH_RANGE.inRange(month)) {
            this.month = month;
            update();
        }
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        if (DAY_RANGE.inRange(day)) {
            this.day = day;
            update();
        }
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
        update();
    }

    /**
     * Create a version with a known value.
     *
     * parses and updates version value with parsed data.
     */

    public void update(){
        value = parse(year, month, day, build);
    }


    /**
     * Parse date and build data into a version string
     *
     * @return version string in the format:
     * "$year.$month.$day.$build"
     */
    public static String parse(int year, int month, int day, String build) {
        return year + "." +  month + "." +  day + "." + build;
    }

    /**
     * Permittable year range for a version
     */
    public static final Range YEAR_RANGE = new Range(1970, Integer.MAX_VALUE);

    /**
     * Permittable month range for a version
     */
    public static final Range MONTH_RANGE = new Range(1, 12);

    /**
     * Permittable day range for a version
     */
    public static final Range DAY_RANGE = new Range(1, 31);

}

