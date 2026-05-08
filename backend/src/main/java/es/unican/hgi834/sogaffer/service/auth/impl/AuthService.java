package es.unican.hgi834.sogaffer.service.auth.impl;

import es.unican.hgi834.sogaffer.model.dto.auth.AccessTokenDto;
import es.unican.hgi834.sogaffer.model.dto.sorare.graphql.SorareGraphQLResponse;
import es.unican.hgi834.sogaffer.model.dto.sorare.auth.SorareSignInWrapperDto;
import es.unican.hgi834.sogaffer.service.auth.ISorareAuthService;
import es.unican.hgi834.sogaffer.model.dto.auth.LoginDto;
import es.unican.hgi834.sogaffer.service.auth.IAuthService;
import es.unican.hgi834.sogaffer.service.auth.ISorareGraphQLService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {

    private final ISorareAuthService sorareAuthService;
    private final ISorareGraphQLService sorareGraphQLService;

    public AuthService(ISorareAuthService sorareAuthService,
                       ISorareGraphQLService sorareGraphQLService) {
        this.sorareAuthService = sorareAuthService;
        this.sorareGraphQLService = sorareGraphQLService;
    }

    @Override
    public AccessTokenDto login(LoginDto loginDto) {
        String email = loginDto.email();
        String password = loginDto.password();

        String salt = sorareAuthService.getSalt(email).salt();
        String hashedPassword = BCrypt.hashpw(password, salt);

        SorareSignInWrapperDto signInDto =
                sorareGraphQLService.signIn(new LoginDto(email, hashedPassword));
        return new AccessTokenDto(signInDto.signIn().jwtToken().token(), 1);
    }
}
