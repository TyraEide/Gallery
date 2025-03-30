package com.Gallery.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Course {
    private String id;
    private String name;
    private String course_code;
    private boolean is_public;
    private String timezone;


    public void setId(String id) {
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

    public void setCourse_code(String course_code) {
        this.course_code = course_code;
    }

    public String getCourse_code() {
        return course_code;
    }

    public void setIs_public(boolean is_public) {
        this.is_public = is_public;
    }

    public boolean getIs_public() {
        return is_public;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getTimezone() {
        return timezone;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Course other = (Course) obj;

        if (this.getId() != other.getId())
            return false;
        if (this.getName() != other.getName())
            return false;
        return this.getCourse_code() == other.getCourse_code();
    }
}
