package es.unican.hgi834.sogaffer.service.auth.impl;

import es.unican.hgi834.sogaffer.model.dto.auth.AccessTokenDto;
import es.unican.hgi834.sogaffer.model.dto.auth.AccessRefreshTokenDto;
import es.unican.hgi834.sogaffer.model.entity.User;
import es.unican.hgi834.sogaffer.service.auth.*;
import es.unican.hgi834.sogaffer.model.dto.auth.LoginDto;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {

    private final ISorareLoginService sorareLoginService;
    private final IJwtTokenService jwtTokenService;
    private final IRefreshTokenService refreshTokenService;

    public AuthService(
            ISorareLoginService sorareLoginService,
            IJwtTokenService jwtTokenService,
            IRefreshTokenService refreshTokenService) {
        this.sorareLoginService = sorareLoginService;
        this.jwtTokenService = jwtTokenService;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public AccessRefreshTokenDto login(LoginDto loginDto) {
        User user = sorareLoginService.login(loginDto);
        return generateTokens(user);
    }

    private AccessRefreshTokenDto generateTokens(User user) {
        String refreshToken = refreshTokenService.generateRefreshToken(user);
        AccessTokenDto accessToken = jwtTokenService.generateToken(user.getEmail());

        return new AccessRefreshTokenDto(accessToken, refreshToken);
    }
}
