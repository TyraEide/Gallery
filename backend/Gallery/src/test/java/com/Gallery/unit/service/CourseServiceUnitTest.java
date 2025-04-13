package com.Gallery.unit.service;

import com.Gallery.model.*;
import com.Gallery.service.CourseService;
import com.Gallery.service.InstitutionService;
import com.Gallery.service.TokenService;
import com.Gallery.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceUnitTest {
    @Mock RestTemplate restTemplate;
    @Mock InstitutionService institutionService;
    @Mock TokenService tokenService;
    @InjectMocks CourseService courseService;
    private final User emptyUser = new User();

    private static List<Institution> institutionList;
    private static Map<String, Map<Course, List<DiscussionTopic>>> mockedApi;
    private static Map<String, String> jsonAnnouncements;

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
        List<String> courses = List.of("Course1", "Course2");

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

    private void mockInstitutionService(List<Institution> validInstitutions, boolean invalidInstitutions) {
        for (Institution institution : validInstitutions) {
            lenient().when(institutionService.findApiUrlByShortName(institution.getShortName()))
                    .thenReturn(Optional.of(institution.getApiUrl()));
            lenient().when(institutionService.findByShortName(institution.getShortName()))
                    .thenReturn(Optional.of(institution));
        }

        if (invalidInstitutions) {
            lenient().when(institutionService.findApiUrlByShortName(any(String.class)))
                    .thenReturn(Optional.empty());
            lenient().when(institutionService.findByShortName(any(String.class)))
                    .thenReturn(Optional.empty());
        }
    }

    private void mockTokenService(List<Institution> validInstitutions, boolean invalidInstitutions) {
        for (Institution institution : validInstitutions) {
            CanvasToken canvasToken = new CanvasToken();
            canvasToken.setUser(emptyUser);
            canvasToken.setInstitution(institution);
            canvasToken.setToken("validToken");
            lenient().when(tokenService.getTokenForUserAndInstitutionShortName(emptyUser, institution.getShortName()))
                    .thenReturn(canvasToken);
        }

        if (invalidInstitutions) {
            lenient().when(tokenService.getTokenForUserAndInstitutionShortName(any(User.class), any(String.class)))
                    .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, "This institution has no stub."));
        }

        lenient().when(institutionService.findAllInstitutions()).thenReturn(institutionList);
    }

    @Test
    public void shouldReturnAPIAnnouncementsUponGetAnnouncements() throws JsonProcessingException {
        // Mock services
        Institution institution = institutionList.getFirst();
        mockTokenService(List.of(institution), false);
        mockInstitutionService(List.of(institution), false);

        String institutionShortName = institution.getShortName();
        Map<Course, List<DiscussionTopic>> expected = mockedApi.get(institutionShortName);
        List<Course> courses = new ArrayList<>(mockedApi.get(institutionShortName).keySet());
        String baseApiUrl = institutionService.findApiUrlByShortName(institutionShortName).get();

        // Mock restTemplate response for each course
        List<String> courseIds = new ArrayList<>();
        List<String> contextCodes = new ArrayList<>();
        for (int i = 0; i < courses.size(); i++) {
            courseIds.add(courses.get(i).getId());
            contextCodes.add("course_"+courses.get(i).getId());
            ResponseEntity<Course> response = new ResponseEntity<>(courses.get(i), HttpStatus.OK);
            when(restTemplate.exchange(
                    eq(baseApiUrl + "/courses/{courseId}"),
                    eq(HttpMethod.GET),
                    any(),
                    eq(Course.class),
                    eq(courseIds.get(i))
            )).thenReturn(response);
        }

        // Mock RestTemplate response
        CanvasToken authorizationToken = tokenService.getTokenForUserAndInstitutionShortName(emptyUser, institutionShortName);
        HttpEntity<String> request = createRequestWithAuthorizationHeaders(authorizationToken);
        ResponseEntity<String> response = new ResponseEntity<>(jsonAnnouncements.get(institutionShortName), HttpStatus.OK);
        when(restTemplate.exchange(
                any(),
                eq(HttpMethod.GET),
                any(),
                eq(String.class),
                any(Object[].class)
        )).thenReturn(response);

        // Perform getAnnouncements
        Map<String, Map<Course, List<DiscussionTopic>>> announcements = courseService.getAnnouncements(institutionShortName, courseIds, emptyUser);

        // Assertions
        verify(restTemplate).exchange(
                baseApiUrl + "/announcements?context_codes[]={validCourseIds}",
                HttpMethod.GET,
                request,
                String.class,
                contextCodes.toString());

        Map<Course, List<DiscussionTopic>> actual = announcements.get(institutionShortName);
        for (Course course : expected.keySet()) {
            for (DiscussionTopic announcement : expected.get(course)) {
                assertTrue(actual.get(course).contains(announcement));
            }
        }
    }

    private HttpEntity<String> createRequestWithAuthorizationHeaders(CanvasToken authorizationToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + authorizationToken.getToken());
        return new HttpEntity<>(headers);
    }

    @Test
    public void shouldThrowErrorUponInvalidInstitution() {
        mockTokenService(new ArrayList<>(), true);
        assertThrows(HttpClientErrorException.class, () -> courseService.getAnnouncements("invalidInstitution", null, emptyUser));
    }

    @Test
    public void shouldThrowErrorWhenUserHasNotSetToken() {
        mockTokenService(new ArrayList<>(), true);
        assertThrows(HttpClientErrorException.class, () -> courseService.getAnnouncements(institutionList.getFirst().getShortName(), null, emptyUser));
    }

    private void verifyCorrectBaseApiUrl(Institution institution, List<String> courseIds, User authorizedUser) throws JsonProcessingException {
        String institutionBaseUrl = institution.getApiUrl();
        ResponseEntity<String> response = new ResponseEntity<>("", HttpStatus.OK);
        lenient().when(restTemplate.exchange(
                any(),
                eq(HttpMethod.GET),
                any(),
                eq(String.class),
                any(Object[].class)
        )).thenReturn(response);

        String apiUrl = institutionBaseUrl + "/announcements?context_codes[]={validCourseIds}";
        CanvasToken token = tokenService.getTokenForUserAndInstitutionShortName(authorizedUser, institution.getShortName());
        HttpEntity<String> request = createRequestWithAuthorizationHeaders(token);

        courseService.getAnnouncements(institution.getShortName(), courseIds, authorizedUser);

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
        mockInstitutionService(institutionList, false);
        mockTokenService(institutionList, false);
        for (Institution institution : institutionList) {
            verifyCorrectBaseApiUrl(institution, new ArrayList<>(), emptyUser);
        }
    }

    @Test
    public void shouldReturnAllAnnouncementsForAllInstitutionsUponGetAllAnnouncements() throws JsonProcessingException {
        // Mock services
        mockTokenService(institutionList, false);
        mockInstitutionService(institutionList, false);

        // Mock response for all institutions
        for (Institution institution : institutionList) {
            List<Course> courses = new ArrayList<>(mockedApi.get(institution.getShortName()).keySet());

            ResponseEntity<List<Course>> responseCourseList = new ResponseEntity<>(courses, HttpStatus.OK);
            when(restTemplate.exchange(
                    eq(institution.getApiUrl() + "/courses"),
                    eq(HttpMethod.GET),
                    any(),
                    eq(new ParameterizedTypeReference<List<Course>>(){})
            )).thenReturn(responseCourseList);

            ResponseEntity<String> response = new ResponseEntity<>(jsonAnnouncements.get(institution.getShortName()), HttpStatus.OK);
            when(restTemplate.exchange(
                    eq( institution.getApiUrl() + "/announcements?context_codes[]={validCourseIds}"),
                    eq(HttpMethod.GET),
                    any(),
                    eq(String.class),
                    any(Object[].class)
            )).thenReturn(response);
        }

        // Perform getAllAnnouncements
        Map<String, Map<Course, List<DiscussionTopic>>> result = courseService.getAllAnnouncements(emptyUser);

        // Validate result
        for (Institution institution : institutionList) {
            String shortName = institution.getShortName();
            Map<Course, List<DiscussionTopic>> expected = mockedApi.get(shortName);
            Map<Course, List<DiscussionTopic>> actual = result.get(shortName);
            for (Course course : mockedApi.get(shortName).keySet()) {
                for (DiscussionTopic announcement : expected.get(course)) {
                    assertTrue(actual.get(course).contains(announcement));
                }
            }
        }
    }
}
