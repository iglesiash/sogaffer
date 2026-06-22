package es.unican.hgi834.sogaffer.service.auth.impl;

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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SorareGraphQLServiceTest {

    private static final String AUD = "SoGaffer";

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private RestClient.RequestBodySpec requestBodySpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @Mock
    private JwtPropertiesConfiguration jwtPropertiesConfiguration;

    private MockedStatic<GraphQLQueryLoader> mockedGraphQlQueryLoader;

    // Stub under test (SUT)
    private SorareGraphQLService sut;

    @BeforeEach
    void setUp() {

        // Mock mutation
        mockedGraphQlQueryLoader = mockStatic(GraphQLQueryLoader.class);
        mockedGraphQlQueryLoader.when(GraphQLQueryLoader::getSignInMutation).thenReturn("Mocked mutation");

        // JwtPropertiesConfiguration
        when(jwtPropertiesConfiguration.getAud()).thenReturn(AUD);

        sut = new SorareGraphQLService(restClient, jwtPropertiesConfiguration);
    }

    @AfterEach
    void tearDown() {
        mockedGraphQlQueryLoader.close();
    }

    @Test
    @DisplayName("UISGQLS.1a - signIn should return a valid JWT token when credentials are valid")
    void signIn_shouldReturnJwtToken_whenCredentialsAreValid() {

        // Arrange
        LoginDto loginDto = new LoginDto("correct@email.com", "hashed_password");
        Map<String, Object> variables = Map.of(
                "input", loginDto,
                "aud", AUD);

        Map<String, Object> requestBody = Map.of(
                "operationName", "SignInMutation",
                "query", GraphQLQueryLoader.getSignInMutation(),
                "variables", variables
        );

        SorareCurrentUserDto currentUser = new SorareCurrentUserDto("00000000-0000-0000-0000-0000000000000", loginDto.email());
        SorareJwtTokenDto jwtToken = new SorareJwtTokenDto("sorare_token", Instant.now().plus(30, ChronoUnit.DAYS));
        SorareSignInDto signInDto = new SorareSignInDto(currentUser, jwtToken, List.of());
        SorareSignInWrapperDto data = new SorareSignInWrapperDto(signInDto);

        SorareGraphQLResponse<SorareSignInWrapperDto> response = new SorareGraphQLResponse<>(data, List.of());

        // RestClient
        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(requestBodyUriSpec.body(requestBody)).thenReturn(requestBodySpec);
        when(responseSpec.body(any(ParameterizedTypeReference.class)))
                .thenReturn(response);

        // Act
        SorareSignInDto signInResponse = sut.signIn(loginDto);

        // Assert
        assertEquals(signInResponse, signInDto);
    }

    @Test
    @DisplayName("UISGQLS.1b - signIn should throw exception when GraphQL mutation cannot be read")
    void signIn_shouldThrowExceptionWhenMutationCannotBeRead() {

        // Arrange
        mockedGraphQlQueryLoader.when(GraphQLQueryLoader::getSignInMutation)
                    .thenThrow(new IllegalStateException("The mutation file could not be read"));

        // Act and assert
        assertThrows(IllegalStateException.class, () -> sut.signIn(new LoginDto("correct@email.com", "hashed_password")));
    }

    @Test
    @DisplayName("UISGQLS.1c - signIn should throw InvalidCredentialsException when credentials are invalid")
    void signIn_shouldThrowInvalidCredentialsException_whenCredentialsAreInvalid() {

        // Arrange
        LoginDto loginDto = new LoginDto("correct@email.com", "incorrect_hashed_password");
        Map<String, Object> variables = Map.of(
                "input", loginDto,
                "aud", AUD);

        Map<String, Object> requestBody = Map.of(
                "operationName", "SignInMutation",
                "query", GraphQLQueryLoader.getSignInMutation(),
                "variables", variables
        );

        SorareCurrentUserDto currentUser = new SorareCurrentUserDto("00000000-0000-0000-0000-0000000000000", loginDto.email());
        SorareJwtTokenDto jwtToken = new SorareJwtTokenDto("sorare_token", Instant.now().plus(30, ChronoUnit.DAYS));
        SorareSignInDto signInDto = new SorareSignInDto(currentUser, jwtToken, List.of(new SorareApiErrorDto("Invalid credentials")));
        SorareSignInWrapperDto data = new SorareSignInWrapperDto(signInDto);

        SorareGraphQLResponse<SorareSignInWrapperDto> response = new SorareGraphQLResponse<>(data, List.of());
        response = new SorareGraphQLResponse<>(data, List.of());

        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(requestBodyUriSpec.body(requestBody)).thenReturn(requestBodySpec);
        when(responseSpec.body(any(ParameterizedTypeReference.class)))
                .thenReturn(response);

        // Act and assert
        assertThrows(InvalidCredentialsException.class, () -> sut.signIn(new LoginDto("correct@email.com", "incorrect_hashed_password")));
    }

    @Test
    @DisplayName("UISGQLS.1d - signIn should throw exception when Sorare service is unavailable")
    void signIn_shouldThrowExceptionWhenServiceIsUnavailable() {

        // Arrange
        LoginDto loginDto = new LoginDto("correct@email.com", "hashed_password");
        Map<String, Object> variables = Map.of(
                "input", loginDto,
                "aud", AUD);

        Map<String, Object> requestBody = Map.of(
                "operationName", "SignInMutation",
                "query", GraphQLQueryLoader.getSignInMutation(),
                "variables", variables
        );

        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(requestBodyUriSpec.body(requestBody)).thenReturn(requestBodySpec);
        when(responseSpec.body(any(ParameterizedTypeReference.class)))
                .thenThrow(new RuntimeException("Sorare service unavailable"));

        // Act and assert
        assertThrows(RuntimeException.class, () -> sut.signIn(loginDto));
    }
}