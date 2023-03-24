package com.behrend.contestmanager.controllers;

import org.springframework.stereotype.Controller;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.behrend.contestmanager.models.*;
import com.behrend.contestmanager.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

@Controller
public class SearchController {
    
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

    @GetMapping(value = "/player/search", params = {"searchType", "searchFilter"})
    @ResponseBody
    public List<Player> searchPlayer(@RequestParam(name = "searchType") String type,
                                     @RequestParam(name = "searchFilter") String filter) {
        ArrayList<Player> players = new ArrayList<Player>();
        
        System.out.printf("%s  |  %s", type, filter);

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
        
        if (type.equals("all")) {
            tournaments.addAll(tournamentService.findAllTournaments());
        }
        if (type.equals("id")) {
            try {
                long tournamentId = Long.parseLong(filter);
                tournaments.add(tournamentService.findTournamentById(tournamentId));
            }
            catch (Exception e) {
                return null;
            }
        }
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

    @GetMapping(value = "/rule/search", params = {"searchType", "searchFilter"})
    @ResponseBody
    public ResponseEntity<String> searchRule(@RequestParam(name = "searchType") String type,
                                             @RequestParam(name = "searchFilter") String filter) {

        ObjectMapper mapper = new ObjectMapper();

        if (type.equals("all")) {
            List<Rule> rules = ruleService.findAllRules();
            try {
                String ruleListAsJson = mapper.writeValueAsString(rules);
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(ruleListAsJson);
            }
            catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error finding rule, please try again");
            }
        }
        if (type.equals("id")) {
            try {
                long ruleId = Long.parseLong(filter);
                Rule rule = ruleService.findRuleById(ruleId);
                String ruleAsJson = mapper.writeValueAsString(rule);
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(ruleAsJson);
            }
            catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID provided is not a number");
            }
            catch (JsonProcessingException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error finding rule, please try again");
            }
        }
        if (type.equals("ruleName")) {
            try {
                Rule rule = ruleService.findRuleByName(filter);
                String ruleAsJson = mapper.writeValueAsString(rule);
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(ruleAsJson);
            }
            catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error finding rule, please try again");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid search type");
    }
    
    @GetMapping(value = "/match/search", params={"playerOneId, playerTwoId, tournamentId"})
    @ResponseBody
    public ResponseEntity<String> searchMatch(@RequestParam(name = "searchType") String type,
                                              @RequestParam(name = "searchFilter") String filter) {
        ArrayList<Match> matches = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        if (type.equals("playerId")) {
            long playerId;
            try {
                playerId = Long.parseLong(filter);
            }
            catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID provided is not a number");
            }

            Player player = playerService.findPlayerById(playerId);
            matches.addAll(matchService.getMatchesByPlayer(player));
            String matchesAsJson;
            try {
                matchesAsJson = mapper.writeValueAsString(matches);
            }
            catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error finding matches, please try again");
            }
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(matchesAsJson);
        }
        if (type.equals("tournamentId")) {
            long tournamentId;
            try {
                tournamentId = Long.parseLong(filter);
            }
            catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID provided is not a number");
            }

            Tournament tournament = tournamentService.findTournamentById(tournamentId);
            String tournamentAsJson;
            try {
                tournamentAsJson = mapper.writeValueAsString(tournament);
            }
            catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error finding matches, please try again");
            }
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(tournamentAsJson);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid search type");
    }
}
