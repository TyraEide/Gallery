package com.Gallery.unit.model;

import com.Gallery.model.Enrollment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EnrollmentUnitTest {

    @Test
    public void shouldNotAllowNullInConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new Enrollment(null, null));
    }

    @Test
    public void shouldReturnValuesUponGet() {
        String type = "test_type";
        String user_id = "test_id";
        Enrollment e = new Enrollment(type, user_id);
        assertEquals(type, e.getType());
        assertEquals(user_id, e.getUserId());
    }


}
