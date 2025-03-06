package com.Gallery.model;

import java.util.Map;

public class CanvasUser {
    private final String id;
    private String name;
    private String sortable_name;
    private String avatar_url;
    private String first_name;
    private String last_name;
    private String effective_locale;

    public CanvasUser(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public String getFirstName() {
        return first_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public String getLastName() {
        return last_name;
    }

    public void setSortableName(String sortable_name) {
        this.sortable_name = sortable_name;
    }

    public String getSortableName() {
        return sortable_name;
    }

    public void setAvatarUrl(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getAvatarUrl() {
        return avatar_url;
    }

    public void setEffectiveLocale(String effective_locale) {
        this.effective_locale = effective_locale;
    }

    public String getEffectiveLocale() {
        return effective_locale;
    }
}
