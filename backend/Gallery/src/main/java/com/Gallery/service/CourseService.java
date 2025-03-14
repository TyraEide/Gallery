package com.Gallery.service;

import com.Gallery.model.DiscussionTopic;
import com.Gallery.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class CourseService {

    private final WebClient webClient;

    public CourseService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("").build();
    }

    public List<DiscussionTopic> getAnnouncements(String institution,String course_id, User user) {
        String token;
        if(institution.equals("uib")) {
            token = user.getUibToken();
        }
        else if(institution.equals("hvl")) {
            token = user.getHvlToken();
        }
        else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }

        if(token == null) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        }


    }
}
