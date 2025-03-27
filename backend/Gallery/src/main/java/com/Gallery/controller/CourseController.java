package com.Gallery.controller;

import com.Gallery.model.DiscussionTopic;
import com.Gallery.model.User;
import com.Gallery.service.CourseService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/courses")
public class CourseController {
    private CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/{institution}/{course_id}/announcements")
    public DiscussionTopic[] getAnnouncements(@PathVariable String institution, @PathVariable String[] courseIds,
                                                  @AuthenticationPrincipal User user) {
        return courseService.getAnnouncements(institution, courseIds, user);
    }

}
