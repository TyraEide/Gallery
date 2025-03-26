package com.Gallery.unit.model;

import com.Gallery.model.CanvasUser;
import com.Gallery.model.DiscussionTopic;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class DiscussionTopicUnitTest {

    @Test
    public void shouldReturnSetArgumentsUponGet() {
        String title = "Title";
        Instant posted_at = Instant.now();
        Instant delayed_posted_at = Instant.now().plus(Duration.ofHours(3));
        String message = "Message";
        // Add permissions
        CanvasUser author = new CanvasUser();
        author.setId("id");
        String url = "https://example.com/course/123/discussiontopic/123";
        boolean isAnnouncement = true;

        DiscussionTopic e = new DiscussionTopic();

        e.setTitle(title);
        e.setPosted_at(posted_at);
        e.setDelayed_post_at(delayed_posted_at);
        e.setMessage(message);
        e.setAuthor(author);
        e.setUrl(url);
        e.setIs_announcement(isAnnouncement);

        assertEquals(title, e.getTitle());
        assertEquals(posted_at, e.getPosted_at());
        assertEquals(delayed_posted_at, e.getDelayed_post_at());
        assertEquals(message, e.getMessage());
        assertEquals(author, e.getAuthor());
        assertEquals(url, e.getUrl());
        assertEquals(isAnnouncement, e.isAnnouncement());
    }

    @Test
    public void shouldSerializeFromJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        File jsonFile = new File("src/test/java/com/Gallery/unit/model/announcementTestJson.json");
        DiscussionTopic discussionTopic = objectMapper.readValue(jsonFile, DiscussionTopic.class);
        assertDoesNotThrow(() -> objectMapper.readValue(jsonFile, DiscussionTopic.class));

        HashMap<String, Object> jsonMap = objectMapper.readValue(jsonFile, HashMap.class);
        assertEquals(jsonMap.get("title"), discussionTopic.getTitle());

        Instant posted_at = Instant.parse((CharSequence) jsonMap.get("posted_at"));
        assertEquals(posted_at, discussionTopic.getPosted_at());

        Instant delayed_post_at = Instant.parse((CharSequence) jsonMap.get("delayed_post_at"));
        assertEquals(delayed_post_at, discussionTopic.getDelayed_post_at());

        // TODO: Should CanvasUser be connected to User objects and stored in database?
        //  This part would just search the author by id then and compare?


        assertEquals(jsonMap.get("message"), discussionTopic.getMessage());
        assertEquals(jsonMap.get("url"), discussionTopic.getUrl());
        assertEquals(jsonMap.get("is_announcement"), discussionTopic.isAnnouncement());
    }
}
