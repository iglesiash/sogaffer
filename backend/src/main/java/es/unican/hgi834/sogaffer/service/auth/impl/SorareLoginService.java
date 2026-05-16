package es.unican.hgi834.sogaffer.service.auth.impl;

import es.unican.hgi834.sogaffer.model.dto.auth.LoginDto;
import es.unican.hgi834.sogaffer.model.dto.sorare.auth.SorareSignInDto;
import es.unican.hgi834.sogaffer.model.entity.SorareToken;
import es.unican.hgi834.sogaffer.model.entity.User;
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
    public User login(LoginDto loginDto) {

        String email = loginDto.email();
        SorareToken existingSorareToken = sorareTokenService.getSorareTokenByEmail(email);

        // A valid token exists, so the user also exists
        if (existingSorareToken != null) {
            return userService.getByEmail(email);
        }

        // If no valid token exists, call Sorare
        String password = loginDto.password();
        String salt = sorareAuthService.getSalt(email).salt();
        String hashedPassword = BCrypt.hashpw(password, salt);

        SorareSignInDto signInDto = sorareGraphQLService.signIn(new LoginDto(email, hashedPassword));
        User user = userService.getUserBySorareSignInDto(signInDto.currentUser());

        sorareTokenService.invalidateActiveTokens(user.getId());
        sorareTokenService.persistSorareToken(user, signInDto.jwtToken());

        return user;
    }
}
