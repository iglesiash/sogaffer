package com.protu.sogaffer.model.dto;

import lombok.Data;

@Data
public class CurrentUser {
    private String slug;
    private TokenDto jwtToken;
} // CurrentUser
