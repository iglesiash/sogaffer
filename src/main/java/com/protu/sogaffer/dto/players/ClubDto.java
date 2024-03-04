package com.protu.sogaffer.dto.players;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.protu.sogaffer.model.dto.LeagueDto;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClubDto {
    private String code;
    private String name;
    private String pictureUrl;
    private LeagueDto domesticLeague;
    private List<UpcomingGameDto> upcomingGames;
}
