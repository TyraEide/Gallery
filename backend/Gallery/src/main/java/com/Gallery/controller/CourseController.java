package com.Gallery.controller;

import com.Gallery.model.Course;
import com.Gallery.model.DiscussionTopic;
import com.Gallery.model.User;
import com.Gallery.service.CourseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/courses")
public class CourseController {
    private CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/announcements")
    public Map<String, Map<Course, List<DiscussionTopic>>> getAllAnnouncements(@AuthenticationPrincipal User user) throws JsonProcessingException {
        return courseService.getAllAnnouncements(user);
    }

}
