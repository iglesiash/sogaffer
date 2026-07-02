package es.unican.hgi834.sogaffer.service.auth;

import es.unican.hgi834.sogaffer.model.dto.auth.LoginDto;
import es.unican.hgi834.sogaffer.model.dto.auth.UserDto;

public interface ISorareLoginService {

    /**
     * Authenticates a user with the given login credentials and returns their data.
     *
     * @param loginDto an object containing the user's email and password.
     * @return a {@link UserDto} object representing the authenticated user.
     */
    UserDto login(LoginDto loginDto);
}
