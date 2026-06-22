package es.unican.hgi834.sogaffer.service.auth;

import es.unican.hgi834.sogaffer.model.dto.auth.UserDto;
import es.unican.hgi834.sogaffer.model.dto.sorare.auth.SorareJwtTokenDto;
import es.unican.hgi834.sogaffer.model.entity.SorareToken;

import java.util.List;

public interface ISorareTokenService {
    List<SorareToken> invalidateActiveTokens(int userId);

    void persistSorareToken(UserDto user, SorareJwtTokenDto jwtTokenDto);
}
