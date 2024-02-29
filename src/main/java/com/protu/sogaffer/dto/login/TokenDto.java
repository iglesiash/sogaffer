package com.protu.sogaffer.dto.login;

import lombok.Data;

@Data
public class TokenDto {
    private String token;
    private String expiredAt;
} // TokenDto