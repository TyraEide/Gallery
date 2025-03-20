package com.Gallery.unit.model;

import com.Gallery.model.CanvasUser;
import com.Gallery.model.DiscussionTopic;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiscussionTopicUnitTest {

    @Test
    public void shouldReturnSetArgumentsUponGet() {
        String title = "Title";
        LocalDateTime posted_at = LocalDateTime.now();
        LocalDateTime delayed_posted_at = LocalDateTime.now().plusHours(3);
        String message = "Message";
        // Add permissions
        CanvasUser author = new CanvasUser("id");
        String url = "https://example.com/course/123/discussiontopic/123";
        boolean isAnnouncement = true;

        DiscussionTopic e = new DiscussionTopic();

        e.setTitle(title);
        e.setPosted_at(posted_at);
        e.setDelayed_post_at(delayed_posted_at);
        e.setMessage(message);
        e.setAuthor(author);
        e.setUrl(url);
        e.setAnnouncement(isAnnouncement);

        assertEquals(title, e.getTitle());
        assertEquals(posted_at, e.getPosted_at());
        assertEquals(delayed_posted_at, e.getDelayed_post_at());
        assertEquals(message, e.getMessage());
        assertEquals(author, e.getAuthor());
        assertEquals(url, e.getUrl());
        assertEquals(isAnnouncement, e.isAnnouncement());



    }
}
