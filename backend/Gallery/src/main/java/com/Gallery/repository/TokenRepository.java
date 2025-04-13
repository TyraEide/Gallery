package com.Gallery.repository;

import com.Gallery.model.CanvasToken;
import com.Gallery.model.CanvasTokenId;
import com.Gallery.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<CanvasToken, CanvasTokenId> {
    Optional<List<CanvasToken>> findCanvasTokenByUser(User user);

}
