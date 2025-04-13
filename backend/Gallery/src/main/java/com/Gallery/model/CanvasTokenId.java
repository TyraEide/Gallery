package com.Gallery.model;

import java.io.Serializable;
import java.util.Objects;

public class CanvasTokenId implements Serializable {
    private User user;

    private Institution institution;

    public CanvasTokenId(User user, Institution institution) {
        this.user = user;
        this.institution = institution;
    }

    public CanvasTokenId() {
    }

    public User getUser() {
        return user;
    }

    public Institution getInstitution() {
        return institution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CanvasTokenId that = (CanvasTokenId) o;
        return Objects.equals(user, that.user) && Objects.equals(institution, that.institution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, institution);
    }
}
