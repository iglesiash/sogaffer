package com.protu.sogaffer.dto.players;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CountryDto {
    @JsonAlias("code")
    private String threeLetterCode;

    private String flagUrl;
}
