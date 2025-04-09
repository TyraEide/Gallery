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

    public Optional<String> findApiUrlByShortName(String shortName) {
        return institutionRepository.findById(shortName)
                .map(Institution::getApiUrl);
    }

    public List<Institution> findAllInstitutions() {
        return institutionRepository.findAll();
    }

    public Optional<Institution> findByShortName(String shortName) {
        return institutionRepository.findById(shortName);
    }

    public void deleteAll() {
        institutionRepository.deleteAll();
    }

    public Institution createInstitution(Institution institution) {
        return institutionRepository.save(institution);
    }

    public List<Institution> createAllInstitutions(List<Institution> institutions) {
        return institutionRepository.saveAll(institutions);
    }
}
