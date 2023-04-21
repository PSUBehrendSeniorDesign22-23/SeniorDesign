package com.behrend.contestmanager.controllers;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    
    @PatchMapping(value = "/player/update", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<String> updatePlayer(@RequestBody Map<String, String> inputMap) {
        Player player = new Player();

        long playerId;
        try {
            playerId = Long.parseLong(inputMap.get("playerId"));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"operation\":\"failure\",\"message\":\"Unable to parse player ID\"}");
        }

        String skipperName = inputMap.get("skipperName");
        String rankString = inputMap.get("rank");

        Integer rank = null;
        if (!rankString.equals("")) {
            try {
                rank = Integer.parseInt(rankString);
            }
            catch (Exception e) {
                return ResponseEntity.badRequest().body("{\"operation\":\"failure\",\"message\":\"Rank must be a number\"}");
            }
        }

        player.setSkipperName(skipperName);
        player.setRank(rank);

        player = playerService.updatePlayer(player, playerId);

        if (player == null) {
            return ResponseEntity.badRequest().body("{\"operation\":\"failure\",\"message\":\"Player not found\"}");
        }

        return ResponseEntity.ok().body("{\"operation\":\"success\",\"message\":\"Player updated\"}");
    }

    @PatchMapping(value = "/tournament/update", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<String> updateTournament(@RequestBody Map<String, String> inputMap) {
        Tournament tournament = new Tournament();

        long tournamentId;
        
        try {
            tournamentId = Long.parseLong(inputMap.get("tournamentId"));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"operation\":\"failure\",\"message\":\"Unable to parse tournament ID\"}");
        }

        
        String tournamentName = inputMap.get("tournamentName");
        String tournamentLocation = inputMap.get("tournamentLocation");
        
        tournament.setName(tournamentName);
        tournament.setLocation(tournamentLocation);
        
        String dateString = inputMap.get("tournamentDate");
        if (!dateString.equals("")) {
            Date tournamentDate;
            try {
                tournamentDate = Date.valueOf(dateString);
                tournament.setDate(tournamentDate);
            }
            catch (Exception e) {
                return ResponseEntity.badRequest().body("{\"operation\":\"failure\",\"message\":\"Unable to parse date\"}");
            }
        }
        
        String rulesetIdString = inputMap.get("tournamentRulesetId");

        Ruleset ruleset;
        long rulesetId;
        if (!rulesetIdString.equals("")) {
            try {
                rulesetId = Long.parseLong(inputMap.get("tournamentRulesetId"));
            }
            catch (Exception e) {
                return ResponseEntity.badRequest().body("{\"operation\":\"failure\",\"message\":\"Unable to parse ruleset Id\"}");
            }

            ruleset = rulesetService.findRulesetById(rulesetId);
            if (ruleset == null) {
                return ResponseEntity.badRequest().body("{\"operation\":\"failure\",\"message\":\"Ruleset with ID " + rulesetId + " does not exist\"}");
            }
            tournament.setRuleset(ruleset);
        }

        inputMap.remove("tournamentId");
        inputMap.remove("tournamentName");
        inputMap.remove("tournamentLocation");
        inputMap.remove("tournamentDate");
        inputMap.remove("tournamentRulesetId");


        ArrayList<Long> playerIds = new ArrayList<>();

        for (String key : inputMap.keySet()) {
            try {
                playerIds.add(Long.parseLong(inputMap.get(key)));
            }
            catch (Exception e) {
                return ResponseEntity.badRequest().body("{\"operation\":\"failure\",\"message\":\"Error parsing player IDs\"}");
            }
        }

        ArrayList<Player> playerList = new ArrayList<>();
        for (long id : playerIds) {
            Player player = playerService.findPlayerById(id);
            if (player == null) {
                return ResponseEntity.badRequest().body("{\"operation\":\"failure\",\"message\":\"Player with ID " + id + " does not exist\"}");
            }
            playerList.add(player);
        }

        tournament.setPlayers(playerList);

        tournament = tournamentService.updateTournament(tournament, tournamentId);

        if (tournament == null) {
            return ResponseEntity.badRequest().body("{\"operation\":\"failure\",\"message\":\"Tournament not found\"}");
        }
                                                
        return ResponseEntity.ok().body("{\"operation\":\"success\",\"message\":\"Tournament updated\"}");
    }

    @PatchMapping(value = "/ruleset/update", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<String> updateRuleset(@RequestBody Map<String, String> inputMap) {

        Ruleset ruleset = new Ruleset();

        long rulesetId;
        try {
            rulesetId = Long.parseLong(inputMap.get("rulesetId"));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"operation\":\"failure\",\"message\":\"Unable to parse ruleset Id\"}");
        }

        String rulesetName = inputMap.get("rulesetName");
        String rulesetOrigin = inputMap.get("rulesetOrigin");

        ruleset.setName(rulesetName);
        ruleset.setOrigin(rulesetOrigin);

        inputMap.remove("rulesetId");
        inputMap.remove("rulesetName");
        inputMap.remove("rulesetOrigin");

        ArrayList<Rule> rules = new ArrayList<>();
        for (String ruleName : inputMap.keySet()) {
            Rule rule;
            String attribute = inputMap.get(ruleName);

            // Check for empty value
            if (attribute.equals("")) {
                return ResponseEntity.badRequest().body("{\"operation\":\"failure\",\"message\":\"Attribute with key " + ruleName + " is empty\"}");
            }

            // Check if the rule already exists
            rule = ruleService.findRuleByNameAndAttribute(ruleName, attribute);

            // If it does not exist, create it
            if (rule == null) {
                rule = new Rule();
                rule.setName(ruleName);
                rule.setAttribute(attribute);
                rule = ruleService.saveRule(rule);
            }
            // Add rule to list of rules
            rules.add(rule);
        }

        ruleset.setRules(rules);
        
        ruleset = rulesetService.updateRuleset(ruleset, rulesetId);

        return ResponseEntity.ok().body("{\"operation\":\"success\",\"message\":\"Ruleset updated\"}");
    }

    @PatchMapping(value = "/rule/update")
    @ResponseBody
    public ResponseEntity<String> updateRule(@RequestBody Map<String, String> inputMap) {
        Rule rule = new Rule();

        long ruleId;

        try {
            ruleId = Long.parseLong(inputMap.get("ruleId"));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Rule ID must be a number");
        }
        

        String ruleName = inputMap.get("ruleName");
        String attribute = inputMap.get("attribute");

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

    @PatchMapping(value = "/match/update", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<String> updateMatch(@RequestBody Map<String, String> inputMap) {

        Match match = new Match();

        long matchId;
        long defenderId;
        long challengerId;
        long tournamentId;

        int defenderScore;
        int challengerScore;

        try {
            matchId = Long.parseLong(inputMap.get("matchId"));
            defenderId = Long.parseLong(inputMap.get("defenderId"));
            challengerId = Long.parseLong(inputMap.get("challengerId"));
            tournamentId = Long.parseLong(inputMap.get("tournamentId"));
            defenderScore = Integer.parseInt(inputMap.get("defenderScore"));
            challengerScore = Integer.parseInt(inputMap.get("challengerScore"));
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body("{\"operation\":\"failure\",\"message\":\"Values must be numerical\"}");
        }


        Player defender = playerService.findPlayerById(defenderId);

        if (defender == null) {
            return ResponseEntity.badRequest().body("Defender not found");
        }

        Player challenger = playerService.findPlayerById(challengerId);

        if (challenger == null) {
            return ResponseEntity.badRequest().body("Challenger not found");
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

