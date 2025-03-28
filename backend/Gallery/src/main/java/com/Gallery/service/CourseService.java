package com.Gallery.service;

import com.Gallery.model.DiscussionTopic;
import com.Gallery.model.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;

@Service
public class CourseService {
    private final RestTemplate restTemplate;
    private final String uibApiUrlBase = "https://mitt.uib.no/api/v1";
    private final String hvlApiUrlBase = "https://hvl.instructure.com/api/v1";

    public CourseService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public DiscussionTopic[] getAnnouncements(String institution, String[] courseIds, User user) {
        String token = getToken(institution, user);
        if(token == null) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "User has not set " + institution + " authorization token.");
        }

        String baseApiUrl = getBaseApiUrl(institution);
        String apiUrl = baseApiUrl + "/announcements?context_codes[]={validCourseIds}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<DiscussionTopic[]> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                request,
                DiscussionTopic[].class,
                Arrays.toString(courseIds));

        return response.getBody();
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
}
