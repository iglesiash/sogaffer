package es.unican.hgi834.sogaffer.service.auth.impl;

import es.unican.hgi834.sogaffer.model.dto.auth.AccessTokenDto;
import es.unican.hgi834.sogaffer.model.dto.sorare.auth.SorareCurrentUserDto;
import es.unican.hgi834.sogaffer.model.dto.sorare.auth.SorareJwtTokenDto;
import es.unican.hgi834.sogaffer.model.dto.sorare.auth.SorareSignInDto;
import es.unican.hgi834.sogaffer.model.entity.SorareToken;
import es.unican.hgi834.sogaffer.model.entity.User;
import es.unican.hgi834.sogaffer.repository.ISorareTokenRepository;
import es.unican.hgi834.sogaffer.repository.IUserRepository;
import es.unican.hgi834.sogaffer.service.auth.*;
import es.unican.hgi834.sogaffer.model.dto.auth.LoginDto;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
public class AuthService implements IAuthService {

    private final ISorareAuthService sorareAuthService;
    private final ISorareGraphQLService sorareGraphQLService;
    private final IJwtTokenService jwtTokenService;
    private final IEncryptionService encryptionService;

    // TODO: fetch from service instead of repository
    private final IUserRepository userRepository;
    private final ISorareTokenRepository sorareTokenRepository;

    public AuthService(ISorareAuthService sorareAuthService,
                       ISorareGraphQLService sorareGraphQLService,
                       IJwtTokenService jwtTokenService,
                       IEncryptionService encryptionService,
                       IUserRepository userRepository,
                       ISorareTokenRepository sorareTokenRepository) {
        this.sorareAuthService = sorareAuthService;
        this.sorareGraphQLService = sorareGraphQLService;
        this.jwtTokenService = jwtTokenService;
        this.encryptionService = encryptionService;
        this.userRepository = userRepository;
        this.sorareTokenRepository = sorareTokenRepository;
    }

    @Override
    @Transactional
    public AccessTokenDto login(LoginDto loginDto) {
        String email = loginDto.email();
        SorareToken existingSorareToken = sorareTokenRepository.findByUserEmail(email);

        // If no valid token exists, call Sorare
        if (existingSorareToken == null) {
            String password = loginDto.password();
            String salt = sorareAuthService.getSalt(email).salt();
            String hashedPassword = BCrypt.hashpw(password, salt);

            SorareSignInDto signInDto = sorareGraphQLService.signIn(new LoginDto(email, hashedPassword));
            User user = getUserBySorareSignInDto(signInDto.currentUser());

            invalidateActiveTokens(user);
            createToken(user, signInDto.jwtToken());
        }

        return jwtTokenService.generateToken(email);
    }

    private void invalidateActiveTokens(User user) {
        Set<SorareToken> activeTokens = sorareTokenRepository.findActiveTokensByUserId(user.getId());
        activeTokens.forEach(token -> token.setValid(false));

        sorareTokenRepository.saveAll(activeTokens);
    }

    private User getUserBySorareSignInDto(SorareCurrentUserDto currentUser) {
        String email = currentUser.email();
        UUID userUUID = UUID.fromString(currentUser.sorareId().replace("User:", ""));

        // Upsert user according to the Sorare ID
        User user = userRepository.findBySorareId(userUUID);
        if (user == null) {
            // Insert
            user = new User();
            user.setSorareId(userUUID);
        }

        // Set email whatsoever
        user.setEmail(email);
        return userRepository.save(user);
    }

    private void createToken(User user, SorareJwtTokenDto jwtTokenDto) {
        SorareToken sorareToken = new SorareToken();
        sorareToken.setUser(user);
        sorareToken.setToken(encryptToken(jwtTokenDto.token()));
        sorareToken.setExpirationDate(jwtTokenDto.expiredAt());
        sorareToken.setValid(true);

        sorareTokenRepository.save(sorareToken);
    }

    private String encryptToken(String token) {
        String encryptedToken;
        try {
            encryptedToken = encryptionService.encrypt(token);
        } catch (Exception e) {
            // TODO: custom exception
            throw new RuntimeException("Error encrypting token", e);
        }

        return encryptedToken;
    }
}
