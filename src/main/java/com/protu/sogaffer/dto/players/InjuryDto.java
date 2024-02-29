package com.protu.sogaffer.dto.players;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InjuryDto {
    private Boolean active;
    private Date startDate;
    private Date expectedEndDate;
}
