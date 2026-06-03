package es.unican.hgi834.sogaffer.service.auth;

import es.unican.hgi834.sogaffer.model.dto.auth.UserDto;
import es.unican.hgi834.sogaffer.model.dto.sorare.auth.SorareJwtTokenDto;

public interface ISorareTokenService {
    void invalidateActiveTokens(int userId);

    void persistSorareToken(UserDto user, SorareJwtTokenDto jwtTokenDto);
}
