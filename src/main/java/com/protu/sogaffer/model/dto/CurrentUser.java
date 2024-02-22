package com.protu.sogaffer.model.dto;

import com.protu.sogaffer.dto.login.UserProfileDto;

import lombok.Data;

@Data
public class CurrentUser {
    private String slug;
    private UserProfileDto profile;
    private TokenDto jwtToken;
} // CurrentUser