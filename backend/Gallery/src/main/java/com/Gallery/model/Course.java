package com.Gallery.model;


public class Course {
    private final String id;
    private String name;
    private String course_code;
    private boolean is_public;
    private Calendar calendar;
    private String timezone;

    public Course(String id) {
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

    public void setCourseCode(String course_code) {
        this.course_code = course_code;
    }

    public String getCourseCode() {
        return course_code;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setPublic(boolean is_public) {
        this.is_public = is_public;
    }

    public boolean isPublic() {
        return is_public;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getTimezone() {
        return timezone;
    }
}
