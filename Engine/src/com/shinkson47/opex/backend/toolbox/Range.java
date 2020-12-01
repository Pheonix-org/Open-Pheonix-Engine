package com.shinkson47.opex.backend.toolbox;

/**
 * Classification of a range between two integers.
 *
 * (from...to)
 * (0...10)

 */
public class Range {

    public Range(int from, int to){
        setFrom(from);
        setTo(to);
    }

    /**
     * Beginning index of the range.
     *
     * Gettable. Set only on class instantiation.
     * @return the beginning index of range.
     */
    private int from;

    /**
     * Ending index of the range.
     *
     * Gettable. Set only on class instantiation.
     * @return the ending index of the range.
     */
    private int to;

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    /**
     * Determines if a number is within the range this class specifies.
     *
     * @return true if value is within range (from...to)
     * @return false if value is not within range (from...to)
     *
     * @param value - integer to check in range.
     */
    public boolean inRange(int value){
      return inRange(value, this);
    }


    /**
     * Determines if a number is within the range specified.
     * @return true if value is greater than min AND less than max.
     *
     * @param value - value to check if in range
     * @param from - beginning of range
     * @param to - end of range
     */
    public static boolean inRange(int value, int from, int to){
        return (value >= from && value <= to);
    }

    /**
     * Determines if a number is within the range specified.
     * @return true if value is greater than min AND less than max.
     *
     * @param value - value to check if in range
     * @param range - Range instance to check against.
     */
    public static boolean inRange(int value, Range range) {
        return inRange(value, range.getFrom(), range.getTo());
    }


}

