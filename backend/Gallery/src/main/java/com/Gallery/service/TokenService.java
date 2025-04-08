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
    private InstitutionRepository institutionRepository;
    private UserRepository userRepository;

    public TokenService(TokenRepository tokenRepository, InstitutionRepository institutionRepository, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.institutionRepository = institutionRepository;
        this.userRepository = userRepository;
    }

    private Institution getInstitution(String shortNameInstitution) {
        Optional<Institution> institution = institutionRepository.findByShortName(shortNameInstitution);
        if (institution.isPresent()) {
            return institution.get();
        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No such institution found: " + shortNameInstitution);
        }
    }

    private boolean userExists(User user) {
        return userRepository.existsById(user.getId());
    }

    private CanvasToken getTokenUserAndInstitution(User user, Institution institution) {
        if (userExists(user)) {
            CanvasTokenId canvasTokenId = new CanvasTokenId(user, institution);
            Optional<CanvasToken> canvasToken = tokenRepository.findById(canvasTokenId);
            if (canvasToken.isPresent()) {
                return canvasToken.get();
            } else {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Could not find token to institution" + institution.getShortName() + "for user " + user.getId());
            }
        } else {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "User with id " + user.getId() + " not found");
        }
    }

    /**
     * Gets a {@link User}'s {@link CanvasToken} for a given institution.
     * @param user the {@link User} for which to get the token
     * @param shortNameInstitution the short name of the institution the token should be linked to
     * @return the authorization token for the given User to the given institution's Canvas page.
     */
    public CanvasToken getTokenForUserAndInstitutionShortName(User user, String shortNameInstitution) {
        Institution institution = getInstitution(shortNameInstitution);
        return getTokenUserAndInstitution(user, institution);
    }
}
