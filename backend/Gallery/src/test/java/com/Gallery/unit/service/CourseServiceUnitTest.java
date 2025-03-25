package com.Gallery.unit.service;

import com.Gallery.model.DiscussionTopic;
import com.Gallery.model.User;
import com.Gallery.service.CourseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourseServiceUnitTest {
    @Mock RestTemplate restTemplate;
    @InjectMocks CourseService courseService;

    @Test
    public void shouldReturnAPIAnnouncementsUponGetAnnouncements() {
        // Expected response
        DiscussionTopic announcement = new DiscussionTopic();
        announcement.setUrl("url");
        DiscussionTopic[] expected = new DiscussionTopic[]{announcement};

        // Prep arguments
        String validInstitution = "uib";
        String[] validCourseIds = new String[]{"course_validCourseId"};
        User authorizedUser = new User();
        authorizedUser.setUibToken("validToken");

        // Mock response
        String apiUrl = "https://mitt.uib.no/api/v1/announcements?context_codes[]={validCourseIds}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + authorizedUser.getUibToken());
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<DiscussionTopic[]> response = new ResponseEntity<>(expected, HttpStatus.OK);
        when(restTemplate.exchange(
                Mockito.anyString(),
                Mockito.eq(HttpMethod.GET),
                Mockito.any(),
                Mockito.eq(DiscussionTopic[].class),
                Mockito.any(Object[].class)))
                .thenReturn(response);

        DiscussionTopic[] announcements = courseService.getAnnouncements(validInstitution, validCourseIds, authorizedUser);

        // Assertions
        verify(restTemplate).exchange(
                apiUrl,
                HttpMethod.GET,
                request,
                DiscussionTopic[].class,
                Arrays.toString(validCourseIds));
        assertEquals(1, announcements.length);
        assertEquals(expected[0], announcements[0]);
    }

    @Test
    public void shouldThrowErrorUponInvalidInstitution() {
        assertThrows(HttpClientErrorException.class, () -> courseService.getAnnouncements("invalidInstitution", null, new User()));
    }

    @Test
    public void shouldThrowErrorWhenUserHasNotSetToken() {
        assertThrows(HttpClientErrorException.class, () -> courseService.getAnnouncements("uib", null, new User()));
    }

    private void verifyCorrectBaseApiUrl(String institution, String institutionBaseUrl, String[] courseIds, User authorizedUser) {
        ResponseEntity<DiscussionTopic[]> response = new ResponseEntity<>(new DiscussionTopic[0], HttpStatus.OK);
        when(restTemplate.exchange(
                Mockito.anyString(),
                Mockito.eq(HttpMethod.GET),
                Mockito.any(),
                Mockito.eq(DiscussionTopic[].class),
                Mockito.any(Object[].class)))
                .thenReturn(response);

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
                DiscussionTopic[].class,
                Arrays.toString(courseIds)
        );
    }

    @Test
    public void shouldUseCorrectBaseApiUrlForValidInstitution() {
        User authorizedUser = new User();
        authorizedUser.setUibToken("uibToken");
        authorizedUser.setHvlToken("hvlToken");

        String uibBaseApiUrl = "https://mitt.uib.no";
        String hvlBaseApiUrl = "https://hvl.instructure.com";

        verifyCorrectBaseApiUrl("uib", uibBaseApiUrl, null, authorizedUser);
        verifyCorrectBaseApiUrl("hvl", hvlBaseApiUrl, null,  authorizedUser);
    }
}
