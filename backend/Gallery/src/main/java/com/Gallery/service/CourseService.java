package com.Gallery.service;

import com.Gallery.model.Course;
import com.Gallery.model.DiscussionTopic;
import com.Gallery.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class CourseService {
    private final RestTemplate restTemplate;
    private final String uibApiUrlBase = "https://mitt.uib.no/api/v1";
    private final String hvlApiUrlBase = "https://hvl.instructure.com/api/v1";

    public CourseService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
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

        // Link announcements to course in the map
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
     * Returns a map linking each course from the courseIds list to the canvas announcements for that course.
     * If announcements from all institutions are wanted, {@link #getAllAnnouncements(User)} should be used instead.
     * @param institution the institution the courses belong to
     * @param courseIds the courses for which to get announcements
     * @param user the user who makes the request
     * @return a map linking each course id from the courseIds list to the canvas announcements for that course.
     */
    public Map<String, Map<Course, List<DiscussionTopic>>> getAnnouncements(String institution, List<String> courseIds, User user) throws JsonProcessingException {
        String token = getToken(institution, user);
        if(token == null) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "User has not set " + institution + " authorization token.");
        }

        String baseApiUrl = getBaseApiUrl(institution);

        // Get courses from the canvas api
        List<Course> courses = new ArrayList<>();
        for (String courseId : courseIds) {
            ResponseEntity<Course> course = restTemplate.exchange(
                    baseApiUrl + "/courses/{courseId}",
                    HttpMethod.GET,
                    buildRequest(token),
                    Course.class,
                    courseId
            );
            courses.add(course.getBody());
        }

        Map<String, Map<Course, List<DiscussionTopic>>> announcements = new HashMap<>();
        announcements.put(institution, getAnnouncements(baseApiUrl, courseIds, courses, token));
        return announcements;
    }

    private String getBaseApiUrl(String institution) {
        if(institution.equals("uib")) {
            return uibApiUrlBase;
        }
        else if(institution.equals("hvl")) {
            return hvlApiUrlBase;
        }
        else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No such institution found: " + institution);
        }
    }

    private String getToken(String institution, User user) {
        String token;
        if(institution.equals("uib")) {
            token = user.getUibToken();
        }
        else if(institution.equals("hvl")) {
            token = user.getHvlToken();
        }
        else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No such institution found: " + institution);
        }
        return token;
    }

    /**
     * Returns a map linking each institution to the user's courses, and these are then mapped
     * to the announcements the user has authorization to see from the course.
     * @param user the user to get announcements for
     * @return a map linking each institution to the user's courses, and these are then mapped
     * to the announcements the user has authorization to see from the course.
     */
    public Map<String, Map<Course, List<DiscussionTopic>>> getAllAnnouncements(User user) throws JsonProcessingException {
        Map<String, Map<Course, List<DiscussionTopic>>> announcements = new HashMap<>();

        // Get all courses for each institution
        List<String> institutions = List.of("uib", "hvl");
        for (String institution : institutions) {
            String token = getToken(institution, user);
            if(token == null) {
                throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "User has not set " + institution + " authorization token.");
            }
            String baseApiUrl = getBaseApiUrl(institution);
            String apiUrl = baseApiUrl + "/courses";

            ResponseEntity<Course[]> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    buildRequest(token),
                    Course[].class);

            List<Course> courses = List.of(response.getBody());
            List<String> courseIds = new ArrayList<>();
            for (Course course : courses) {
                courseIds.add(course.getId());
            }

            announcements.put(institution, getAnnouncements(baseApiUrl, courseIds, courses, token));
        }
        return announcements;
    }


}
