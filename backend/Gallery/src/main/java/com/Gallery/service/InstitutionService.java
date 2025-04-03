package com.Gallery.service;

import com.Gallery.model.Institution;
import com.Gallery.repository.InstitutionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InstitutionService {
    private InstitutionRepository institutionRepository;

    public InstitutionService(InstitutionRepository institutionRepository) {
        this.institutionRepository = institutionRepository;
    }

    public Optional<String> getApiUrlByShortName(String shortName) {
        return institutionRepository.findByShortName(shortName)
                .map(Institution::getApiUrl);
    }

    public List<Institution> getAllInstitutions() {
        return institutionRepository.findAll();
    }
}
