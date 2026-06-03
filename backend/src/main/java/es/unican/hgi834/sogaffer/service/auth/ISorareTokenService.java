package es.unican.hgi834.sogaffer.service.auth;

import es.unican.hgi834.sogaffer.model.dto.sorare.auth.SorareJwtTokenDto;
import es.unican.hgi834.sogaffer.model.entity.SorareToken;
import es.unican.hgi834.sogaffer.model.entity.User;

public interface ISorareTokenService {
    void invalidateActiveTokens(int userId);

    void persistSorareToken(User user, SorareJwtTokenDto jwtTokenDto);
}
