package com.Gallery.unit.model;

import com.Gallery.model.CanvasUser;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CanvasUserUnitTest {

    @Test
    public void shouldReturnSetUponGet() {
        String id = "1";
        String name = "Test Testy";
        String sortable_name = "Testy, Test";
        String first_name = "Test";
        String last_name = "Testy";
        String avatar_url = "test.png";
        String effective_locale = "nn";

        CanvasUser e = new CanvasUser();

        e.setId(id);
        e.setName(name);
        e.setSortableName(sortable_name);
        e.setFirstName(first_name);
        e.setLastName(last_name);
        e.setAvatarUrl(avatar_url);
        e.setEffectiveLocale(effective_locale);

        assertEquals(id, e.getId());
        assertEquals(name, e.getName());
        assertEquals(sortable_name, e.getSortableName());
        assertEquals(first_name, e.getFirstName());
        assertEquals(last_name, e.getLastName());
        assertEquals(avatar_url, e.getAvatarUrl());
        assertEquals(effective_locale, e.getEffectiveLocale());
    }

}
