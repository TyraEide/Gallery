package com.Gallery.model;

import com.Gallery.converter.StringCryptoConverter;
import jakarta.persistence.*;

@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user", "institution"})
})
@IdClass(CanvasTokenId.class)
@Entity
public class CanvasToken {

    @Id
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private User user;

    @Id
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
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
}
