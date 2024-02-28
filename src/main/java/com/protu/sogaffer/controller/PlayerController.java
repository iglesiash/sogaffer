package com.protu.sogaffer.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.protu.sogaffer.dto.players.PlayerDto;
import com.protu.sogaffer.service.PlayerService;

@RestController
@RequestMapping("${api}/players")
// @CrossOrigin("http://localhost:3000")
public class PlayerController {

    private PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public List<PlayerDto> getPlayers()
            throws JsonProcessingException, InterruptedException, ExecutionException {
        return playerService.getPlayers();
    }

}
