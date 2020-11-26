package com.shinkson47.opex.backend.toolbox;


import java.io.Serializable;

public final class Version implements Serializable {
    private String value;
    private int year;
    private int month;
    private int day;
    private String build;
    private static final Range YEAR_RANGE = new Range(1970, Integer.MAX_VALUE);
    private static final Range MONTH_RANGE = new Range(1, 12);
    private static final Range DAY_RANGE = new Range(1, 31);
    public final String getValue() {
        return this.value;
    }

    public final void setValue(String var1) {
        assert var1 != null;
        this.value = var1;
    }

    public final int getYear() {
        return this.getYear();
    }

    public final void setYear(int value) {
        if (YEAR_RANGE.inRange(value)) {
            this.year = value;
        }

        this.update();
    }

    public final int getMonth() {
        return this.getMonth();
    }

    public final void setMonth(int value) {
        if (MONTH_RANGE.inRange(value)) {
            this.month = value;
        }

        this.update();
    }

    public final int getDay() {
        return this.getDay();
    }

    public final void setDay(int value) {
        if (DAY_RANGE.inRange(value)) {
            this.day = value;
        }

        this.update();
    }

    public final String getBuild() {
        return this.getBuild();
    }

    public final void setBuild(String value) {
        assert value != null;
        this.build = value;
        this.update();
    }

    public final void update() {
        this.value = parse(this.getYear(), this.getMonth(), this.getDay(), this.getBuild());
    }

    public Version(int Year, int Month, int Day, String Build) {
        this.value = "0.0.0.x";
        this.year = Year;
        this.month = Month;
        this.day = Day;
        this.build = Build;
    }

        public final String parse(int year, int month, int day, String build) {
            return "" + year + '.' + month + '.' + day + '.' + build;
        }
        public final Range getYEAR_RANGE() {
            return Version.YEAR_RANGE;
        }

        public final Range getMONTH_RANGE() {
            return Version.MONTH_RANGE;
        }

        public final Range getDAY_RANGE() {
            return Version.DAY_RANGE;
        }
}
