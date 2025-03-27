package com.Gallery.unit.controller;

import com.Gallery.controller.CourseController;
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

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourseControllerUnitTest {
    @Mock private CourseService courseService;
    @InjectMocks private CourseController courseController;

    @Test
    public void shouldReturnAnnouncementsWhenValidCourseIdsAndInstitution() {
        // Expected response
        DiscussionTopic announcement = new DiscussionTopic();
        announcement.setUrl("url");
        DiscussionTopic[] expected = new DiscussionTopic[]{announcement};

        // Prep arguments
        String validInstitution = "uib";
        String[] validCourseIds = new String[]{"course_validCourseId"};
        User authorizedUser = new User();
        authorizedUser.setUibToken("validToken");

        when(courseService.getAnnouncements(validInstitution, validCourseIds, authorizedUser)).thenReturn(expected);
        DiscussionTopic[] result = courseController.getAnnouncements(validInstitution, validCourseIds, authorizedUser);
        verify(courseService).getAnnouncements(validInstitution, validCourseIds, authorizedUser);
        assertEquals(expected, result);
    }
}
