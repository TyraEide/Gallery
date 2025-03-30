package com.Gallery.integration.controller;

import com.Gallery.controller.CourseController;
import com.Gallery.model.DiscussionTopic;
import com.Gallery.model.User;
import com.Gallery.service.CourseService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class CourseControllerIntegrationTest {
    @Mock private RestTemplate restTemplate;
    @InjectMocks private CourseService courseService;
    @Autowired private CourseController courseController;
    @Autowired MockMvc mockMvc;

    private static Map<String, Map<String, DiscussionTopic[]>> mockedApi;

    @BeforeAll
    public static void mockApi() {
        Map<String, Map<String, DiscussionTopic[]>> api = new HashMap<>();
        String[] institutions = new String[]{"uib", "hvl"};
        String[] courses = new String[]{"Course1", "Course2"};

        for (int i = 0; i < institutions.length; i++) {
            api.put(institutions[i], new HashMap<>());
            for (int j = 0; j < courses.length; j++) {
                Map<String, DiscussionTopic[]> entry = api.get(institutions[i]);
                DiscussionTopic[] announcements = new DiscussionTopic[3];
                for (int k = 0; k < 3; k++) {
                    DiscussionTopic announcement = new DiscussionTopic();
                    announcement.setUrl(institutions[i] + "/" + courses[i] + "/" + k);
                    announcements[i] = announcement;
                }
                entry.put(courses[i], announcements);
            }
        }
        mockedApi = api;
    }

    @Test
    public void shouldReturnOKUponGettingAllAnnouncements() throws Exception {
        User user = new User();
        user.setHvlToken("hvlToken");
        user.setUibToken("uibToken");

        mockMvc.perform(get("/api/courses/announcements")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());
    }


}
