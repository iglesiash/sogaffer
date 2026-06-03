package es.unican.hgi834.sogaffer.service.auth;

import es.unican.hgi834.sogaffer.model.dto.auth.LoginDto;
import es.unican.hgi834.sogaffer.model.dto.auth.AccessRefreshTokenDto;

public interface IAuthService {
    AccessRefreshTokenDto login(LoginDto loginDto);
}
