package com.Gallery.controller;

import com.Gallery.model.Course;
import com.Gallery.model.DiscussionTopic;
import com.Gallery.model.User;
import com.Gallery.service.CourseService;
import com.Gallery.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/courses")
public class CourseController {
    private CourseService courseService;
    private UserService userService;

    public CourseController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

    @GetMapping("/users/{id}")
    public Map<String, List<Course>> getAllCourses(@PathVariable UUID id) {
        User user = userService.getUser(id);
        return courseService.getAllCourses(user);
    }

    @GetMapping("/{id}/users/{userId}")
    public Course getCourse(@PathVariable String id, @PathVariable UUID userId, @RequestBody String institutionShortName) {
        User user = userService.getUser(userId);
        return courseService.getCourse(id, institutionShortName, user);
    }

    @GetMapping("/announcements/users/{id}")
    public Map<String, Map<Course, List<DiscussionTopic>>> getAllAnnouncements(@PathVariable UUID id) throws JsonProcessingException {
        User user = userService.getUser(id);
        return courseService.getAllAnnouncements(user);
    }

}
