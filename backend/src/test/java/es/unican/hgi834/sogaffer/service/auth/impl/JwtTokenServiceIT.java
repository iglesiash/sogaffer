package es.unican.hgi834.sogaffer.service.auth.impl;

import es.unican.hgi834.sogaffer.configuration.JwtPropertiesConfiguration;
import es.unican.hgi834.sogaffer.model.dto.auth.AccessTokenDto;
import es.unican.hgi834.sogaffer.service.auth.IJwtTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class JwtTokenServiceIT {

    private static final String EXPECTED_ALGORITHM = "HmacSHA256";
    private static final String USERNAME = "correct@email.com";

    @Autowired
    private JwtPropertiesConfiguration config;

    // System under test (SUT)
    @Autowired
    private IJwtTokenService sut;

    @Test
    @DisplayName("IIJTS.1a - generateToken should return a non-null DTO")
    void generateToken_shouldReturnNonNullDto() {
        AccessTokenDto accessTokenDto = sut.generateToken(USERNAME);
        assertNotNull(accessTokenDto);
        assertNotNull(accessTokenDto.accessToken());
    }

    @Test
    @DisplayName("IIJTS.1a - generated token should contain username as subject")
    void generateToken_shouldContainUsernameAsSubject() {
        Claims claims = parseClaims(sut.generateToken(USERNAME).accessToken());
        assertEquals(USERNAME, claims.getSubject());
    }

    @Test
    @DisplayName("IIJTS.1a - generated token should use configured AUD as issuer")
    void generateToken_shouldUseConfiguredAudAsIssuer() {
        Claims claims = parseClaims(sut.generateToken(USERNAME).accessToken());
        assertEquals(config.getAud(), claims.getIssuer());
    }

    @Test
    @DisplayName("IIJTS.1a - token expiration should equal issuedAt plus configured duration")
    void generateToken_shouldHaveExpirationBasedOnConfiguredDuration() {
        Claims claims = parseClaims(sut.generateToken(USERNAME).accessToken());
        long expirationMillis = claims.getExpiration().getTime();
        long issuedAtMillis = claims.getIssuedAt().getTime();
        assertEquals(config.getDuration(), (expirationMillis - issuedAtMillis) / 1000);
    }

    @Test
    @DisplayName("IIJTS.1b - should return a SecretKey with the expected algorithm")
    void getSigningKey_shouldReturnExpectedAlgorithm() {
        SecretKey signingKey = sut.getSigningKey();

        assertNotNull(signingKey);
        assertEquals(EXPECTED_ALGORITHM, signingKey.getAlgorithm());
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(sut.getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}