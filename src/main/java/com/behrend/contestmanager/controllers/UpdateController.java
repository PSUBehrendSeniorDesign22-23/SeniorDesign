package com.behrend.contestmanager.controllers;

import java.sql.Date;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.behrend.contestmanager.models.*;
import com.behrend.contestmanager.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

@Controller
public class UpdateController {

    @Autowired
    PlayerService playerService = new PlayerServiceImpl();
    @Autowired
    TournamentService tournamentService = new TournamentServiceImpl();
    @Autowired
    MatchService matchService = new MatchServiceImpl();
    @Autowired
    RulesetService rulesetService = new RulesetServiceImpl();
    @Autowired
    RuleService ruleService = new RuleServiceImpl();
    
    @PatchMapping(value = "/player/update", params = {"playerId", "editssname", "rank"})
    @ResponseBody
    public ResponseEntity<String> updatePlayer(@RequestParam(name = "playerId") long playerId,
                                               @RequestParam(name = "editssname", required = false) String skipperName,
                                               @RequestParam(name = "rank", required = false) int rank) {
        Player player = new Player();
        player.setSkipperName(skipperName);
        player.setRank(rank);

        player = playerService.updatePlayer(player, playerId);

        if (player == null) {
            return ResponseEntity.badRequest().body("Player not found");
        }

        String playerAsJson;
        try {
            playerAsJson = JsonMapper(player);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating player");
        }

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(playerAsJson);
    }

    @PatchMapping(value = "/tournament/update", params = {"tournamentId", "name", "location", "date", "rulesetId", "playerIds"})
    @ResponseBody
    public ResponseEntity<String> updateTournament(@RequestParam(name = "tournamentId") long tournamentId,
                                                   @RequestParam(name = "name") String name,
                                                   @RequestParam(name = "location") String location,
                                                   @RequestParam(name = "date") Date date,
                                                   @RequestParam(name = "rulesetName") String rulesetName,
                                                   @RequestParam(name = "playerIds") ArrayList<Long> playerIds) {
        Tournament tournament = new Tournament();
        tournament.setName(name);
        tournament.setLocation(location);
        tournament.setDate(date);

        Ruleset ruleset = rulesetService.findRulesetsByName(rulesetName).get(0);

        if (ruleset == null) {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body("\"operation\": \"failure\", \"message\": \"Ruleset not found\"");
        }

        tournament.setRuleset(ruleset);

        ArrayList<Player> playerList = new ArrayList<>();
        for (long id : playerIds) {
            Player player = playerService.findPlayerById(id);
            if (player == null) {
                return ResponseEntity.badRequest().body("Player with ID " + id + " does not exist");
            }
            playerList.add(player);
        }

        tournament.setPlayers(playerList);

        tournament = tournamentService.updateTournament(tournament, tournamentId);

        if (tournament == null) {
            return ResponseEntity.badRequest().body("Tournament not found");
        }
                                                
        String tournamentAsJson;
        try {
            tournamentAsJson = JsonMapper(tournament);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating tournament");
        }

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(tournamentAsJson);
    }

    @PatchMapping(value = "/rule/update", params = {"ruleId", "ruleName", "attribute"})
    @ResponseBody
    public ResponseEntity<String> updateRule(@RequestParam(name = "ruleId") long ruleId,
                                             @RequestParam(name = "ruleName") String ruleName,
                                             @RequestParam(name = "attribute") String attribute) {
        Rule rule = new Rule();
        rule.setName(ruleName);
        rule.setAttribute(attribute);

        rule = ruleService.updateRule(rule, ruleId);

        if (rule == null) {
            return ResponseEntity.badRequest().body("Rule not found");
        }

        String ruleAsJson;
        try {
            ruleAsJson = JsonMapper(rule);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating rule");
        }

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(ruleAsJson);
    }

    @PatchMapping(value = "/match/update", params = {"matchId", "defenderId", "challengerId", "tournamentId", "defenderScore", "challengerScore"})
    @ResponseBody
    public ResponseEntity<String> updateMatch(@RequestParam(name = "matchId") long matchId,
                                              @RequestParam(name = "defenderId") long defenderId,
                                              @RequestParam(name = "challengerId") long challengerId,
                                              @RequestParam(name = "tournamentId") long tournamentId,
                                              @RequestParam(name = "defenderScore") int defenderScore,
                                              @RequestParam(name = "challengerScore") int challengerScore) {
        Match match = new Match();

        Player defender = playerService.findPlayerById(defenderId);

        if (defender == null) {
            return ResponseEntity.badRequest().body("Player one not found");
        }

        Player challenger = playerService.findPlayerById(challengerId);

        if (challenger == null) {
            return ResponseEntity.badRequest().body("Player two not found");
        }

        Tournament tournament = tournamentService.findTournamentById(tournamentId);

        if (tournament == null) {
            return ResponseEntity.badRequest().body("Tournament not found");
        }

        match.setDefender(defender);
        match.setChallenger(challenger);
        match.setTournament(tournament);
        match.setDefenderScore(defenderScore);
        match.setChallengerScore(challengerScore);

        match = matchService.updateMatch(match, matchId);

        if (match == null) {
            return ResponseEntity.badRequest().body("Match not found");
        }

        String matchAsJson;
        try {
            matchAsJson = JsonMapper(match);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating match");
        }

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(matchAsJson);
    }

    private <T> String JsonMapper(T object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}

