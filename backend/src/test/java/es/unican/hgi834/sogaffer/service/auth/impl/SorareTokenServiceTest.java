package es.unican.hgi834.sogaffer.service.auth.impl;

import es.unican.hgi834.sogaffer.model.dto.auth.UserDto;
import es.unican.hgi834.sogaffer.model.dto.sorare.auth.SorareJwtTokenDto;
import es.unican.hgi834.sogaffer.model.entity.SorareToken;
import es.unican.hgi834.sogaffer.repository.ISorareTokenRepository;
import es.unican.hgi834.sogaffer.service.auth.IEncryptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SorareTokenServiceTest {

    private static final UserDto VALID_USER = new UserDto(
            1,
            UUID.fromString("00000000-0000-0000-0000-000000000000"),
            "test@test.com",
            null
    );
    public static final SorareJwtTokenDto VALID_TOKEN = new SorareJwtTokenDto("sorare_token",
            Instant.now().minus(1, ChronoUnit.SECONDS));

    @Mock
    private ISorareTokenRepository sorareTokenRepository;

    @Mock
    private IEncryptionService encryptionService;

    private SorareTokenService sut;

    @BeforeEach
    void setUp() {
        sut = new SorareTokenService(sorareTokenRepository, encryptionService);
    }

    @Test
    @DisplayName("UISTS.1a - invalidateActiveTokens should set all active tokens to invalid")
    void UISTS_1a_invalidateActiveTokens_setsAllTokensToInvalid() {
        // Arrange
        SorareToken token1 = new SorareToken();
        SorareToken token2 = new SorareToken();
        token1.setValid(true);
        token2.setValid(true);
        Set<SorareToken> activeTokens = Set.of(token1, token2);

        when(sorareTokenRepository.findActiveTokensByUserId(1)).thenReturn(activeTokens);

        // Act
        sut.invalidateActiveTokens(1);

        // Assert
        assertFalse(token1.isValid());
        assertFalse(token2.isValid());
    }

    @Test
    @DisplayName("UISTS.1a - invalidateActiveTokens should call repository saveAll with updated tokens")
    void invalidateActiveTokens_shouldCallSaveAllWithUpdatedTokens() {
        // Arrange
        SorareToken token1 = new SorareToken();
        token1.setValid(true);

        SorareToken token2 = new SorareToken();
        token2.setValid(true);

        Set<SorareToken> activeTokens = Set.of(token1, token2);

        when(sorareTokenRepository.findActiveTokensByUserId(1)).thenReturn(activeTokens);

        // Act
        sut.invalidateActiveTokens(1);

        // Assert
        verify(sorareTokenRepository, times(1)).saveAll(activeTokens);
    }

    @Test
    @DisplayName("UISTS.1b - invalidateActiveTokens should propagate exception when repository fails")
    void invalidateActiveTokens_shouldPropagateExceptionWhenRepositoryFails() {
        // Arrange
        String errorMessage = "Persistence service unavailable";
        when(sorareTokenRepository.findActiveTokensByUserId(1))
                .thenThrow(new RuntimeException(errorMessage));

        // Act and assert
        Exception exception = assertThrows(RuntimeException.class, () -> sut.invalidateActiveTokens(1));
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    @DisplayName("UISTS.1c - persistSorareToken should save token when valid inputs are provided")
    void persistSorareToken_shouldSaveTokenWhenValidInputsAreProvided() throws Exception {
        // Arrange
        when(encryptionService.encrypt(anyString())).thenReturn("encrypted_token");

        // Act
        sut.persistSorareToken(VALID_USER, VALID_TOKEN);

        // Assert
        verify(sorareTokenRepository, times(1)).save(any(SorareToken.class));
    }


    @Test
    @DisplayName("UISTS.1d - persistSorareToken should throw NullPointerException when user is null")
    void persistSorareToken_shouldThrowNullPointerExceptionWhenUserIsNull() {
        // Act and assert
        assertThrows(NullPointerException.class,
                () -> sut.persistSorareToken(null, VALID_TOKEN));
    }

    @Test
    @DisplayName("UISTS.1e - persistSorareToken should throw NullPointerException when token is null")
    void persistSorareToken_shouldThrowNullPointerExceptionWhenTokenIsNull() {
        // Act and assert
        assertThrows(NullPointerException.class,
                () -> sut.persistSorareToken(VALID_USER, null));
    }

    @Test
    @DisplayName("UISTS.1f - persistSorareToken should propagate repository exception unwrapped")
    void persistSorareToken_shouldPropagateRepositoryExceptionUnwrapped() throws Exception {
        // Arrange
        when(encryptionService.encrypt(anyString())).thenReturn("sorare_token");

        RuntimeException dbException = new RuntimeException("DB unavailable");
        doThrow(dbException).when(sorareTokenRepository).save(any(SorareToken.class));

        // Act and assert
        assertSame(dbException, assertThrows(RuntimeException.class,
                () -> sut.persistSorareToken(VALID_USER, VALID_TOKEN)));
    }
}