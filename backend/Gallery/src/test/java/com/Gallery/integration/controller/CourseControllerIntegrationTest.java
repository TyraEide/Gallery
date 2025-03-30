package com.Gallery.integration.controller;

import com.Gallery.controller.CourseController;
import com.Gallery.model.Course;
import com.Gallery.model.DiscussionTopic;
import com.Gallery.model.User;
import com.Gallery.service.CourseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class CourseControllerIntegrationTest {
    @MockitoBean private RestTemplate restTemplate;
    @Autowired private CourseService courseService;
    @Autowired private CourseController courseController;
    @Autowired MockMvc mockMvc;

    private static Map<String, Map<Course, List<DiscussionTopic>>> mockedApi;
    private static Map<String, String> jsonAnnouncements;

    @BeforeAll
    public static void initialize() throws JsonProcessingException {
        mockApiAndJsonCourses();
    }

    private static void mockApiAndJsonCourses() throws JsonProcessingException {
        Map<String, Map<Course, List<DiscussionTopic>>> api = new HashMap<>();
        Map<String, String> json = new HashMap<>();
        String[] institutions = new String[]{"uib", "hvl"};
        String[] courses = new String[]{"Course1", "Course2"};

        for (String institution : institutions) {
            api.put(institution, new HashMap<>());
            ObjectMapper mapper = new ObjectMapper();
            Set<String> announcementsJson = new HashSet<>();

            for (String course : courses) {
                Course courseObj = new Course();
                courseObj.setId(course);
                courseObj.setCourse_code(courseObj.getId());

                List<DiscussionTopic> announcements = new ArrayList<>();

                for (int k = 0; k < 3; k++) {
                    DiscussionTopic announcement = new DiscussionTopic();
                    announcement.setUrl(institution + "/" + course + "/" + k);
                    announcements.add(announcement);

                    String jsonAnnouncement = mapper.writeValueAsString(announcement);
                    JsonNode root = mapper.readTree(jsonAnnouncement);
                    ObjectNode objectNode = (ObjectNode) root;
                    objectNode.put("context_code", "course_"+courseObj.getId());
                    announcementsJson.add(mapper.writeValueAsString(objectNode));
                }
                Map<Course, List<DiscussionTopic>> entryApi = api.get(institution);
                entryApi.put(courseObj, announcements);
            }
            json.put(institution, announcementsJson.toString());
        }
        mockedApi = api;
        jsonAnnouncements = json;
    }

    @Test
    public void shouldReturnOKUponGettingAllAnnouncements() throws Exception {
        User user = new User();
        user.setHvlToken("hvlToken");
        user.setUibToken("uibToken");

        // Mock response uib
        List<Course> uibCourses = new ArrayList<>(mockedApi.get("uib").keySet());

        ResponseEntity<Course[]> uibResponseCourseList = new ResponseEntity<>(uibCourses.toArray(new Course[0]), HttpStatus.OK);
        when(restTemplate.exchange(
                eq("https://mitt.uib.no/api/v1/courses"),
                eq(HttpMethod.GET),
                any(),
                eq(Course[].class)
        )).thenReturn(uibResponseCourseList);

        ResponseEntity<String> uibResponse = new ResponseEntity<>(jsonAnnouncements.get("uib"), HttpStatus.OK);
        when(restTemplate.exchange(
                eq("https://mitt.uib.no/api/v1/announcements?context_codes[]={validCourseIds}"),
                eq(HttpMethod.GET),
                any(),
                eq(String.class),
                any(Object[].class)
        )).thenReturn(uibResponse);

        // Mock response hvl
        List<Course> hvlCourses = new ArrayList<>(mockedApi.get("hvl").keySet());

        ResponseEntity<Course[]> hvlResponseCourseList = new ResponseEntity<>(hvlCourses.toArray(new Course[0]), HttpStatus.OK);
        when(restTemplate.exchange(
                eq("https://hvl.instructure.com/api/v1/courses"),
                eq(HttpMethod.GET),
                any(),
                eq(Course[].class)
        )).thenReturn(hvlResponseCourseList);

        ResponseEntity<String> hvlResponse = new ResponseEntity<>(jsonAnnouncements.get("hvl"), HttpStatus.OK);
        when(restTemplate.exchange(
                eq("https://hvl.instructure.com/api/v1/announcements?context_codes[]={validCourseIds}"),
                eq(HttpMethod.GET),
                any(),
                eq(String.class),
                any(Object[].class)
        )).thenReturn(hvlResponse);

        mockMvc.perform(get("/api/courses/announcements")
                        .with(csrf())
                        .with(user(user)))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
