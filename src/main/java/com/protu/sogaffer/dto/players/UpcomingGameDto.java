package com.protu.sogaffer.dto.players;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpcomingGameDto {
    private Date date;
    private String venue;
    private ClubDto homeTeam;
    private ClubDto awayTeam;
}
