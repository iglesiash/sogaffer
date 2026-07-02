package es.unican.hgi834.sogaffer.service.auth.impl;

import es.unican.hgi834.sogaffer.model.dto.auth.AccessTokenDto;
import es.unican.hgi834.sogaffer.model.dto.auth.AccessRefreshTokenDto;
import es.unican.hgi834.sogaffer.model.dto.auth.UserDto;
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
        UserDto user = sorareLoginService.login(loginDto);
        return generateTokens(user);
    }

    /**
     * Generates an access token and a refresh token for the specified user.
     *
     * @param user the user for whom the tokens will be generated.
     * @return an {@link AccessRefreshTokenDto} containing the generated access token
     * and refresh token.
     */
    private AccessRefreshTokenDto generateTokens(UserDto user) {
        String refreshToken = refreshTokenService.generateRefreshToken(user);
        AccessTokenDto accessToken = jwtTokenService.generateToken(user.email());

        return new AccessRefreshTokenDto(accessToken, refreshToken);
    }
}
