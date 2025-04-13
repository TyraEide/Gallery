package com.Gallery.service;

import com.Gallery.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.awt.*;
import java.util.*;
import java.util.List;

@Service
public class CourseService {
    private final RestTemplate restTemplate;
    private final InstitutionService institutionService;
    private final TokenService tokenService;


    public CourseService(RestTemplate restTemplate, InstitutionService institutionService, TokenService tokenService) {
        this.institutionService = institutionService;
        this.restTemplate = restTemplate;
        this.tokenService = tokenService;
    }

    private HttpEntity<String> buildRequest(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + token);
        return new HttpEntity<>(headers);
    }

    private Map<Course, List<DiscussionTopic>> getAnnouncements(String baseApiUrl, List<String> courseIds, List<Course> courses, String token) throws JsonProcessingException {
        // Get announcements from the canvas api
        String apiUrl = baseApiUrl + "/announcements?context_codes[]={validCourseIds}";
        List<String> contextCodes = convertCourseIdToContextCode(courseIds);

        // We avoid getting the response as DiscussionTopic to get the context_code information
        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                buildRequest(token),
                String.class,
                contextCodes.toString());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());

        return getAnnouncementsPerCourse(courses, root, mapper);
    }

    private Map<Course, List<DiscussionTopic>> getAnnouncementsPerCourse(List<Course> courses, JsonNode root, ObjectMapper mapper) throws JsonProcessingException {
        Map<Course, List<DiscussionTopic>> announcementsPerCourse = new HashMap<>();
        for (JsonNode node : root) {
            String courseContext = node.get("context_code").asText();
            // context_code is given like this: course_123. We want the part after _
            String courseCode = courseContext.split("_")[1];
            // Find the course the announcement belongs to
            for (Course course : courses) {
                if (course.getCourse_code().equals(courseCode)) {
                    // Add announcement to course in map
                    DiscussionTopic announcement = mapper.treeToValue(node, DiscussionTopic.class);
                    List<DiscussionTopic> registeredAnnouncements = announcementsPerCourse.getOrDefault(course, new ArrayList<>());
                    registeredAnnouncements.add(announcement);
                    announcementsPerCourse.put(course, registeredAnnouncements);
                    break;
                }
            }
        }
        return announcementsPerCourse;
    }

    private List<String> convertCourseIdToContextCode(List<String> courseIds) {
        List<String> contextCodes = new ArrayList<>();
        for (String courseId : courseIds) {
            contextCodes.add("course_"+courseId);
        }
        return contextCodes;
    }

    /**
     * Gets the user's announcements for the specified courses at the specified institution.
     * The user must have set a valid authorization token for the institutions.
     * @param institution a valid institution name that all the wanted courses belong to
     * @param courseIds a list of valid course ids for which to collect announcements
     * @param user the user for which to collect the announcements
     * @return A map linking each institution to its courses, and each course to its announcements
     * @throws JsonProcessingException if the json response from the Canvas api cannot be processed
     * @throws HttpClientErrorException if the user has not set an authorization token or if the institution is invalid
     */
    public Map<String, Map<Course, List<DiscussionTopic>>> getAnnouncements(String institution, List<String> courseIds, User user) throws JsonProcessingException {
        String token = getToken(institution, user);
        String baseApiUrl = getBaseApiUrl(institution);

        // Get courses from the canvas api
        List<Course> courses = new ArrayList<>();
        for (String courseId : courseIds) {
            Course course = getCourse(courseId, institution, user);
            courses.add(course);
        }

        Map<String, Map<Course, List<DiscussionTopic>>> announcements = new HashMap<>();
        announcements.put(institution, getAnnouncements(baseApiUrl, courseIds, courses, token));
        return announcements;
    }

    public Course getCourse(String courseId, String institution, User user) {
        String token = getToken(institution, user);
        String baseApiUrl = getBaseApiUrl(institution);

        ResponseEntity<Course> course = restTemplate.exchange(
                baseApiUrl + "/courses/{courseId}",
                HttpMethod.GET,
                buildRequest(token),
                Course.class,
                courseId
        );

        return course.getBody();
    }

    /**
     * Gets the user's courses for the specified institution.
     * The user must have set a valid authorization token for the institutions.
     * @param institution a valid institution name that all the wanted courses belong to
     * @param user the user for which to collect the announcements
     * @return A list of courses the user is enrolled in at the given institutions Canvas
     * @throws HttpClientErrorException if the user has not set an authorization token or if the institution is invalid
     */
    public List<Course> getCourses(String institution, User user) {
        String token = getToken(institution, user);
        String baseApiUrl = getBaseApiUrl(institution);
        String apiUrl = baseApiUrl + "/courses";

        ResponseEntity<List<Course>> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                buildRequest(token),
                new ParameterizedTypeReference<>(){}
        );

        return response.getBody();
    }

    private String getBaseApiUrl(String institution) {
        Optional<String> baseApiUrl = institutionService.findApiUrlByShortName(institution);
        if (baseApiUrl.isPresent()) {
            return baseApiUrl.get();
        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No such institution found: " + institution);
        }
    }

    private String getToken(String institution, User user) {
        return tokenService.getTokenForUserAndInstitutionShortName(user, institution).getToken();
    }

    /**
     * Gets all announcements for all courses the user is enrolled in on all institutions.
     * The user must have set a valid token for all institutions.
     * @param user the user for which to collect the announcements
     * @return A map linking each institution to its courses, and each course to its announcements
     * @throws JsonProcessingException if the json response from the Canvas api cannot be processed
     * @throws HttpClientErrorException if the user has not set both tokens
     */
    public Map<String, Map<Course, List<DiscussionTopic>>> getAllAnnouncements(User user) throws JsonProcessingException {
        Map<String, Map<Course, List<DiscussionTopic>>> announcements = new HashMap<>();

        // Get all courses for each institution
        List<Institution> institutions = institutionService.findAllInstitutions();
        for (Institution institution : institutions) {
            String token = getToken(institution.getShortName(), user);
            String baseApiUrl = institution.getApiUrl();

            List<Course> courses = getCourses(institution.getShortName(), user);
            List<String> courseIds = new ArrayList<>();
            for (Course course : courses) {
                courseIds.add(course.getId());
            }

            announcements.put(institution.getShortName(), getAnnouncements(baseApiUrl, courseIds, courses, token));
        }
        return announcements;
    }

    /**
     * Gets all courses the user is enrolled in on all institutions.
     * The user must have set a valid token for all institutions.
     * @param user the user for which to collect the courses
     * @return A map linking each institution to its courses
     * @throws HttpClientErrorException if the user has not set both tokens
     */
    public Map<String, List<Course>> getAllCourses(User user) {
        Map<String, List<Course>> courses = new HashMap<>();

        // Get all courses for each institution
        List<Institution> institutions = institutionService.findAllInstitutions();
        for (Institution institution : institutions) {
            String institutionShortName = institution.getShortName();
            courses.put(institutionShortName, getCourses(institutionShortName, user));
        }
        return courses;
    }


}
