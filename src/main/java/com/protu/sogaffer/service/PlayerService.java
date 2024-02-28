package com.protu.sogaffer.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.protu.sogaffer.dto.players.PlayerDto;
import com.protu.sogaffer.dto.players.PlayerListDto;
import com.protu.sogaffer.dto.players.TeamDto;
import com.protu.sogaffer.dto.players.TeamsDto;
import com.protu.sogaffer.utils.GraphqlUtils;
import com.protu.sogaffer.utils.RequestUtils;

@Service
public class PlayerService {
    private RequestUtils requestUtils;

    public PlayerService(RequestUtils requestUtils) {
        this.requestUtils = requestUtils;
    }

    public List<PlayerDto> getPlayers() throws JsonProcessingException, InterruptedException, ExecutionException {
        List<String> leagues = Arrays.asList(
                new String[] { "laliga-es", "premier-league-gb-eng", "bundesliga-de", "ligue-1-fr", "serie-a-it" });
        List<TeamDto> teams = new ArrayList<>();
        List<PlayerDto> players = new ArrayList<>();

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (String leagueSlug : leagues) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    String query = GraphqlUtils.loadQuery("getTeamsFromTopFiveLeagues");
                    Map<String, Object> variables = new HashMap<>();
                    variables.put("leagueSlug", leagueSlug);
                    // TODO: send token from front-end
                    ResponseEntity<String> response = requestUtils.callGraphQLService(query, variables, "");
                    JsonNode signInNode = requestUtils.getObjectMapper().readTree(response.getBody()).path("data")
                            .get("football")
                            .get("competition").get("clubs");

                    TeamsDto teamsDto = requestUtils.getObjectMapper().treeToValue(signInNode, TeamsDto.class);

                    teams.addAll(teamsDto.getNodes());
                } catch (Exception e) {
                    e.printStackTrace();
                } // try-catch
            }, executor);
            futures.add(future);
        } // for

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allFutures.join(); // Wait for all futures to complete

        for (TeamDto team : teams) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {

                    String query = GraphqlUtils.loadQuery("getPlayersQuery");
                    Map<String, Object> variables = new HashMap<>();
                    variables.put("teamSlug", team.getSlug());

                    // TODO: send token from front-end
                    ResponseEntity<String> response = requestUtils.callGraphQLService(query, variables, "");
                    JsonNode signInNode = requestUtils.getObjectMapper().readTree(response.getBody()).path("data")
                            .get("football")
                            .get("club").get("activePlayers");

                    PlayerListDto playerListDto = requestUtils.getObjectMapper().treeToValue(signInNode,
                            PlayerListDto.class);

                    players.addAll(playerListDto.getNodes());
                } catch (Exception e) {

                }
            }, executor);
            futures.add(future);
        }

        CompletableFuture<Void> allFutures2 = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allFutures2.join(); // Wait for all futures to complete

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            // Handle exception
        }

        return players;
    }

} // PlayerService
