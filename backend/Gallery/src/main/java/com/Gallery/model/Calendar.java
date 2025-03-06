package com.Gallery.model;

public class Calendar {
    private final String ics;

    public Calendar(String ics) {
        if (ics == null) {
            throw new IllegalArgumentException("Calendar cannot have null ics.");
        }
        this.ics = ics;
    }

    public String getIcs() {
        return ics;
    }
}
