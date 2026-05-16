package es.unican.hgi834.sogaffer.service.auth.impl;

import es.unican.hgi834.sogaffer.model.dto.auth.AccessTokenDto;
import es.unican.hgi834.sogaffer.model.dto.auth.AccessRefreshTokenDto;
import es.unican.hgi834.sogaffer.model.dto.sorare.auth.SorareSignInDto;
import es.unican.hgi834.sogaffer.model.entity.SorareToken;
import es.unican.hgi834.sogaffer.model.entity.User;
import es.unican.hgi834.sogaffer.service.auth.*;
import es.unican.hgi834.sogaffer.model.dto.auth.LoginDto;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {

    private final ISorareAuthService sorareAuthService;
    private final ISorareGraphQLService sorareGraphQLService;
    private final IJwtTokenService jwtTokenService;
    private final IRefreshTokenService refreshTokenService;
    private final IUserService userService;
    private final ISorareTokenService sorareTokenService;

    public AuthService(ISorareAuthService sorareAuthService,
                       ISorareGraphQLService sorareGraphQLService,
                       IJwtTokenService jwtTokenService,
                       IRefreshTokenService refreshTokenService,
                       IUserService userService,
                       ISorareTokenService sorareTokenService) {
        this.sorareAuthService = sorareAuthService;
        this.sorareGraphQLService = sorareGraphQLService;
        this.jwtTokenService = jwtTokenService;
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;
        this.sorareTokenService = sorareTokenService;
    }

    @Override
    public AccessRefreshTokenDto login(LoginDto loginDto) {
        String email = loginDto.email();
        SorareToken existingSorareToken = sorareTokenService.getSorareTokenByEmail(email);
        User user = null;

        // If no valid token exists, call Sorare
        if (existingSorareToken == null) {
            String password = loginDto.password();
            String salt = sorareAuthService.getSalt(email).salt();
            String hashedPassword = BCrypt.hashpw(password, salt);

            SorareSignInDto signInDto = sorareGraphQLService.signIn(new LoginDto(email, hashedPassword));
            user = userService.getUserBySorareSignInDto(signInDto.currentUser());

            sorareTokenService.invalidateActiveTokens(user.getId());
            sorareTokenService.persistSorareToken(user, signInDto.jwtToken());
        }

        // If the user is null, it means that a valid token already exists; fetch the user by email
        if (user == null) {
            user = userService.getByEmail(email);
        }

        // Generate both access and refresh tokens
        String refreshToken = refreshTokenService.generateRefreshToken(user);
        AccessTokenDto accessToken = jwtTokenService.generateToken(email);

        return new AccessRefreshTokenDto(accessToken, refreshToken);
    }
}
