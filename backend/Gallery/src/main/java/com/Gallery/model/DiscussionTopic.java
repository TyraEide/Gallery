package com.Gallery.model;

import java.util.Date;
import java.util.Map;

public class DiscussionTopic {

    private String title;
    private Date posted_at;
    private Date delayed_post_at;
    private String message;
    private Map<String, Boolean> permissions;
    private CanvasUser author;
    private String url;
    private boolean isAssignment;

    public DiscussionTopic(String title, Date posted_at, Date delayed_post_at, String message,
                           Map<String, Boolean> permissions, CanvasUser author, String url, boolean isAssignment) {
        this.title = title;
        this.posted_at = posted_at;
        this.delayed_post_at = delayed_post_at;
        this.message = message;
        this.permissions = permissions;
        this.author = author;
        this.url = url;
        this.isAssignment = isAssignment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getPosted_at() {
        return posted_at;
    }

    public void setPosted_at(Date posted_at) {
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


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isAssignment() {
        return isAssignment;
    }

    public void setAssignment(boolean assignment) {
        isAssignment = assignment;
    }

    public Date getDelayed_post_at() {
        return delayed_post_at;
    }

    public void setDelayed_post_at(Date delayed_post_at) {
        this.delayed_post_at = delayed_post_at;
    }
}
