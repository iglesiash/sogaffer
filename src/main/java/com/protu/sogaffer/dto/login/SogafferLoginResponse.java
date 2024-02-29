package com.protu.sogaffer.dto.login;

import lombok.Data;

@Data
public class SogafferLoginResponse {
    private SignInDto loginInformation;
    private String accessToken;
}
