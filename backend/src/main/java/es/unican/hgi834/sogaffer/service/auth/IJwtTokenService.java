package es.unican.hgi834.sogaffer.service.auth;

import es.unican.hgi834.sogaffer.model.dto.auth.AccessTokenDto;

import javax.crypto.SecretKey;

public interface IJwtTokenService {
    /**
     * Generates a new JWT access token for a given username.
     *
     * @param username the username to be used as the subject in the generated token.
     * @return an {@link AccessTokenDto} object containing the generated token
     * and its expiration duration in seconds.
     */
    AccessTokenDto generateToken(String username);

    /**
     * Generates the signing key used for JWT token generation.
     *
     * @return the {@link SecretKey} used to sign the JWT tokens.
     */
    SecretKey getSigningKey();
}
