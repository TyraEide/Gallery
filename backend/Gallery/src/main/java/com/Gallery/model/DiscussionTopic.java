package com.Gallery.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;
import java.util.Map;

/**
 * Representing DiscussionTopic objects from Canvas.
 * This includes announcements and discussions.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscussionTopic {

    private String title;
    private Instant posted_at;
    private Instant delayed_post_at;
    private String message;
    private Map<String, Boolean> permissions;
    private CanvasUser author;
    private String url;
    private boolean is_announcement;

    public DiscussionTopic(String title, Instant posted_at, Instant delayed_post_at, String message,
                           Map<String, Boolean> permissions, CanvasUser author, String url, boolean is_announcement) {
        this.title = title;
        this.posted_at = posted_at;
        this.delayed_post_at = delayed_post_at;
        this.message = message;
        this.permissions = permissions;
        this.author = author;
        this.url = url;
        this.is_announcement = is_announcement;
    }

    public DiscussionTopic() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * The posting date of the {@link DiscussionTopic}. If posting
     * should be delayed, this date indicates when the {@link DiscussionTopic}
     * was made available for write-authorized users. See {@link DiscussionTopic#getDelayed_post_at()}
     * for the delayed posting date.
     * If {@link DiscussionTopic#getDelayed_post_at()} is null, this
     * is the date the post was made available for read-authorized users.
     * @return the datetime the {@link DiscussionTopic} was posted
     */
    public Instant getPosted_at() {
        return posted_at;
    }

    public void setPosted_at(Instant posted_at) {
        this.posted_at = posted_at;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Boolean> getPermissions() {
        return permissions;
    }

    public void setPermissions(Map<String, Boolean> permissions) {
        this.permissions = permissions;
    }

    public CanvasUser getAuthor() {
        return author;
    }

    public void setAuthor(CanvasUser author) {
        this.author = author;
    }


    /**
     * @return the url pointing to the {@link DiscussionTopic} at Canvas.
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url pointing to the {@link DiscussionTopic} at Canvas.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isAnnouncement() {
        return is_announcement;
    }

    public void setIs_announcement(boolean is_announcement) {
        this.is_announcement = is_announcement;
    }

    /**
     * Indicates that the post will be available
     * for regular readers at a later time.
     * If return is null, the post has been made available
     * for readers at the time specified by
     * {@link DiscussionTopic#getPosted_at()}. If not null,
     * {@link DiscussionTopic#getPosted_at()} indicates when the post
     * was made available for users authorized to edit the specific announcement,
     * and the returned date is when regular users will be able to see the post.
     *
     * @return the datetime a post is made available for read-authorized users, or null
     * if the post already is available.
     */
    public Instant getDelayed_post_at() {
        return delayed_post_at;
    }

    /**
     * The date at which the post should be made available for read-authorized
     * users. Used only when the post should be posted at a later time than creation,
     * and should otherwise be set to null.
     * @param delayed_post_at the datetime to delay posting of the DiscussionTopic to
     */
    public void setDelayed_post_at(Instant delayed_post_at) {
        this.delayed_post_at = delayed_post_at;
    }


}
