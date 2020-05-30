package com.shinkson47.OPEX.backend.toolbox


/**
 * Temporary classification for OPEX standardised date versioning.
 *
 * @see OPEXDocs - Versioning standard.
 *
 * Versions are specified in the following format:
 *        [year][month][day][daily build]
* i.e:   2020.5.12.A, 2020.5.12.B .. 2020.5.12.AA, 2020.5.12.AB etc.
 *
 * //TODO support for semantic versioning
 * //TODO Version superclass, date version, semantic versioning sub class
 */
class Version(Year: Int, Month: Int, Day: Int, Build: String) {
    /**
     * Value of the version
     */
    var value: String = "0.0.0.x";  get() = value

    /**
     * version year.
     * 
     * Settable within the range 1970 to Int.max.
     * Setting out of range will result in no effect.
     */
    var year: Int = Year; get() = year; set(value) { if (YEAR_RANGE.inRange(value)) field = value; update() }

    /**
     * version month
     */
    var month: Int = Month; get() = month; set(value) { if(MONTH_RANGE.inRange(value)) field = value; update() }

    /**
     * version day
     */
    var day: Int = Day; get() = day; set(value) { if (DAY_RANGE.inRange(value)) field = value; update() }

    /**
     * Version build string
     */
    var build: String = Build; get() = build; set(value) { field = value; update() }

    /**
     * Create a version with a known value.
     *
     * parses and updates version value with parsed data.
     */

    fun update(){
        value = parse(year, month, day, build)
    }

    companion object {
        /**
         * Parse date and build data into a version string
         *
         * @return version string in the format:
         * "$year.$month.$day.$build"
         */
        fun parse(year: Int, month: Int, day: Int, build: String): String = "$year.$month.$day.$build"

        /**
         * Permittable year range for a version
         */
        val YEAR_RANGE: Range = Range(1970, Int.MAX_VALUE)

        /**
         * Permittable month range for a version
         */
        val MONTH_RANGE: Range = Range(1, 12)

        /**
         * Permittable day range for a version
         */
        val DAY_RANGE: Range = Range(1, 31)
    }
}

