package com.protu.sogaffer.dto.players;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerListDto {
    private List<PlayerDto> nodes;
} // PlayerListDto