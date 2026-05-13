package es.unican.hgi834.sogaffer.service.auth;

import es.unican.hgi834.sogaffer.model.dto.auth.AccessTokenDto;

import javax.crypto.SecretKey;

public interface IJwtTokenService {
    AccessTokenDto generateToken(String username);

    SecretKey getSigningKey();
}
