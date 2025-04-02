package com.Gallery.service;

import com.Gallery.model.Institution;
import com.Gallery.repository.InstitutionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstitutionService {
    private InstitutionRepository institutionRepository;

    public InstitutionService(InstitutionRepository institutionRepository) {
        this.institutionRepository = institutionRepository;
    }

    public String getApiUrlByShortName(String shortName) {
        return institutionRepository.findByShortName(shortName)
                .map(Institution::getApiUrl)
                .orElse(null);
    }

    public List<Institution> getAllInstitutions() {
        return institutionRepository.findAll();
    }
}
