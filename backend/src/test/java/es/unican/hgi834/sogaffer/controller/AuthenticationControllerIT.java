package es.unican.hgi834.sogaffer.controller;

import es.unican.hgi834.sogaffer.exception.InvalidCredentialsException;
import es.unican.hgi834.sogaffer.model.dto.auth.LoginDto;
import es.unican.hgi834.sogaffer.model.dto.auth.UserDto;
import es.unican.hgi834.sogaffer.model.entity.User;
import es.unican.hgi834.sogaffer.model.mapper.UserMapper;
import es.unican.hgi834.sogaffer.repository.IRefreshTokenRepository;
import es.unican.hgi834.sogaffer.repository.IUserRepository;
import es.unican.hgi834.sogaffer.service.auth.ISorareLoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthenticationControllerIT {

    private static final int EXPECTED_ACCESS_TOKEN_DURATION = 3600;
    private static final String EMAIL = "correct@email.com";
    private static final String CORRECT_PASSWORD = "correct_password";
    private static final String INCORRECT_PASSWORD = "incorrect_password";
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

    private static final MockHttpServletRequestBuilder POST = post("/auth/login");

    private static final LoginDto CORRECT_PASSWORD_LOGIN_DTO = new LoginDto(EMAIL, CORRECT_PASSWORD);
    private static final LoginDto INCORRECT_PASSWORD_LOGIN_DTO = new LoginDto(EMAIL, INCORRECT_PASSWORD);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthenticationController controller;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRefreshTokenRepository refreshTokenRepository;

    @MockitoBean
    private ISorareLoginService sorareLoginService;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setSorareId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        user.setEmail(EMAIL);
        user = userRepository.save(user);

        UserDto validUser = UserMapper.toDto(user);
        when(sorareLoginService.login(CORRECT_PASSWORD_LOGIN_DTO)).thenReturn(validUser);
    }

    @AfterEach
    void tearDown() {
        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("IIAC.1a - login should return 200, valid access token and HttpOnly Secure=false cookie")
    void login_shouldReturn200WithNonSecureCookie() throws Exception {
        assertLoginResponseWithSecureFlag(false);
    }

    @Test
    @DisplayName("IIAC.1b - login should return 200, valid access token and HttpOnly Secure=true cookie")
    void login_shouldReturn200WithSecureCookie() throws Exception {
        String cookieSecurePropertyName = "isCookieSecure";
        ReflectionTestUtils.setField(controller, cookieSecurePropertyName, true);
        try {
            assertLoginResponseWithSecureFlag(true);
        } finally {
            ReflectionTestUtils.setField(controller, cookieSecurePropertyName, false);
        }
    }

    @Test
    @DisplayName("IIAC.1c - login should return 400 when email is null")
    void login_shouldReturn400WhenEmailIsNull() throws Exception {
        LoginDto loginDto = new LoginDto(null, CORRECT_PASSWORD);

        mockMvc.perform(POST
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("IIAC.1d - login should return 400 when email is blank")
    void login_shouldReturn400WhenEmailIsBlank() throws Exception {
        LoginDto loginDto = new LoginDto("", CORRECT_PASSWORD);

        mockMvc.perform(POST
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("IIAC.1e - login should return 400 when password is null")
    void login_shouldReturn400WhenPasswordIsNull() throws Exception {
        LoginDto loginDto = new LoginDto(EMAIL, null);

        mockMvc.perform(POST
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("IIAC.1f - login should return 400 when password is blank")
    void login_shouldReturn400WhenPasswordIsBlank() throws Exception {
        LoginDto loginDto = new LoginDto(EMAIL, "");

        mockMvc.perform(POST
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("IIAC.1g - login should return 400 when request body is invalid JSON")
    void login_shouldReturn400WhenBodyIsInvalidJson() throws Exception {
        mockMvc.perform(POST
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("IIAC.1h - login should return 401 when credentials are invalid")
    void login_shouldReturn401WhenCredentialsAreInvalid() throws Exception {
        when(sorareLoginService.login(INCORRECT_PASSWORD_LOGIN_DTO))
                .thenThrow(new InvalidCredentialsException());

        mockMvc.perform(POST
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(INCORRECT_PASSWORD_LOGIN_DTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("IIAC.1i - login should return 500 when an unexpected exception occurs")
    void login_shouldReturn500WhenUnexpectedExceptionOccurs() throws Exception {
        when(sorareLoginService.login(CORRECT_PASSWORD_LOGIN_DTO))
                .thenThrow(new RuntimeException("Unexpected error"));

        performValidLogin().andExpect(status().isInternalServerError());
    }

    private void assertLoginResponseWithSecureFlag(boolean isCookieSecure) throws Exception {
        performValidLogin()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.expiresIn").value(EXPECTED_ACCESS_TOKEN_DURATION))
                .andExpect(result -> {
                    Cookie cookie = result.getResponse().getCookie(REFRESH_TOKEN_COOKIE_NAME);
                    assertNotNull(cookie);
                    assertNotNull(cookie.getValue());
                    assertFalse(cookie.getValue().isBlank());
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