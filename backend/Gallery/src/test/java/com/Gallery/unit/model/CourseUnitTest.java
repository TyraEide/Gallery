package com.Gallery.unit.model;

import com.Gallery.model.Calendar;
import com.Gallery.model.Course;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CourseUnitTest {

    @Test
    public void shouldReturnSetUponGet() {
        String id = "1";
        String name = "TST V25 / Test Course";
        String course_code = "TST";
        boolean is_public = true;
        Calendar calendar = new Calendar("example.ics");
        String timezone = "Europe/Copenhagen";

        Course e = new Course("1");
        e.setName(name);
        e.setCourseCode(course_code);
        e.setPublic(is_public);
        e.setCalendar(calendar);
        e.setTimezone(timezone);

        assertEquals(id, e.getId());
        assertEquals(name, e.getName());
        assertEquals(course_code, e.getCourseCode());
        assertEquals(is_public, e.isPublic());
        assertEquals(calendar, e.getCalendar());
        assertEquals(timezone, e.getTimezone());
    }

}
