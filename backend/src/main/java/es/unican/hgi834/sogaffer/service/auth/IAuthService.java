package es.unican.hgi834.sogaffer.service.auth;

import es.unican.hgi834.sogaffer.model.dto.auth.LoginDto;
import es.unican.hgi834.sogaffer.model.dto.auth.AccessRefreshTokenDto;

public interface IAuthService {

    /**
     * Authenticates a user using the provided login credentials and generates an access token
     * and a refresh token for the authenticated user.
     *
     * @param loginDto the login credentials containing the user's email and password.
     * @return an {@link AccessRefreshTokenDto} object containing the generated access token
     * and refresh token for the authenticated user.
     */
    AccessRefreshTokenDto login(LoginDto loginDto);
}
