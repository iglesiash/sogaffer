package com.protu.sogaffer.model.dto;

import lombok.Data;

@Data
public class TokenDto {
    private String token;
    private String expiredAt;
} // TokenDto