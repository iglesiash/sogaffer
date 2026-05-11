package es.unican.hgi834.sogaffer.service.auth.impl;

import es.unican.hgi834.sogaffer.model.dto.auth.AccessTokenDto;
import es.unican.hgi834.sogaffer.model.dto.sorare.auth.SorareSignInDto;
import es.unican.hgi834.sogaffer.model.entity.SorareToken;
import es.unican.hgi834.sogaffer.model.entity.User;
import es.unican.hgi834.sogaffer.repository.ISorareTokenRepository;
import es.unican.hgi834.sogaffer.repository.IUserRepository;
import es.unican.hgi834.sogaffer.service.auth.IJwtTokenService;
import es.unican.hgi834.sogaffer.service.auth.ISorareAuthService;
import es.unican.hgi834.sogaffer.model.dto.auth.LoginDto;
import es.unican.hgi834.sogaffer.service.auth.IAuthService;
import es.unican.hgi834.sogaffer.service.auth.ISorareGraphQLService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService implements IAuthService {

    private final ISorareAuthService sorareAuthService;
    private final ISorareGraphQLService sorareGraphQLService;
    private final IJwtTokenService jwtTokenService;

    private final IUserRepository userRepository;
    private final ISorareTokenRepository sorareTokenRepository;

    public AuthService(ISorareAuthService sorareAuthService,
                       ISorareGraphQLService sorareGraphQLService,
                       IJwtTokenService jwtTokenService,
                       IUserRepository userRepository,
                       ISorareTokenRepository sorareTokenRepository) {
        this.sorareAuthService = sorareAuthService;
        this.sorareGraphQLService = sorareGraphQLService;
        this.jwtTokenService = jwtTokenService;
        this.userRepository = userRepository;
        this.sorareTokenRepository = sorareTokenRepository;
    }

    @Override
    public AccessTokenDto login(LoginDto loginDto) {
        String email = loginDto.email();
        String password = loginDto.password();

        String salt = sorareAuthService.getSalt(email).salt();
        String hashedPassword = BCrypt.hashpw(password, salt);

        SorareSignInDto signInDto = sorareGraphQLService.signIn(new LoginDto(email, hashedPassword));
        SorareToken sorareToken = new SorareToken();

        String userId = signInDto.currentUser().sorareId();
        UUID userUUID = UUID.fromString(userId.replace("User:", ""));
        User user = userRepository.findBySorareId(userUUID);

        if (user == null) {
            // User does not exist in the DB; create it
            user = new User();
            user.setSorareId(userUUID);
            user = userRepository.save(user);
        }

        sorareToken.setUser(user);
        sorareToken.setToken(signInDto.jwtToken().token());
        sorareTokenRepository.save(sorareToken);

        return jwtTokenService.generateToken(userId);
    }
}
