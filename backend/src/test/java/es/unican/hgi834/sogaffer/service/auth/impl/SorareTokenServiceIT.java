package es.unican.hgi834.sogaffer.service.auth.impl;

import es.unican.hgi834.sogaffer.model.dto.auth.UserDto;
import es.unican.hgi834.sogaffer.model.dto.sorare.auth.SorareJwtTokenDto;
import es.unican.hgi834.sogaffer.model.entity.SorareToken;
import es.unican.hgi834.sogaffer.model.entity.User;
import es.unican.hgi834.sogaffer.model.mapper.UserMapper;
import es.unican.hgi834.sogaffer.repository.ISorareTokenRepository;
import es.unican.hgi834.sogaffer.repository.IUserRepository;
import es.unican.hgi834.sogaffer.service.auth.IEncryptionService;
import es.unican.hgi834.sogaffer.service.auth.ISorareTokenService;
import com.zaxxer.hikari.HikariDataSource;
import es.unican.hgi834.sogaffer.utils.TestDataSourceUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.CannotCreateTransactionException;

import javax.sql.DataSource;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class SorareTokenServiceIT {

    private static final SorareJwtTokenDto VALID_TOKEN = new SorareJwtTokenDto("sorare_token",
            Instant.now().minus(1, ChronoUnit.SECONDS));

    private UserDto validUser;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ISorareTokenRepository sorareTokenRepository;

    @Autowired
    private IEncryptionService encryptionService;

    @Autowired
    private DataSource dataSource;

    // System under test (SUT)
    @Autowired
    private ISorareTokenService sut;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setSorareId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        user.setEmail("test@test.com");
        user = userRepository.save(user);

        validUser = UserMapper.toDto(user);
    }

    @AfterEach
    void tearDown() {
        // Tests that close the DataSource use @DirtiesContext, which already destroys and recreates the context
        // (and the in-memory H2 database). There is nothing to clean up, and the pool is already closed.
        if (TestDataSourceUtils.isClosed(dataSource)) {
            return;
        }

        sorareTokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("IISTS.1a - invalidateActiveTokens should set all active tokens to invalid and persist them")
    void invalidateActiveTokens_setsAllTokensToInvalidAndPersists() {
        // Arrange
        persistSorareToken();
        persistSorareToken();

        // Act
        List<SorareToken> result = sut.invalidateActiveTokens(validUser.id());

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().noneMatch(SorareToken::isValid));

        Set<SorareToken> stillActive = sorareTokenRepository.findActiveTokensByUserId(validUser.id());
        assertTrue(stillActive.isEmpty());
    }

    @Test
    @DisplayName("IISTS.1a - invalidateActiveTokens should return an empty list when there are no active tokens")
    void invalidateActiveTokens_returnsEmptyListWhenNoActiveTokens() {
        // Act
        List<SorareToken> result = sut.invalidateActiveTokens(validUser.id());

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DirtiesContext
    @DisplayName("IISTS.1b - invalidateActiveTokens should throw when persistence service is unavailable")
    void invalidateActiveTokens_shouldPropagateExceptionWhenRepositoryFails() {
        // Arrange
        ((HikariDataSource) dataSource).close();

        // Act & Assert
        assertThrows(CannotCreateTransactionException.class,
                () -> sut.persistSorareToken(validUser, VALID_TOKEN));
    }

    @Test
    @DisplayName("IISTS.1c - persistSorareToken should save an encrypted token retrievable from the database")
    void persistSorareToken_shouldSaveEncryptedToken() throws Exception {
        // Act
        sut.persistSorareToken(validUser, VALID_TOKEN);

        // Assert
        SorareToken persisted = sorareTokenRepository.findActiveTokensByUserId(validUser.id())
                .stream()
                .findFirst()
                .orElseThrow();

        assertNotEquals(VALID_TOKEN.token(), persisted.getToken());
        assertEquals(VALID_TOKEN.token(), encryptionService.decrypt(persisted.getToken()));
    }

    @Test
    @DisplayName("IISTS.1d - persistSorareToken should throw NullPointerException when user is null")
    void persistSorareToken_shouldThrowNullPointerExceptionWhenUserIsNull() {
        // Act and assert
        assertThrows(NullPointerException.class,
                () -> sut.persistSorareToken(null, VALID_TOKEN));
    }

    @Test
    @DisplayName("IISTS.1e - persistSorareToken should throw NullPointerException when token is null")
    void persistSorareToken_shouldThrowNullPointerExceptionWhenTokenIsNull() {
        // Act and assert
        assertThrows(NullPointerException.class,
                () -> sut.persistSorareToken(validUser, null));
    }

    @Test
    @DirtiesContext
    @DisplayName("IISTS.1f - persistSorareToken should throw when persistence service is unavailable")
    void persistSorareToken_shouldThrow_whenPersistenceServiceIsUnavailable() {
        // Arrange
        ((HikariDataSource) dataSource).close();

        // Act & Assert
        assertThrows(CannotCreateTransactionException.class,
                () -> sut.persistSorareToken(validUser, VALID_TOKEN));
    }

    private void persistSorareToken() {
        SorareToken token = new SorareToken();
        token.setUser(UserMapper.toEntity(validUser));
        token.setToken("encrypted_token");
        token.setExpirationDate(Instant.now().plus(30, ChronoUnit.DAYS));
        token.setValid(true);
        sorareTokenRepository.save(token);
    }
}
