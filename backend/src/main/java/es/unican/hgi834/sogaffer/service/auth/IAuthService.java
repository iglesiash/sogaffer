package es.unican.hgi834.sogaffer.service.auth;

import es.unican.hgi834.sogaffer.model.dto.auth.AccessTokenDto;
import es.unican.hgi834.sogaffer.model.dto.auth.LoginDto;

public interface IAuthService {
    AccessTokenDto login(LoginDto loginDto);
}
