package com.protu.sogaffer.dto.login;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CredentialsRequestDto {
    private String email;
    private String password;
}
