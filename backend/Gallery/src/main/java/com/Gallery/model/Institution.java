package com.Gallery.model;

import java.util.ArrayList;
import java.util.List;

public enum Institution {
    UIB("The University of Bergen", "UiB", "https://mitt.uib.no/api/v1/"),
    HVL("The Western Norway University of Applied Sciences", "HvL", "https://hvl.instructure.com/api/v1/");

    private final String fullName;
    private final String shortName;
    private final String apiUrl;

    Institution(String fullName, String shortName, String apiUrl) {
        this.fullName = fullName;
        this.shortName = shortName;
        this.apiUrl = apiUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public List<Institution> getAllInstitutions() {
        return List.of(Institution.values());
    }
}
