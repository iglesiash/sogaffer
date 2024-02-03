package com.protu.sogaffer.model.dto;

import java.util.List;

import lombok.Data;

@Data
public class SignInDto {
    private CurrentUser currentUser;
    private Object otpSessionChallenge;
    private List<Object> errors;
}