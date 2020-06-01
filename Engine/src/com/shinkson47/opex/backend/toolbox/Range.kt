package com.shinkson47.opex.backend.toolbox

/**
 * Classification of a range between two integers.
 *
 * (from...to)
 * (0...10)
 *
 * @param From - beginning index of the range.
 * @param To -  ending index of the range
 */
class Range(From: Int, To: Int) {

    /**
     * Beginning index of the range.
     *
     * Gettable. Set only on class instantiation.
     * @return the beginning index of range.
     */
    private var from: Int = From; get() = from

    /**
     * Ending index of the range.
     *
     * Gettable. Set only on class instantiation.
     * @return the ending index of the range.
     */
    private var to: Int = To; get () = to

    /**
     * Determines if a number is within the range this class specifies.
     *
     * @return true if value is within range (from...to)
     * @return false if value is not within range (from...to)
     *
     * @param value - integer to check in range.
     */
    fun inRange(value: Int): Boolean = (value in from until to)


    companion object {
        /**
         * Determines if a number is within the range specified.
         * @return true if value is greater than min AND less than max.
         *
         * @param value - value to check if in range
         * @param from - beginning of range
         * @param to - end of range
         */
        fun inRange(value: Int, from: Int, to: Int): Boolean = (value in from until to);

        /**
         * Determines if a number is within the range specified.
         * @return true if value is greater than min AND less than max.
         *
         * @param value - value to check if in range
         * @param range - Range instance to check against.
         */
        fun inRange(value: Int, range: Range): Boolean = inRange(value, range.from, range.to)
    }
}

