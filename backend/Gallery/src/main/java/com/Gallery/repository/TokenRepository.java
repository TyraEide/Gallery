package com.Gallery.repository;

import com.Gallery.model.CanvasToken;
import com.Gallery.model.CanvasTokenId;
import com.Gallery.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<CanvasToken, CanvasTokenId> {
    Optional<List<CanvasToken>> getAllTokensForUser(User user);

}
