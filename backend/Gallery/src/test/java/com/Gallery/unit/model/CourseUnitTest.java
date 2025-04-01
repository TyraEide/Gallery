package com.Gallery.unit.model;

import com.Gallery.model.Calendar;
import com.Gallery.model.Course;
import com.Gallery.model.DiscussionTopic;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;

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

        Course e = new Course();
        e.setId(id);
        e.setName(name);
        e.setCourse_code(course_code);
        e.setIs_public(is_public);
        e.setTimezone(timezone);

        assertEquals(id, e.getId());
        assertEquals(name, e.getName());
        assertEquals(course_code, e.getCourse_code());
        assertEquals(is_public, e.getIs_public());
        assertEquals(timezone, e.getTimezone());
    }

    @Test
    public void shouldSerializeFromJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new File("src/test/java/com/Gallery/unit/model/courseTestJson.json");
        Course course = objectMapper.readValue(jsonFile, Course.class);
        assertDoesNotThrow(() -> objectMapper.readValue(jsonFile, Course.class));

        HashMap<String, Object> jsonMap = objectMapper.readValue(jsonFile, HashMap.class);
        assertEquals(jsonMap.get("id").toString(), course.getId());
        assertEquals(jsonMap.get("name"), course.getName());
        assertEquals(jsonMap.get("timezone"), course.getTimezone());
        assertEquals(jsonMap.get("course_code"), course.getCourse_code());
        assertEquals(jsonMap.get("is_public"), course.getIs_public());
    }

}
