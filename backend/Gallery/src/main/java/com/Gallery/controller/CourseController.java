package com.Gallery.controller;

import com.Gallery.model.DiscussionTopic;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/courses")
public class CourseController {

    @GetMapping("/{institution}/{course_id}/announcements")
    public List<DiscussionTopic> getAnnouncements(@PathVariable String institution, @PathVariable String course_id) {

    }

}
