package com.Gallery.model;

import com.Gallery.converter.StringCryptoConverter;
import jakarta.persistence.*;

import java.util.Objects;

@Table(name = "canvas_tokens")
@IdClass(CanvasTokenId.class)
@Entity()
public class CanvasToken {

    @Id
    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    private User user;

    @Id
    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    private Institution institution;

    @Convert(converter = StringCryptoConverter.class)
    @Column(nullable = false)
    private String token;

    public CanvasToken() {}

    public Institution getInstitution() {
        return institution;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CanvasToken that = (CanvasToken) o;
        return Objects.equals(user, that.user) && Objects.equals(institution, that.institution) && Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, institution, token);
    }
}
