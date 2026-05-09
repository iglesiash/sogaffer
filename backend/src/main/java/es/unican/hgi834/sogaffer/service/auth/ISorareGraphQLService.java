package es.unican.hgi834.sogaffer.service.auth;

import es.unican.hgi834.sogaffer.model.dto.auth.LoginDto;
import es.unican.hgi834.sogaffer.model.dto.sorare.auth.SorareSignInDto;

public interface ISorareGraphQLService {
    SorareSignInDto signIn(LoginDto loginDto);
}
