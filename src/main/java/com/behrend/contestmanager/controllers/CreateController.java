package com.behrend.contestmanager.controllers;

import java.sql.Date;
import java.util.Map;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.behrend.contestmanager.models.*;
import com.behrend.contestmanager.repository.UserRepository;
import com.behrend.contestmanager.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class CreateController {
    
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
    @Autowired
    UserRepository userRepository;

    @PostMapping(value = "/player/create", params = {"addPlayerEmail", "addssname"})
    @ResponseBody
    public ResponseEntity<String> createPlayer(@RequestParam(name = "addPlayerEmail") String userEmail, 
                                               @RequestParam(name = "addssname") String skipperName){
        
        Player player = new Player();
        User user;

        if(userEmail.equals("") || skipperName.equals("")){
            return ResponseEntity.badRequest().body("{\"operation\": \"failure\",\"message\": \"Email and skipper name cannot be empty\"}");
        }
        else {
            user = userRepository.findByEmail(userEmail);
            if (user == null) {
                return ResponseEntity.badRequest().body("{\"operation\": \"failure\",\"message\": \"User with email " + userEmail + " does not exist\"}");
            }
            Player existingPlayer = playerService.findPlayerByUserId(user.getUserId());
            if (existingPlayer != null) {
                return ResponseEntity.badRequest().body("{\"operation\": \"failure\",\"message\": \"Player with email " + userEmail + " already exists\"}");
            }
        }

        player.setUser(user);
        player.setSkipperName(skipperName);
        
        playerService.savePlayer(player);

        return ResponseEntity.ok("{\"operation\": \"success\"}");
    }

    @PostMapping(value = "/tournament/create", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<String> createTournament(@RequestBody Map<String, String> inputMap) {

        Tournament tournament = new Tournament();

        String name = inputMap.get("tournamentName");
        String location = inputMap.get("tournamentLocation");
        String rulesetName = inputMap.get("tournamentRulesetName");
        String dateAsString = inputMap.get("tournamentDate");

        if (name == null || location == null || rulesetName == null || dateAsString == null) {
            return ResponseEntity.internalServerError().body("{\"operation\":\"failure\",\"message\":\"Something went wrong\"}");
        }

        if (name.equals("") || location.equals("") || rulesetName.equals("") || dateAsString.equals("")) {
            return ResponseEntity.badRequest().body("{\"operation\":\"failure\",\"message\":\"Name, location, and ruleset are required\"}");
        }

        Date date;
        try {
            date = Date.valueOf(dateAsString);
        }        
        catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"operation\":\"failure\",\"message\":\"Date format is invalid\"}");
        }

        tournament.setName(name);
        tournament.setLocation(location);
        tournament.setDate(date);

        Ruleset ruleset = rulesetService.findRulesetsByName(rulesetName).get(0);
        if(ruleset == null){
            return ResponseEntity.badRequest().body("{\"operation\":\"failure\",\"message\":\"Ruleset not found\"}");
        }
        else{
            tournament.setRuleset(ruleset);
        }

        inputMap.remove("tournamentName");
        inputMap.remove("tournamentLocation");
        inputMap.remove("tournamentRulesetName");
        inputMap.remove("tournamentDate");

        ArrayList<Player> players = new ArrayList<>();
        for (String playerEntry : inputMap.keySet()) {
            Player player;
            String playerId = inputMap.get(playerEntry);

            if (playerId.equals("") || playerId.equals("none")) {
                return ResponseEntity.badRequest().body("{\"operation:\"\"failure\",\"message\":\"All players must be selected\"}");
            }

            player = playerService.findPlayerById(Long.parseLong(playerId));

            players.add(player);
        }

        tournament.setPlayers(players);
        tournamentService.saveTournament(tournament);

        return ResponseEntity.ok("{\"operation\":\"success\"}");
    }

    @PostMapping(value = "/ruleset/create", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<String> createRuleset(@RequestBody Map<String, String> inputMap){

        Ruleset ruleset = new Ruleset();

        String rulesetName = inputMap.get("rulesetName");
        String rulesetOrigin = inputMap.get("rulesetOrigin");
        ArrayList<Rule> rules = new ArrayList<>();

        if(rulesetName.equals("") || rulesetOrigin.equals("")) {
            return ResponseEntity.badRequest().body("{\"operation:\"\"failure\",\"message\":\"Ruleset name and origin are required\"}");
        }

        inputMap.remove("rulesetName");
        inputMap.remove("rulesetOrigin");

        // For each rule
        for (String ruleName : inputMap.keySet()) {
            Rule rule;
            String attribute = inputMap.get(ruleName);

            // Check for empty value
            if (attribute.equals("")) {
                return ResponseEntity.badRequest().body("{\"operation:\"\"failure\",\"message\":\"Attribute with key " + ruleName + " is empty\"}");
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

        ruleset.setName(rulesetName);
        ruleset.setOrigin(rulesetOrigin);
        ruleset.setRules(rules);
        rulesetService.saveRuleset(ruleset);

        return ResponseEntity.ok("{\"operation\": \"success\"}");
    }

    @PostMapping(value = "/match/create", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<String> createMatch(@RequestBody Map<String, String> inputMap) {

        Match match = new Match();

        long defenderId;
        long challengerId;
        long tournamentId;

        int defenderScore;
        int challengerScore;

        try {
            defenderId = Long.parseLong(inputMap.get("defenderId"));
            challengerId = Long.parseLong(inputMap.get("challengerId"));
            tournamentId = Long.parseLong(inputMap.get("tournamentId"));
            defenderScore = Integer.parseInt(inputMap.get("defenderScore"));
            challengerScore = Integer.parseInt(inputMap.get("challengerScore"));
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body("{\"operation:\"\"failure\",\"message\":\"Values must be numerical\"}");
        }
        
        if (defenderId == challengerId) {
            return ResponseEntity.badRequest().body("{\"operation:\"\"failure\",\"message\":\"Defender and Challenger cannot be the same\"}")
        }

        Player defender = playerService.findPlayerById(defenderId);
        Player challenger = playerService.findPlayerById(challengerId);
        Tournament tournament = tournamentService.findTournamentById(tournamentId);

        if (defender == null) {
            return ResponseEntity.badRequest().body("{\"operation:\"\"failure\",\"message\":\"Player with ID: " + defenderId + " does not exist\"}");
        }
        if (challenger == null) {
            return ResponseEntity.badRequest().body("{\"operation:\"\"failure\",\"message\":\"Player with ID: " + challengerId + " does not exist\"}");
        }
        if (tournament == null) {
            return ResponseEntity.badRequest().body("{\"operation:\"\"failure\",\"message\":\"Tournament with ID: " + tournamentId + " does not exist\"}");
        }

        match.setDefender(defender);
        match.setChallenger(challenger);
        match.setTournament(tournament);

        match.setDefenderScore(defenderScore);
        match.setChallengerScore(challengerScore);

        matchService.saveMatch(match);

        ObjectMapper objectMapper = new ObjectMapper();
        String matchAsJson;
        try {
            matchAsJson = objectMapper.writeValueAsString(match);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating match, please try again");
        }

        matchService.saveMatch(match);
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(matchAsJson);
    }

    @PostMapping(value = "/rule/create", params = {"ruleName", "ruleAttribute"})
    @ResponseBody
    public ResponseEntity<String> createRule(@RequestParam(name = "ruleName") String ruleName,
                                             @RequestParam(name = "ruleAttribute") String ruleAttribute) {
        Rule rule = new Rule();

        if (ruleName.equals("") || ruleAttribute.equals("")) {
            return ResponseEntity.badRequest().body("{\"operation:\"\"failure\",\"message\":\"Rule key and attribute are required\"}");
        }
        
        rule.setName(ruleName);
        rule.setAttribute(ruleAttribute);

        ObjectMapper objectMapper = new ObjectMapper();
        String ruleAsJson;
        try {
            ruleAsJson = objectMapper.writeValueAsString(rule);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error creating rule, please try again");
        }

        ruleService.saveRule(rule);
        
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(ruleAsJson);
    }
}
