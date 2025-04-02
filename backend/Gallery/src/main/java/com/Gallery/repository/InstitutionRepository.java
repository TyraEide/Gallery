package com.Gallery.repository;

import com.Gallery.model.Institution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstitutionRepository extends JpaRepository<Institution, String> {
    Optional<Institution> findByShortName(String shortName);
}
