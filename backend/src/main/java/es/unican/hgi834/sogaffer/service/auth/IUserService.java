package es.unican.hgi834.sogaffer.service.auth;

import es.unican.hgi834.sogaffer.model.dto.auth.UserDto;
import es.unican.hgi834.sogaffer.model.dto.sorare.auth.SorareCurrentUserDto;

public interface IUserService {
    /**
     * Retrieves or creates a user based on the provided Sorare current user data.
     *
     * @param currentUser the Sorare current user data containing the Sorare ID and email.
     * @return a {@link UserDto} representation of the retrieved or newly created user.
     */
    UserDto getUserBySorareSignInDto(SorareCurrentUserDto currentUser);
}
