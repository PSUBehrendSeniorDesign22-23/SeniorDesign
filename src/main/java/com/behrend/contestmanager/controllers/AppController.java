package com.behrend.contestmanager.controllers;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.behrend.contestmanager.models.*;
import com.behrend.contestmanager.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class AppController {
    
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

    @GetMapping(value = "/")
    public String index() {
        return "DeveloperTools";
    }

    @GetMapping(value = "/players/search", params = {"searchType", "searchFilter"})
    @ResponseBody
    public List<Player> searchPlayer(@RequestParam(name = "searchType") String type,
                                     @RequestParam(name = "searchFilter") String filter) {
        ArrayList<Player> players = new ArrayList<Player>();
        
        System.out.printf("%s  |  %s", type, filter);

        if (type.equals("pname"))
        {
            System.out.println("Test");
            players.addAll(playerService.findPlayerByFirstName(filter));
        }
        if (type.equals("pssname"))
        {
            players.addAll(playerService.findPlayersBySkipperName(filter));
        }

        return players;
    }

    @GetMapping(value = "/tournament/search", params = {"searchType", "searchFilter"})
    @ResponseBody
    public List<Tournament> searchTournament(@RequestParam(name = "searchType") String type, 
                                             @RequestParam(name = "searchFilter") String filter) {
        ArrayList<Tournament> tournaments = new ArrayList<Tournament>();
        
        if (type.equals("tname"))
        {
            tournaments.addAll(tournamentService.findTournamentsByName(filter));
        }
        if (type.equals("tlocation"))
        {
            tournaments.addAll(tournamentService.findTournamentsByLocation(filter));
        }
        if (type.equals("tdate"))
        {
            try
            {
                Date inputDate = Date.valueOf(filter);
                tournaments.addAll(tournamentService.findTournamentsByDate(inputDate));
            }
            catch (Exception e)
            {
                return null;
            }
        }

        return tournaments;
    }

    @GetMapping(value = "/ruleset/search", params = {"searchType", "searchFilter"})
    @ResponseBody
    public List<Ruleset> searchRuleset(@RequestParam(name = "searchType") String type,
                                       @RequestParam(name = "searchFilter") String filter) {
        ArrayList<Ruleset> rulesets = new ArrayList<Ruleset>();
        
        if (type.equals("rname"))
        {
            rulesets.addAll(rulesetService.findRulesetsByName(filter));
        }
        if (type.equals("rorigin"))
        {
            rulesets.addAll(rulesetService.findRulesetsByOrigin(filter));
        }

        return rulesets;
    }
    
    @PostMapping(value = "/players/create", params = {"addfname","addlname","addssname","addeadd","addpnum"})
    @ResponseBody
    public ResponseEntity<String> createPlayer(@RequestParam(name = "addfname") String firstName, 
                                                @RequestParam(name = "addlname") String lastName, 
                                                @RequestParam(name = "addssname") String skipperName, 
                                                @RequestParam(name = "addeadd") String email, 
                                                @RequestParam(name = "addpnum") String phoneNum){
        
        Player player = new Player();

        if(firstName != null){
            player.setFirstName(firstName);
        }
        if(lastName != null){
            player.setLastName(lastName);
        }
        if(skipperName != null){
            player.setSkipperName(skipperName);
        }
        if(email != null){
            player.setEmail(email);
        }
        if(phoneNum != null){
            player.setPhoneNum(phoneNum);
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

    @PostMapping(value = "/match/create", params = {"playerOneId", "playerTwoId", "tournamentId", "playerOneScore", "playerTwoScore"})
    @ResponseBody
    public ResponseEntity<String> createMatch(@RequestParam(name = "playerOneId") long playerOneId,
                                              @RequestParam(name = "playerTwoId") long playerTwoId,
                                              @RequestParam(name = "tournamentId") long tournamentId,
                                              @RequestParam(name = "playerOneScore", required = false) int playerOneScore,
                                              @RequestParam(name = "playerTwoScore", required = false) int playerTwoScore) {

        Match match = new Match();
        
        Player playerOne = playerService.findPlayerById(playerOneId);
        Player playerTwo = playerService.findPlayerById(playerTwoId);
        Tournament tournament = tournamentService.findTournamentById(tournamentId);

        if (playerOne == null) {
            return new ResponseEntity<>("Player with ID: " + playerOneId + " does not exist", HttpStatus.BAD_REQUEST);
        }
        if (playerTwo == null) {
            return new ResponseEntity<>("Player with ID: " + playerTwoId + " does not exist", HttpStatus.BAD_REQUEST);
        }
        if (tournament == null) {
            return new ResponseEntity<>("Tournament with ID: " + tournamentId + " does not exist", HttpStatus.BAD_REQUEST);
        }

        match.setPlayerOne(playerOne);
        match.setPlayerTwo(playerTwo);
        match.setTournament(tournament);

        match.setPlayerOneScore(playerOneScore);
        match.setPlayerTwoScore(playerTwoScore);

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
