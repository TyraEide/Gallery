package com.Gallery.controller;

import com.Gallery.model.Institution;
import com.Gallery.service.InstitutionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@RequestMapping("api/institutions")
public class InstitutionController {
    private final InstitutionService institutionService;
    public InstitutionController(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createInstitution(@RequestBody Institution institution) {
        institutionService.create(institution);
    }

    @GetMapping("/{shortName}")
    public Institution getInstitutionByShortName(@PathVariable String shortName) {
        return institutionService.findByShortName(shortName).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND,
                "No institution with short name " + shortName + " found."));
    }

    @GetMapping()
    public List<Institution> getAllInstitutions() {
        return institutionService.findAllInstitutions();
    }
}
