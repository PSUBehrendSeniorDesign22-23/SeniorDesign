package com.behrend.contestmanager.controllers;

import java.sql.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.behrend.contestmanager.models.*;
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

     @PostMapping(value = "/player/create", params = {"addfname","addlname","addssname","addeadd","addpnum"})
    @ResponseBody
    public ResponseEntity<String> createPlayer(@RequestParam(name = "userId") long userId, 
                                               @RequestParam(name = "skipperName") String skipperName){
        
        Player player = new Player();

        if(skipperName != null){
            player.setSkipperName(skipperName);
        }
        
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
                return ResponseEntity.status(HttpStatus.valueOf(400)).body("{\"operation\": \"failure\", \"message\": \"Ruleset not found\"}");
            }else{
                tournament.setRuleset(ruleset);
            }
        }
        tournamentService.saveTournament(tournament);

        return ResponseEntity.ok("{\"operation\": \"success\"}");
    }

    @PostMapping(value = "/ruleset/create", params = {"addrname","addrorigin"})
    @ResponseBody
    public ResponseEntity<String> createRuleset(@RequestParam(name = "addrname") String rulesetName, @RequestParam(name = "addrorigin") String origin){

        Ruleset ruleset = new Ruleset();

        if(rulesetName != null){
            ruleset.setName(rulesetName);
        }
        if(origin != null){
            ruleset.setOrigin(origin);
        }
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
        
        ObjectMapper objectMapper = new ObjectMapper();
        String ruleAsJson;
        try {
            ruleAsJson = objectMapper.writeValueAsString(rule);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating match, please try again");
        }

        ruleService.saveRule(rule);
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(ruleAsJson);
    }
}
