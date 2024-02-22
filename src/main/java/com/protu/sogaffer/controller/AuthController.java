package com.protu.sogaffer.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.protu.sogaffer.dto.login.CredentialsRequestDto;
import com.protu.sogaffer.model.dto.LoginResponseDto;
import com.protu.sogaffer.service.AuthService;

@RestController
@RequestMapping("${auth}/")
@CrossOrigin("http://localhost:3000")
public class AuthController {
    
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("login")
    public LoginResponseDto login(@RequestBody CredentialsRequestDto credentials) {
        return authService.authenticate(credentials);
        // return salt;
    } // login
} // AuthController
