package com.shinkson47.opex.backend.toolbox;

public final class Range {

    private int value;
    private int from;
    private int to;

    public int getValue() {
        return value;
    }

    private final int getFrom() {
        return this.from;
    }

    private final int getTo() {
        return this.to;
    }

    public final boolean inRange() {
        return inRange(this);
    }

    public final boolean inRange(int i){
        return inRange(i, this);
    }

    public Range(int From, int To) {
        this.from = From;
        this.to = To;
    }

    public static boolean inRange(int value, int from, int to) {
        return value <= to && value >= from;
    }

    public static boolean inRange(int value, Range range) {
        return inRange(value, range.getFrom(), range.getTo());
    }

    public static boolean inRange(Range range) {
        return inRange(range.getValue(), range.getFrom(), range.getTo());
    }

}

