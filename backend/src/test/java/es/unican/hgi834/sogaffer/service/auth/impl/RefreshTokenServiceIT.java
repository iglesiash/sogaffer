package es.unican.hgi834.sogaffer.service.auth.impl;

import com.zaxxer.hikari.HikariDataSource;
import es.unican.hgi834.sogaffer.model.dto.auth.UserDto;
import es.unican.hgi834.sogaffer.model.entity.RefreshToken;
import es.unican.hgi834.sogaffer.model.entity.User;
import es.unican.hgi834.sogaffer.model.mapper.UserMapper;
import es.unican.hgi834.sogaffer.repository.IRefreshTokenRepository;
import es.unican.hgi834.sogaffer.repository.IUserRepository;
import es.unican.hgi834.sogaffer.service.auth.IRefreshTokenService;
import es.unican.hgi834.sogaffer.utils.TestDataSourceUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.Base64;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class RefreshTokenServiceIT {
    private static final int TOKEN_BYTES = 32;

    private UserDto validUser;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRefreshTokenRepository refreshTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DataSource dataSource;

    // System under test (SUT)
    @Autowired
    private IRefreshTokenService sut;

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

        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("IIRTS.1a - generateRefreshToken should return a non-null token for a valid user")
    void generateRefreshToken_shouldReturnTokenForValidUser() {
        // Act
        String token = sut.generateRefreshToken(validUser);

        // Assert
        assertNotNull(token);
    }

    @Test
    @DisplayName("IIRTS.1a - generated refresh token should have expected byte length when decoded")
    void generateRefreshToken_shouldHaveExpectedDecodedByteLength() {
        // Act
        String token = sut.generateRefreshToken(validUser);

        // Assert
        assertEquals(TOKEN_BYTES, Base64.getUrlDecoder().decode(token).length);
    }

    @Test
    @DisplayName("IIRTS.1a - each call to generateRefreshToken should produce a different token")
    void generateRefreshToken_shouldProduceDifferentTokenOnEachCall() {
        // Act
        String first = sut.generateRefreshToken(validUser);
        String second = sut.generateRefreshToken(validUser);

        // Assert
        assertNotEquals(first, second);
    }

    @Test
    @DisplayName("IIRTS.1a - generateRefreshToken should persist a hashed token retrievable from the database")
    void generateRefreshToken_validUser_persistsHashedToken() {
        // Act
        String token = sut.generateRefreshToken(validUser);

        // Assert
        RefreshToken persisted = refreshTokenRepository.findAll().stream()
                .filter(rt -> rt.getUser().getId() == validUser.id())
                .findFirst()
                .orElseThrow();

        assertTrue(passwordEncoder.matches(token, persisted.getHashedToken()));
    }

    @Test
    @DisplayName("IIRTS.1b - generateRefreshToken should throw exception when user is null")
    void generateRefreshToken_shouldThrowExceptionWhenUserIsNull() {
        // Act and assert
        assertThrows(NullPointerException.class,
                () -> sut.generateRefreshToken(null));
    }

    @Test
    @DirtiesContext
    @DisplayName("IIRTS.1c - generateRefreshToken should propagate repository failures as exception")
    void generateRefreshToken_shouldPropagateRepositoryFailure() {
        // Arrange
        ((HikariDataSource) dataSource).close();

        // Act and assert
        assertThrows(CannotCreateTransactionException.class, () -> sut.generateRefreshToken(validUser));
    }
}
