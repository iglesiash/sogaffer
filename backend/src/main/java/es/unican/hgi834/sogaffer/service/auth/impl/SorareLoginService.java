package es.unican.hgi834.sogaffer.service.auth.impl;

import es.unican.hgi834.sogaffer.model.dto.auth.LoginDto;
import es.unican.hgi834.sogaffer.model.dto.auth.UserDto;
import es.unican.hgi834.sogaffer.model.dto.sorare.auth.SorareSignInDto;
import es.unican.hgi834.sogaffer.service.auth.*;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class SorareLoginService implements ISorareLoginService {

    private final ISorareAuthService sorareAuthService;
    private final ISorareGraphQLService sorareGraphQLService;
    private final ISorareTokenService sorareTokenService;
    private final IUserService userService;

    public SorareLoginService(ISorareAuthService sorareAuthService,
                              ISorareGraphQLService sorareGraphQLService,
                              ISorareTokenService sorareTokenService,
                              IUserService userService) {
        this.sorareAuthService = sorareAuthService;
        this.sorareGraphQLService = sorareGraphQLService;
        this.sorareTokenService = sorareTokenService;
        this.userService = userService;
    }

    @Override
    public UserDto login(LoginDto loginDto) {

        String email = loginDto.email();
        String hashedPassword = hashPassword(loginDto, email);

        SorareSignInDto signInDto = sorareGraphQLService.signIn(new LoginDto(email, hashedPassword));
        UserDto user = userService.getUserBySorareSignInDto(signInDto.currentUser());

        sorareTokenService.invalidateActiveTokens(user.id());
        sorareTokenService.persistSorareToken(user, signInDto.jwtToken());

        return user;
    }

    private String hashPassword(LoginDto loginDto, String email) {
        String password = loginDto.password();
        String salt = sorareAuthService.getSalt(email).salt();
        return BCrypt.hashpw(password, salt);
    }
}
