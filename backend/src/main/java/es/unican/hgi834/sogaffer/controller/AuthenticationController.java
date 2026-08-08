package es.unican.hgi834.sogaffer.controller;

import es.unican.hgi834.sogaffer.model.dto.auth.AccessTokenDto;
import es.unican.hgi834.sogaffer.model.dto.auth.LoginDto;
import es.unican.hgi834.sogaffer.model.dto.auth.AccessRefreshTokenDto;
import es.unican.hgi834.sogaffer.service.auth.IAuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Value("${sogaffer.cookie.secure}")
    private boolean isCookieSecure;

    private final IAuthService authenticationService;

    public AuthenticationController(IAuthService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Handles user login by authenticating the provided credentials against Sorare
     * and generating access and refresh tokens.
     *
     * @param loginDto the data transfer object containing the email and password for authentication.
     * @param response the HTTP response to which the refresh token cookie will be added.
     * @return the access token details including the token string and its expiration time.
     */
    @PostMapping("/login")
    @ResponseStatus(code = HttpStatus.OK)
    public AccessTokenDto login(@Valid @RequestBody LoginDto loginDto, HttpServletResponse response) {
        AccessRefreshTokenDto responseDto = authenticationService.login(loginDto);
        addRefreshCookieToResponse(response, responseDto.refreshToken());
        return responseDto.accessToken();
    }

    /**
     * Adds the refresh token as an HTTP-only cookie to the provided HTTP response.
     *
     * @param response     the HTTP response to which the refresh token cookie will be added.
     * @param refreshToken the refresh token string to be included in the cookie.
     */
    private void addRefreshCookieToResponse(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
        // TODO: setPath(true) will be necessary after implementing the /refresh endpoint
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(isCookieSecure);
        response.addCookie(refreshTokenCookie);
    }
}
