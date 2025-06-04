package com.Gallery.unit.model;

import com.Gallery.model.Calendar;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CalendarUnitTest {

    @Test
    public void shouldReturnValueUponGet() {
        String ics = "example.ics";
        Calendar e = new Calendar(ics);
        assertEquals(ics, e.getIcs());
    }
    @Test
    public void shouldThrowExceptionForNullIcsOnConstruction() {
        assertThrows(IllegalArgumentException.class, () -> new Calendar(null));
    }
}
