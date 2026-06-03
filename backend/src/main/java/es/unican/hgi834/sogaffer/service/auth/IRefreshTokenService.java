package es.unican.hgi834.sogaffer.service.auth;

import es.unican.hgi834.sogaffer.model.dto.auth.UserDto;

public interface IRefreshTokenService {
    String generateRefreshToken(UserDto user);
}
