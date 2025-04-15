package com.Gallery.integration.controller;

import com.Gallery.controller.CourseController;
import com.Gallery.dto.UserRegistrationDTO;
import com.Gallery.model.*;
import com.Gallery.service.CourseService;
import com.Gallery.service.InstitutionService;
import com.Gallery.service.TokenService;
import com.Gallery.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
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
    @Autowired private InstitutionService institutionService;
    @Autowired private TokenService tokenService;
    @Autowired private UserService userService;
    @Autowired MockMvc mockMvc;

    private static List<Institution> institutionList;
    private static Map<String, Map<Course, List<DiscussionTopic>>> mockedApi;
    private static Map<String, String> jsonAnnouncements;
    private User user;

    @BeforeAll
    public static void initialize() throws JsonProcessingException {
        initializeInstitutionList();
        mockApiAndJsonCourses();
    }

    public static void initializeInstitutionList() {
        Institution uib = new Institution();
        uib.setFullName("The University of Bergen");
        uib.setShortName("uib");
        uib.setApiUrl("https://mitt.uib.no/api/v1");

        Institution hvl = new Institution();
        hvl.setFullName("The Western Norway University of Applied Sciences");
        hvl.setShortName("hvl");
        hvl.setApiUrl("https://hvl.instructure.com/api/v1");

        // Valid institutions return canvasToken
        institutionList = List.of(uib, hvl);
    }

    private static void mockApiAndJsonCourses() throws JsonProcessingException {
        Map<String, Map<Course, List<DiscussionTopic>>> api = new HashMap<>();
        Map<String, String> json = new HashMap<>();
        List<String> courses = List.of("Course1", "Course2");;

        for (Institution institution : institutionList) {
            String shortName = institution.getShortName();
            api.put(shortName, new HashMap<>());
            ObjectMapper mapper = new ObjectMapper();
            Set<String> announcementsJson = new HashSet<>();

            for (String course : courses) {
                Course courseObj = new Course();
                courseObj.setId(course);
                courseObj.setCourse_code(courseObj.getId());

                List<DiscussionTopic> announcements = new ArrayList<>();

                for (int k = 0; k < 3; k++) {
                    DiscussionTopic announcement = new DiscussionTopic();
                    announcement.setUrl(shortName + "/" + course + "/" + k);
                    announcements.add(announcement);

                    String jsonAnnouncement = mapper.writeValueAsString(announcement);
                    JsonNode root = mapper.readTree(jsonAnnouncement);
                    ObjectNode objectNode = (ObjectNode) root;
                    objectNode.put("context_code", "course_"+courseObj.getId());
                    announcementsJson.add(mapper.writeValueAsString(objectNode));
                }
                Map<Course, List<DiscussionTopic>> entryApi = api.get(shortName);
                entryApi.put(courseObj, announcements);
            }
            json.put(shortName, announcementsJson.toString());
        }
        mockedApi = api;
        jsonAnnouncements = json;
    }

    @BeforeEach
    public void initializeServices() {
        tokenService.deleteAll();
        initializeInstitutionService();
        initializeUserService();
    }

    private void initializeInstitutionService() {
        // Clear any existing institutions
        institutionService.deleteAll();
        
        Institution uib = new Institution();
        uib.setFullName("The University of Bergen");
        uib.setShortName("uib");
        uib.setApiUrl("https://mitt.uib.no/api/v1");

        Institution hvl = new Institution();
        hvl.setFullName("The Western Norway University of Applied Sciences");
        hvl.setShortName("hvl");
        hvl.setApiUrl("https://hvl.instructure.com/api/v1");

        List<Institution> allInstitutions = List.of(uib, hvl);

        institutionService.createAll(allInstitutions);
    }

    private void initializeUserService() {
        userService.deleteAll();

        UserRegistrationDTO newUser = new UserRegistrationDTO();
        newUser.setUsername("test");
        newUser.setEmail("test@example.com");
        newUser.setPassword("password");

        user = userService.createUser(newUser);
    }

    private void setValidTokenForUser(User user, List<Institution> institutions) {
        User dbUser = userService.getUser(user.getId());
        for (Institution institution : institutions) {
            Institution dbInstitution = institutionService.findByShortName(institution.getShortName()).get();
            CanvasToken token = new CanvasToken();
            token.setUser(dbUser);
            token.setInstitution(dbInstitution);
            token.setToken("validToken");

            tokenService.create(token, dbUser);
        }
    }

    @Test
    public void shouldReturnOKUponGettingAllCourses() throws Exception {
        setValidTokenForUser(user, institutionList);

        for (Institution institution : institutionList) {
            String shortName = institution.getShortName();
            List<Course> courses = new ArrayList<>(mockedApi.get(shortName).keySet());

            ResponseEntity<List<Course>> responseCourseList = new ResponseEntity<>(courses, HttpStatus.OK);
            when(restTemplate.exchange(
                    eq(institution.getApiUrl() + "/courses"),
                    eq(HttpMethod.GET),
                    any(),
                    eq(new ParameterizedTypeReference<List<Course>>(){})
            )).thenReturn(responseCourseList);
        }

        mockMvc.perform(get("/api/courses")
                        .with(csrf())
                        .with(user(user)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void shouldReturnOKUponGettingAllAnnouncements() throws Exception {
        setValidTokenForUser(user, institutionList);

        for (Institution institution : institutionList) {
            String shortName = institution.getShortName();
            List<Course> courses = new ArrayList<>(mockedApi.get(shortName).keySet());

            ResponseEntity<List<Course>> responseCourseList = new ResponseEntity<>(courses, HttpStatus.OK);
            when(restTemplate.exchange(
                    eq(institution.getApiUrl() + "/courses"),
                    eq(HttpMethod.GET),
                    any(),
                    eq(new ParameterizedTypeReference<List<Course>>(){})
            )).thenReturn(responseCourseList);

            ResponseEntity<String> response = new ResponseEntity<>(jsonAnnouncements.get(shortName), HttpStatus.OK);
            when(restTemplate.exchange(
                    eq(institution.getApiUrl() + "/announcements?context_codes[]={validCourseIds}"),
                    eq(HttpMethod.GET),
                    any(),
                    eq(String.class),
                    any(Object[].class)
            )).thenReturn(response);
            }

        mockMvc.perform(get("/api/courses/announcements")
                        .with(csrf())
                        .with(user(user)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void shouldReturnNotFoundWhenUserHasNotSetToken() throws Exception {
        mockMvc.perform(get("/api/courses/announcements")
                        .with(csrf())
                        .with(user(user)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}
