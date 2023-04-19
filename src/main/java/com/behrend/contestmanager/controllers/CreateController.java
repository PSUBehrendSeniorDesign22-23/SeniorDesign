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
        }

        player.setUser(user);
        player.setSkipperName(skipperName);
        
        playerService.savePlayer(player);

        return ResponseEntity.ok("{\"operation\": \"success\"}");
    }

    @PostMapping(value = "/tournament/create", params = {"addtname","addtloc","addtdate","addtrule"})
    @ResponseBody
    public ResponseEntity<String> createTournament(@RequestParam(name = "addtname") String name,
                                                   @RequestParam(name = "addtloc") String location,
                                                   @RequestParam(name = "addtdate") Date date, 
                                                   @RequestParam(name = "addtrule") String ruleSetName) {

        Tournament tournament = new Tournament();

        if(name != null){
            tournament.setName(name);
        }
        if(location != null){
            tournament.setLocation(location);
        }
        if(date != null){
            tournament.setDate(date);
        }
        if(ruleSetName != null){
            Ruleset ruleset = rulesetService.findRulesetsByName(ruleSetName).get(0);
            if( ruleset == null){
                return ResponseEntity.status(HttpStatus.valueOf(400)).body("{\"operation\":\"failure\",\"message\":\"Ruleset not found\"}");
            }else{
                tournament.setRuleset(ruleset);
            }
        }
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

        if(rulesetName == null || rulesetOrigin == null) {
            return ResponseEntity.badRequest().body("{\"operation:\"\"failure\",\"message\":\"Ruleset name and origin are required\"}");
        }

        inputMap.remove("rulesetName");
        inputMap.remove("rulesetOrigin");

        // For each rule
        for (String ruleName : inputMap.keySet()) {
            Rule rule;
            String attribute = inputMap.get(ruleName);

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

    @PostMapping(value = "/match/create", params = {"defenderId", "challengerId", "tournamentId", "defenderScore", "challengerScore"})
    @ResponseBody
    public ResponseEntity<String> createMatch(@RequestParam(name = "defenderId") long defenderId,
                                              @RequestParam(name = "challengerId") long challengerId,
                                              @RequestParam(name = "tournamentId") long tournamentId,
                                              @RequestParam(name = "defenderScore", required = false) int defenderScore,
                                              @RequestParam(name = "challengerScore", required = false) int challengerScore) {

        Match match = new Match();
        
        Player defender = playerService.findPlayerById(defenderId);
        Player challenger = playerService.findPlayerById(challengerId);
        Tournament tournament = tournamentService.findTournamentById(tournamentId);

        if (defender == null) {
            return new ResponseEntity<>("Player with ID: " + defenderId + " does not exist", HttpStatus.BAD_REQUEST);
        }
        if (challenger == null) {
            return new ResponseEntity<>("Player with ID: " + challengerId + " does not exist", HttpStatus.BAD_REQUEST);
        }
        if (tournament == null) {
            return new ResponseEntity<>("Tournament with ID: " + tournamentId + " does not exist", HttpStatus.BAD_REQUEST);
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

        if (ruleName == null || ruleAttribute == null) {
            return new ResponseEntity<>("Rule name and rule attribute are required", HttpStatus.BAD_REQUEST);
        }
        
        rule.setName(ruleName);
        rule.setAttribute(ruleAttribute);

        ObjectMapper objectMapper = new ObjectMapper();
        String ruleAsJson;
        try {
            ruleAsJson = objectMapper.writeValueAsString(rule);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating rule, please try again");
        }

        ruleService.saveRule(rule);
        
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(ruleAsJson);
    }
}
