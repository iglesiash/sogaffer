package com.protu.sogaffer.model.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserErrorDto {
    private Integer code;
    private String message;
    private List<String> path;
}
