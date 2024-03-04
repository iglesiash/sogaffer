package com.protu.sogaffer.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LeagueDto {
    @JsonAlias("displayName")
    private String label;

    @JsonAlias("slug")
    private String value;

    private String logoUrl;
}
