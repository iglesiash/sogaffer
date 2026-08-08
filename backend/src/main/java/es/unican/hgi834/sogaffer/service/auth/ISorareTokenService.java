package es.unican.hgi834.sogaffer.service.auth;

import es.unican.hgi834.sogaffer.model.dto.auth.UserDto;
import es.unican.hgi834.sogaffer.model.dto.sorare.auth.SorareJwtTokenDto;
import es.unican.hgi834.sogaffer.model.entity.SorareToken;

import java.util.List;

public interface ISorareTokenService {

    /**
     * Invalidates all active Sorare tokens associated with the specified user.
     *
     * @param userId the ID of the user whose active tokens are to be invalidated.
     * @return a list of {@link SorareToken} objects representing the invalidated tokens.
     */
    List<SorareToken> invalidateActiveTokens(int userId);

    /**
     * Persists a Sorare token associated with a user into the database.
     *
     * @param user the {@link UserDto} object containing the user's information.
     * @param jwtTokenDto the {@link SorareJwtTokenDto} object containing the token and its expiration date.
     */
    void persistSorareToken(UserDto user, SorareJwtTokenDto jwtTokenDto);
}
