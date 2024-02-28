package com.protu.sogaffer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageInfoDto {
    private String endCursor;
    private boolean hasNextPage;
}
