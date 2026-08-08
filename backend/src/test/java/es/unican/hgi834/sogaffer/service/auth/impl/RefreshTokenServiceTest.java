package es.unican.hgi834.sogaffer.service.auth.impl;

import es.unican.hgi834.sogaffer.model.dto.auth.UserDto;
import es.unican.hgi834.sogaffer.model.entity.RefreshToken;
import es.unican.hgi834.sogaffer.repository.IRefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Base64;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {
    private static final int TOKEN_BYTES = 32;

    private static final UserDto VALID_USER = new UserDto(
            1,
            UUID.fromString("00000000-0000-0000-0000-000000000000"),
            "test@test.com",
            null
    );

    @Mock
    private IRefreshTokenRepository refreshTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    // Stub under test (SUT)
    private RefreshTokenService sut;

    @BeforeEach
    void setUp() {
        sut = new RefreshTokenService(refreshTokenRepository, passwordEncoder);
    }

    @Test
    @DisplayName("UIRTS.1a - generateRefreshToken should return a non-null token for a valid user")
    void generateRefreshToken_shouldReturnTokenForValidUser() {
        // Arrange
        when(passwordEncoder.encode(anyString())).thenReturn("hashed-token");

        // Act
        String token = sut.generateRefreshToken(VALID_USER);

        // Assert
        assertNotNull(token);
    }

    @Test
    @DisplayName("UIRTS.1a - generated refresh token should have expected byte length when decoded")
    void generateRefreshToken_shouldHaveExpectedDecodedByteLength() {
        // Arrange
        when(passwordEncoder.encode(anyString())).thenReturn("hashed-token");

        // Act
        String token = sut.generateRefreshToken(VALID_USER);

        // Assert
        assertEquals(TOKEN_BYTES, Base64.getUrlDecoder().decode(token).length);
    }

    @Test
    @DisplayName("UIRTS.1a - each call to generateRefreshToken should produce a different token")
    void generateRefreshToken_shouldProduceDifferentTokenOnEachCall() {
        // Arrange
        when(passwordEncoder.encode(anyString())).thenReturn("hashed-token");

        // Act
        String first  = sut.generateRefreshToken(VALID_USER);
        String second = sut.generateRefreshToken(VALID_USER);

        // Assert
        assertNotEquals(first, second);
    }

    @Test
    @DisplayName("UIRTS.1a - generateRefreshToken should persist a hashed token")
    void generateRefreshToken_validUser_persistsToken() {
        // Arrange
        when(passwordEncoder.encode(anyString())).thenReturn("hashed-token");

        // Act
        sut.generateRefreshToken(VALID_USER);

        // Assert
        verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
    }

    @Test
    @DisplayName("UIRTS.1b - generateRefreshToken should throw exception when user is null")
    void generateRefreshToken_shouldThrowExceptionWhenUserIsNull() {
        // Act and assert
        assertThrows(NullPointerException.class,
                () -> sut.generateRefreshToken(null));
    }

    @Test
    @DisplayName("UIRTS.1c - generateRefreshToken should propagate repository failures as exception")
    void generateRefreshToken_shouldPropagateRepositoryFailure() {
        // Arrange
        String errorMessage = "The refresh token could not be persisted";

        when(passwordEncoder.encode(anyString())).thenReturn("hashed-token");
        when(refreshTokenRepository.save(any(RefreshToken.class)))
                .thenThrow(new RuntimeException(errorMessage));

        // Act and assert
        Exception e = assertThrows(RuntimeException.class,
                () -> sut.generateRefreshToken(VALID_USER));
        assertEquals(errorMessage, e.getMessage());
    }
}