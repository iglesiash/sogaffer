package es.unican.hgi834.sogaffer.service.auth;

import es.unican.hgi834.sogaffer.exception.InvalidCredentialsException;
import es.unican.hgi834.sogaffer.exception.SorareAuthException;
import es.unican.hgi834.sogaffer.model.dto.auth.LoginDto;
import es.unican.hgi834.sogaffer.model.dto.sorare.auth.SorareSignInDto;

public interface ISorareGraphQLService {
    /**
     * Authenticates a user using the provided login credentials against the Sorare GraphQL API.
     *
     * @param loginDto an object containing the user's email and password
     * @return an instance of {@link SorareSignInDto} containing the current user information,
     * JWT token, and any API errors encountered during the sign-in process
     * @throws SorareAuthException         if the sign-in process fails due to an error in Sorare
     * @throws InvalidCredentialsException if the provided login credentials are invalid
     */
    SorareSignInDto signIn(LoginDto loginDto);
}
