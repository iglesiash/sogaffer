package es.unican.hgi834.sogaffer.controller;

import es.unican.hgi834.sogaffer.configuration.JwtPropertiesConfiguration;
import es.unican.hgi834.sogaffer.exception.InvalidCredentialsException;
import es.unican.hgi834.sogaffer.model.dto.auth.AccessRefreshTokenDto;
import es.unican.hgi834.sogaffer.model.dto.auth.AccessTokenDto;
import es.unican.hgi834.sogaffer.model.dto.auth.LoginDto;
import es.unican.hgi834.sogaffer.service.auth.IAuthService;
import es.unican.hgi834.sogaffer.service.auth.IJwtTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthenticationController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
@TestPropertySource(properties = "sogaffer.cookie.secure=false")
class AuthenticationControllerTest {


    private static final String EXPECTED_REFRESH_TOKEN_VALUE = "refresh_token_value";
    private static final String EXPECTED_ACCESS_TOKEN_VALUE = "token";
    private static final int EXPECTED_ACCESS_TOKEN_DURATION = 3600;

    private static final MockHttpServletRequestBuilder POST = post("/auth/login");
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    private static final String EMAIL = "correct@email.com";
    public static final String CORRECT_PASSWORD = "correct_password";
    public static final String INCORRECT_PASSWORD = "incorrect_password";

    private static final LoginDto CORRECT_PASSWORD_LOGIN_DTO = new LoginDto(EMAIL, CORRECT_PASSWORD);
    private static final LoginDto INCORRECT_PASSWORD_LOGIN_DTO = new LoginDto(EMAIL, INCORRECT_PASSWORD);


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthenticationController controller;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IAuthService authService;

    @MockitoBean
    private IJwtTokenService jwtTokenService;

    @MockitoBean
    private JwtPropertiesConfiguration jwtPropertiesConfiguration;

    @BeforeEach
    void setUp() {
        AccessTokenDto accessTokenDto = new AccessTokenDto(EXPECTED_ACCESS_TOKEN_VALUE, EXPECTED_ACCESS_TOKEN_DURATION);
        when(authService.login(CORRECT_PASSWORD_LOGIN_DTO))
                .thenReturn(new AccessRefreshTokenDto(accessTokenDto, EXPECTED_REFRESH_TOKEN_VALUE));
    }

    @Test
    @DisplayName("UAC.1a - login should return 200, correct access token and HttpOnly Secure=false cookie")
    void login_shouldReturn200WithNonSecureCookie() throws Exception {
        assertLoginResponseWithSecureFlag(false);
    }

    @Test
    @DisplayName("UAC.1b - login should return 200, correct access token and HttpOnly Secure=true cookie")
    void login_shouldReturn200WithSecureCookie() throws Exception {
        // Set the secure cookie flag to true only for this test
        String cookieSecurePropertyName = "isCookieSecure";
        ReflectionTestUtils.setField(controller, cookieSecurePropertyName, true);
        try {
            assertLoginResponseWithSecureFlag(true);
        } finally {
            // Reset the secure cookie flag to false for other tests
            ReflectionTestUtils.setField(controller, cookieSecurePropertyName, false);
        }
    }

    @Test
    @DisplayName("UAC.1c - login should return 400 when email is null")
    void login_shouldReturn400WhenEmailIsNull() throws Exception {
        LoginDto loginDto = new LoginDto(null, CORRECT_PASSWORD);

        mockMvc.perform(POST
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("UAC.1d - login should return 400 when email is blank")
    void login_shouldReturn400WhenEmailIsBlank() throws Exception {
        LoginDto loginDto = new LoginDto("", CORRECT_PASSWORD);

        mockMvc.perform(POST
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("UAC.1e - login should return 400 when password is null")
    void login_shouldReturn400WhenPasswordIsNull() throws Exception {
        LoginDto loginDto = new LoginDto(EMAIL, null);

        mockMvc.perform(POST
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("UAC.1f - login should return 400 when password is blank")
    void login_shouldReturn400WhenPasswordIsBlank() throws Exception {
        LoginDto loginDto = new LoginDto(EMAIL, "");

        mockMvc.perform(POST
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("UAC.1g - login should return 400 when request body is invalid JSON")
    void login_shouldReturn400WhenBodyIsInvalidJson() throws Exception {
        mockMvc.perform(POST
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("UAC.1h - login should return 401 when credentials are invalid")
    void login_shouldReturn401WhenCredentialsAreInvalid() throws Exception {
        when(authService.login(any(LoginDto.class)))
                .thenThrow(new InvalidCredentialsException());

        mockMvc.perform(POST
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(INCORRECT_PASSWORD_LOGIN_DTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("UAC.1i - login should return 500 when an unexpected exception occurs")
    void login_shouldReturn500WhenUnexpectedExceptionOccurs() throws Exception {
        when(authService.login(CORRECT_PASSWORD_LOGIN_DTO))
                .thenThrow(new RuntimeException("Unexpected error"));

        performValidLogin().andExpect(status().isInternalServerError());
    }

    private void assertLoginResponseWithSecureFlag(boolean isCookieSecure) throws Exception {
        performValidLogin()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(EXPECTED_ACCESS_TOKEN_VALUE))
                .andExpect(jsonPath("$.expiresIn").value(EXPECTED_ACCESS_TOKEN_DURATION))
                .andExpect(result -> {
                    Cookie cookie = result.getResponse().getCookie(REFRESH_TOKEN_COOKIE_NAME);
                    assertNotNull(cookie);
                    assertEquals(EXPECTED_REFRESH_TOKEN_VALUE, cookie.getValue());
                    assertTrue(cookie.isHttpOnly());
                    assertEquals(isCookieSecure, cookie.getSecure());
                });
    }

    private ResultActions performValidLogin() throws Exception {
        return mockMvc.perform(POST
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(CORRECT_PASSWORD_LOGIN_DTO)));
    }
}