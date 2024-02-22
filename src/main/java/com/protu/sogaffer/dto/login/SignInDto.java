package com.protu.sogaffer.dto.login;

import java.util.List;

import com.protu.sogaffer.model.dto.CurrentUser;

import lombok.Data;

@Data
public class SignInDto {
    private CurrentUser currentUser;
    private Object otpSessionChallenge;
    private String tcuToken;
    private List<Object> errors;
}