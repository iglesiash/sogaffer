package es.unican.hgi834.sogaffer.service.auth;

import es.unican.hgi834.sogaffer.model.dto.auth.UserDto;

public interface IRefreshTokenService {
    /**
     * Generates a new refresh token for the specified user and saves it in the database.
     *
     * @param userDto the {@link UserDto} representing the user for whom the refresh token is to be generated.
     * @return the raw refresh token string before storing the hashed version in the database.
     */
    String generateRefreshToken(UserDto userDto);
}
