package es.unican.hgi834.sogaffer.service.auth.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sun.net.httpserver.HttpServer;
import es.unican.hgi834.sogaffer.configuration.JwtPropertiesConfiguration;
import es.unican.hgi834.sogaffer.exception.InvalidCredentialsException;
import es.unican.hgi834.sogaffer.model.dto.auth.LoginDto;
import es.unican.hgi834.sogaffer.model.dto.sorare.auth.SorareCurrentUserDto;
import es.unican.hgi834.sogaffer.model.dto.sorare.auth.SorareJwtTokenDto;
import es.unican.hgi834.sogaffer.model.dto.sorare.auth.SorareSignInDto;
import es.unican.hgi834.sogaffer.model.dto.sorare.auth.SorareSignInWrapperDto;
import es.unican.hgi834.sogaffer.model.dto.sorare.error.SorareApiErrorDto;
import es.unican.hgi834.sogaffer.model.dto.sorare.graphql.SorareGraphQLResponse;
import es.unican.hgi834.sogaffer.utils.GraphQLQueryLoader;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

/**
 * Integration tests for {@link es.unican.hgi834.sogaffer.service.auth.impl.SorareGraphQLService}.
 * Instead of mocking the {@link RestClient} fluent chain, these tests exercise a real HTTP
 * round-trip (request serialization, response deserialization) against an embedded HTTP server
 * standing in for the Sorare GraphQL API.
 */
class SorareGraphQLServiceIT {

    private static final String AUD = "SoGaffer";
    private static final LoginDto VALID_LOGIN = new LoginDto("correct@email.com", "hashed_password");
    private static final LoginDto INVALID_LOGIN = new LoginDto("correct@email.com", "incorrect_hashed_password");

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    private static HttpServer httpServer;
    private static volatile String responseBody;
    private static volatile int responseStatus;

    // System under test (SUT)
    private SorareGraphQLService sut;

    @BeforeAll
    static void startServer() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
        httpServer.createContext("/", exchange -> {
            byte[] bytes = responseBody == null ? new byte[0] : responseBody.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(responseStatus, bytes.length);
            exchange.getResponseBody().write(bytes);
            exchange.getResponseBody().close();
        });
        httpServer.start();
    }

    @AfterAll
    static void stopServer() {
        httpServer.stop(0);
    }

    @BeforeEach
    void setUp() {
        responseStatus = 200;
        responseBody = null;

        JwtPropertiesConfiguration jwtPropertiesConfiguration = new JwtPropertiesConfiguration();
        jwtPropertiesConfiguration.setAud(AUD);

        RestClient restClient = RestClient.builder()
                .baseUrl("http://localhost:" + httpServer.getAddress().getPort())
                .build();

        sut = new SorareGraphQLService(restClient, jwtPropertiesConfiguration);
    }

    @Test
    @DisplayName("IISGQLS.1a - signIn should return a valid JWT token when credentials are valid")
    void signIn_shouldReturnJwtToken_whenCredentialsAreValid() throws Exception {
        // Arrange
        SorareGraphQLResponse<SorareSignInWrapperDto> response = buildSignInResponse(VALID_LOGIN.email(), List.of());
        responseBody = OBJECT_MAPPER.writeValueAsString(response);

        // Act
        SorareSignInDto signInResponse = sut.signIn(VALID_LOGIN);

        // Assert
        assertEquals(response.data().signIn(), signInResponse);
    }

    @Test
    @DisplayName("IISGQLS.1b - signIn should throw exception when GraphQL mutation cannot be read")
    void signIn_shouldThrowExceptionWhenMutationCannotBeRead() {
        try (MockedStatic<GraphQLQueryLoader> mockedGraphQlQueryLoader = mockStatic(GraphQLQueryLoader.class)) {
            // Arrange
            mockedGraphQlQueryLoader.when(GraphQLQueryLoader::getSignInMutation)
                    .thenThrow(new IllegalStateException("The mutation file could not be read"));

            // Act and assert
            assertThrows(IllegalStateException.class, () -> sut.signIn(VALID_LOGIN));
        }
    }

    @Test
    @DisplayName("IISGQLS.1c - signIn should throw InvalidCredentialsException when credentials are invalid")
    void signIn_shouldThrowInvalidCredentialsException_whenCredentialsAreInvalid() throws Exception {
        // Arrange
        List<SorareApiErrorDto> errors = List.of(new SorareApiErrorDto("Invalid credentials"));
        SorareGraphQLResponse<SorareSignInWrapperDto> response = buildSignInResponse(INVALID_LOGIN.email(), errors);
        responseBody = OBJECT_MAPPER.writeValueAsString(response);

        // Act and assert
        assertThrows(InvalidCredentialsException.class, () -> sut.signIn(INVALID_LOGIN));
    }

    @Test
    @DisplayName("IISGQLS.1d - signIn should throw exception when Sorare service is unavailable")
    void signIn_shouldThrowExceptionWhenServiceIsUnavailable() {
        // Arrange
        responseStatus = 503;
        responseBody = "";

        // Act and assert
        assertThrows(RuntimeException.class, () -> sut.signIn(VALID_LOGIN));
    }

    /* Auxiliary methods */

    private SorareGraphQLResponse<SorareSignInWrapperDto> buildSignInResponse(
            String email,
            List<SorareApiErrorDto> errors) {

        SorareCurrentUserDto currentUser =
                new SorareCurrentUserDto("00000000-0000-0000-0000-0000000000000", email);
        SorareJwtTokenDto jwtToken =
                new SorareJwtTokenDto("sorare_token", Instant.now().plus(30, ChronoUnit.DAYS));
        SorareSignInDto signInDto = new SorareSignInDto(currentUser, jwtToken, errors);
        SorareSignInWrapperDto data = new SorareSignInWrapperDto(signInDto);

        return new SorareGraphQLResponse<>(data, List.of());
    }
}
