package es.unican.hgi834.sogaffer.controller;

import es.unican.hgi834.sogaffer.model.dto.auth.AccessTokenDto;
import es.unican.hgi834.sogaffer.model.dto.auth.LoginDto;
import es.unican.hgi834.sogaffer.service.auth.IAuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final IAuthService authenticationService;

    public AuthenticationController(IAuthService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    @ResponseStatus(code = HttpStatus.OK)
    public AccessTokenDto login(@Valid @RequestBody LoginDto loginDto) {
        return authenticationService.login(loginDto);
    }
}
