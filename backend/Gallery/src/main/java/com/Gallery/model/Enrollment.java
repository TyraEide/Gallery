package com.Gallery.model;

public final class Enrollment {
    private final String type;
    private final String user_id;

    public Enrollment(String type, String user_id) {
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null.");
        }
        if (user_id == null) {
            throw new IllegalArgumentException("User id cannot be null.");
        }
        this.type = type;
        this.user_id = user_id;
    }

    public String getType() {
        return type;
    }

    public String getUserId() {
        return user_id;
    }
}
