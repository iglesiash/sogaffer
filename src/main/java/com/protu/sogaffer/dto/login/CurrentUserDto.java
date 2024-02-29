package com.protu.sogaffer.dto.login;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentUserDto {
    private String slug;
    private UserProfileDto profile;
    private TokenDto jwtToken;
} // CurrentUser