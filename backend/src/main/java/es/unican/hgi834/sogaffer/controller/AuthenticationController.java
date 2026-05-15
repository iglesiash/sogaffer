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

    @PostMapping("/login")
    @ResponseStatus(code = HttpStatus.OK)
    public AccessTokenDto login(@Valid @RequestBody LoginDto loginDto, HttpServletResponse response) {
        AccessRefreshTokenDto responseDto = authenticationService.login(loginDto);
        addRefreshCookieToResponse(response, responseDto.refreshToken());
        return responseDto.accessToken();
    }

    private void addRefreshCookieToResponse(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
        // TODO: setPath(true) will be necessary after implementing the /refresh endpoint
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(isCookieSecure);
        response.addCookie(refreshTokenCookie);
    }
}
