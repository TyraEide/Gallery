package com.Gallery.unit.controller;

import com.Gallery.controller.CourseController;
import com.Gallery.model.Course;
import com.Gallery.model.DiscussionTopic;
import com.Gallery.model.User;
import com.Gallery.service.CourseService;
import com.Gallery.unit.service.CourseServiceUnitTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourseControllerUnitTest {
    @Mock private CourseService courseService;
    @InjectMocks private CourseController courseController;
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
    public void shouldReturnAnnouncementsWhenValidCourseIdsAndInstitution() throws JsonProcessingException {
        // Expected response
        Map<String, Map<Course, List<DiscussionTopic>>> expected = mockedApi;
        expected.remove("hvl");

        // Prep arguments
        String validInstitution = "uib";
        List<String> validCourseIds = List.of("Course1, Course2");
        User authorizedUser = new User();
        authorizedUser.setUibToken("validToken");

        when(courseService.getAnnouncements(validInstitution, validCourseIds, authorizedUser)).thenReturn(expected);
        DiscussionTopic[] result = courseController.getAllAnnouncements(authorizedUser);
        verify(courseService).getAnnouncements(validInstitution, validCourseIds, authorizedUser);
        assertEquals(expected, result);
    }
}
