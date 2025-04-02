package com.Gallery.unit.service;

import com.Gallery.model.Course;
import com.Gallery.model.DiscussionTopic;
import com.Gallery.model.Institution;
import com.Gallery.model.User;
import com.Gallery.service.CourseService;
import com.Gallery.service.InstitutionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.sql.Array;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceUnitTest {
    @Mock RestTemplate restTemplate;
    @Mock
    InstitutionService institutionService;
    @InjectMocks CourseService courseService;
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

    @BeforeEach
    public void mockInstitutionService() {
        Institution uib = new Institution();
        uib.setFullName("The University of Bergen");
        uib.setShortName("uib");
        uib.setApiUrl("https://mitt.uib.no/api/v1");

        Institution hvl = new Institution();
        hvl.setFullName("The Western Norway University of Applied Sciences");
        hvl.setShortName("hvl");
        hvl.setApiUrl("https://hvl.instructure.com/api/v1");

        List<Institution> allInstitutions = List.of(uib, hvl);
        for (Institution institution : allInstitutions) {
            lenient().when(institutionService.getApiUrlByShortName(institution.getShortName())).thenReturn(institution.getApiUrl());
        }
    }

    @Test
    public void shouldReturnAPIAnnouncementsUponGetAnnouncements() throws JsonProcessingException {
        Map<Course, List<DiscussionTopic>> expected = mockedApi.get("uib");

        // Prep arguments
        String validInstitution = "uib";
        User authorizedUser = new User();
        authorizedUser.setUibToken("validToken");

        List<Course> courses = new ArrayList<>(mockedApi.get("uib").keySet());
        List<String> courseIds = new ArrayList<>();
        List<String> contextCodes = new ArrayList<>();
        for (int i = 0; i < courses.size(); i++) {
            courseIds.add(courses.get(i).getId());
            contextCodes.add("course_"+courses.get(i).getId());
            ResponseEntity<Course> response = new ResponseEntity<>(courses.get(i), HttpStatus.OK);
            when(restTemplate.exchange(
                    eq("https://mitt.uib.no/api/v1/courses/{courseId}"),
                    eq(HttpMethod.GET),
                    any(),
                    eq(Course.class),
                    eq(courseIds.get(i))
            )).thenReturn(response);
        }

        // Mock response
        String apiUrl = "https://mitt.uib.no/api/v1/announcements?context_codes[]={validCourseIds}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + authorizedUser.getUibToken());
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = new ResponseEntity<>(jsonAnnouncements.get("uib"), HttpStatus.OK);
        when(restTemplate.exchange(
                any(),
                eq(HttpMethod.GET),
                any(),
                eq(String.class),
                any(Object[].class)
        )).thenReturn(response);

        Map<String, Map<Course, List<DiscussionTopic>>> announcements = courseService.getAnnouncements(validInstitution, courseIds, authorizedUser);

        // Assertions
        verify(restTemplate).exchange(
                apiUrl,
                HttpMethod.GET,
                request,
                String.class,
                contextCodes.toString());

        Map<Course, List<DiscussionTopic>> actual = announcements.get("uib");
        for (Course course : expected.keySet()) {
            for (DiscussionTopic announcement : expected.get(course)) {
                assertTrue(actual.get(course).contains(announcement));
            }
        }
    }

    @Test
    public void shouldThrowErrorUponInvalidInstitution() {
        assertThrows(HttpClientErrorException.class, () -> courseService.getAnnouncements("invalidInstitution", null, new User()));
    }

    @Test
    public void shouldThrowErrorWhenUserHasNotSetToken() {
        assertThrows(HttpClientErrorException.class, () -> courseService.getAnnouncements("uib", null, new User()));
    }

    private void verifyCorrectBaseApiUrl(String institution, String institutionBaseUrl, List<String> courseIds, User authorizedUser) throws JsonProcessingException {
        ResponseEntity<String> response = new ResponseEntity<>("", HttpStatus.OK);
        lenient().when(restTemplate.exchange(
                any(),
                eq(HttpMethod.GET),
                any(),
                eq(String.class),
                any(Object[].class)
        )).thenReturn(response);

        String apiUrl = institutionBaseUrl + "/api/v1/announcements?context_codes[]={validCourseIds}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        if (institution.equals("uib")) {
            headers.set("Authorization", "Bearer " + authorizedUser.getUibToken());
        } else if (institution.equals("hvl")) {
            headers.set("Authorization", "Bearer " + authorizedUser.getHvlToken());
        }
        HttpEntity<String> request = new HttpEntity<>(headers);

        courseService.getAnnouncements(institution, courseIds, authorizedUser);

        verify(restTemplate).exchange(
                apiUrl,
                HttpMethod.GET,
                request,
                String.class,
                courseIds.toString()
        );
    }

    @Test
    public void shouldUseCorrectBaseApiUrlForValidInstitution() throws JsonProcessingException {
        User authorizedUser = new User();
        authorizedUser.setUibToken("uibToken");
        authorizedUser.setHvlToken("hvlToken");

        String uibBaseApiUrl = "https://mitt.uib.no";
        String hvlBaseApiUrl = "https://hvl.instructure.com";

        verifyCorrectBaseApiUrl("uib", uibBaseApiUrl, new ArrayList<>(), authorizedUser);
        verifyCorrectBaseApiUrl("hvl", hvlBaseApiUrl, new ArrayList<>(),  authorizedUser);
    }

    @Test
    public void shouldReturnAllAnnouncementsForAllInstitutionsUponGetAllAnnouncements() throws JsonProcessingException {
        // Prep arguments
        User authorizedUser = new User();
        authorizedUser.setUibToken("uibToken");
        authorizedUser.setHvlToken("hvlToken");

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

        // Perform getAllAnnouncements
        Map<String, Map<Course, List<DiscussionTopic>>> result = courseService.getAllAnnouncements(authorizedUser);

        // Validate result
        List<String> institutions = List.of("uib", "hvl");
        for (String institution : institutions) {
            Map<Course, List<DiscussionTopic>> expected = mockedApi.get(institution);
            Map<Course, List<DiscussionTopic>> actual = result.get(institution);
            for (Course course : mockedApi.get(institution).keySet()) {
                for (DiscussionTopic announcement : expected.get(course)) {
                    assertTrue(actual.get(course).contains(announcement));
                }
            }
        }
    }
}
