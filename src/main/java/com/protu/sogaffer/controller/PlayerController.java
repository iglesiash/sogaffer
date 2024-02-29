package com.protu.sogaffer.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.protu.sogaffer.dto.players.PlayerDto;
import com.protu.sogaffer.service.PlayerService;

@RestController
@RequestMapping("${api}/players")
@CrossOrigin("http://localhost:3000")
public class PlayerController {

    private PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public List<PlayerDto> getPlayers(@RequestParam("league") String leagueSlug)
            throws JsonProcessingException, InterruptedException, ExecutionException {
        return playerService.getPlayersByLeague(leagueSlug);
    }

    @GetMapping("/{player-slug}")
    public ResponseEntity<PlayerDto> getPlayer(@PathVariable("player-slug") String playerSlug)
            throws JsonProcessingException, InterruptedException, ExecutionException {
        ResponseEntity<PlayerDto> response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        PlayerDto playerDto = playerService.getPlayer(playerSlug);
        if (playerDto != null) {
            response = ResponseEntity.ok(playerDto);
        }
        return response;
    }

}
