package es.unican.hgi834.sogaffer.service.auth.impl;

import es.unican.hgi834.sogaffer.model.dto.auth.UserDto;
import es.unican.hgi834.sogaffer.model.entity.RefreshToken;
import es.unican.hgi834.sogaffer.model.mapper.UserMapper;
import es.unican.hgi834.sogaffer.repository.IRefreshTokenRepository;
import es.unican.hgi834.sogaffer.service.auth.IRefreshTokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

@Service
public class RefreshTokenService implements IRefreshTokenService {

    private static final int TOKEN_BYTES = 32;
    public static final int TTL = 7 * 24 * 60 * 60; // 7 days

    private final IRefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public RefreshTokenService(IRefreshTokenRepository refreshTokenRepository,
                               PasswordEncoder passwordEncoder) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String generateRefreshToken(UserDto userDto) {
        byte[] randomBytes = new byte[TOKEN_BYTES];
        new SecureRandom().nextBytes(randomBytes);
        String rawToken = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(UserMapper.toEntity(userDto));
        refreshToken.setHashedToken(passwordEncoder.encode(rawToken));
        refreshToken.setExpirationDate(Instant.now().plusSeconds(TTL));
        refreshTokenRepository.save(refreshToken);

        return rawToken;
    }
}