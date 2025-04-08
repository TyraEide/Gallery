package com.Gallery.service;

import com.Gallery.model.CanvasToken;
import com.Gallery.model.CanvasTokenId;
import com.Gallery.model.Institution;
import com.Gallery.model.User;
import com.Gallery.repository.InstitutionRepository;
import com.Gallery.repository.TokenRepository;
import com.Gallery.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

public class TokenService {

    private TokenRepository tokenRepository;
    private InstitutionService institutionService;
    private UserService userService;

    public TokenService(TokenRepository tokenRepository, InstitutionService institutionService, UserService userService) {
        this.tokenRepository = tokenRepository;
        this.institutionService = institutionService;
        this.userService = userService;
    }

    private Institution getInstitution(String shortNameInstitution) {
        Optional<Institution> institution = institutionService.findByShortName(shortNameInstitution);
        if (institution.isPresent()) {
            return institution.get();
        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No such institution found: " + shortNameInstitution);
        }
    }

    private CanvasToken getTokenUserAndInstitution(User userId, Institution institution) {
        if (userService.existsById(userId.getId())) {
            CanvasTokenId canvasTokenId = new CanvasTokenId(userId, institution);
            Optional<CanvasToken> canvasToken = tokenRepository.findById(canvasTokenId);
            if (canvasToken.isPresent()) {
                return canvasToken.get();
            } else {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Could not find token to institution" + institution.getShortName() + "for user " + userId.getId());
            }
        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "User with id " + userId.getId() + " not found");
        }
    }

    /**
     * Gets a {@link User}'s {@link CanvasToken} for a given institution.
     * @param userId the {@link User} for which to get the token
     * @param shortNameInstitution the short name of the institution the token should be linked to
     * @return the authorization token for the given User to the given institution's Canvas page.
     */
    public CanvasToken getTokenForUserAndInstitutionShortName(User userId, String shortNameInstitution) {
        Institution institution = getInstitution(shortNameInstitution);
        return getTokenUserAndInstitution(userId, institution);
    }
}
