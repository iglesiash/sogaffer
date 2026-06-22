package es.unican.hgi834.sogaffer.service.auth.impl;

import es.unican.hgi834.sogaffer.configuration.JwtPropertiesConfiguration;
import es.unican.hgi834.sogaffer.model.dto.auth.AccessTokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtTokenServiceTest {

    private static final String SECRET =
            Base64.getEncoder().encodeToString("illkeepyoumydirtylittlesecretaar".getBytes(StandardCharsets.UTF_8));
    private static final String AUD = "SoGaffer";
    private static final int DURATION = 3600;
    private static final String EXPECTED_ALGORITHM = "HmacSHA256";
    private static final String USERNAME = "correct@email.com";

    @Mock
    private JwtPropertiesConfiguration config;

    // Stub under test (SUT)
    private JwtTokenService sut;

    @BeforeEach
    void setUp() {
        when(config.getSecret()).thenReturn(SECRET);
        when(config.getAud()).thenReturn(AUD);
        when(config.getDuration()).thenReturn(DURATION);

        sut = new JwtTokenService(config);
    }

    @Test
    @DisplayName("UIJTS.1a - generateToken should return a non-null DTO")
    void generateToken_shouldReturnNonNullDto() {
        AccessTokenDto accessTokenDto = sut.generateToken(USERNAME);
        assertNotNull(accessTokenDto);
        assertNotNull(accessTokenDto.accessToken());
    }

    @Test
    @DisplayName("UIJTS.1a - generated token should contain username as subject")
    void generateToken_shouldContainUsernameAsSubject() {
        Claims claims = parseClaims(sut.generateToken(USERNAME).accessToken());
        assertEquals(USERNAME, claims.getSubject());
    }

    @Test
    @DisplayName("UIJTS.1a - generated token should use configured AUD as issuer")
    void generateToken_shouldUseConfiguredAudAsIssuer() {
        Claims claims = parseClaims(sut.generateToken(USERNAME).accessToken());
        assertEquals(AUD, claims.getIssuer());
    }

    @Test
    @DisplayName("UIJTS.1a - token expiration should equal issuedAt plus configured duration")
    void generateToken_shouldHaveExpirationBasedOnConfiguredDuration() {
        Claims claims = parseClaims(sut.generateToken(USERNAME).accessToken());
        long expirationMillis = claims.getExpiration().getTime();
        long issuedAtMillis   = claims.getIssuedAt().getTime();
        assertEquals(DURATION, (expirationMillis - issuedAtMillis) / 1000);
    }

    // The signing key uses the expected algorithm
    @Test
    void UIJTS_1b_getSigningKey() {
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