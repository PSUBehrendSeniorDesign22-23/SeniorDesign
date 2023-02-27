package com.behrend.contestmanager.controllers;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.behrend.contestmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.behrend.contestmanager.models.*;
import com.behrend.contestmanager.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

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

    @Autowired
    UserRepository userRepo;

    @GetMapping(value = "/")
    public String index(Model model) {
        return "Landing";
    }

    @PostMapping("/login")
    public String UserLogin(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User acc = userRepo.findByEmail(user.getEmail());
        if(acc==null){return "redirect:/LoginFailed";}
        if(encoder.matches(user.getPassword(),acc.getPassword())){
            UserService.setLoggedIn(acc.getUserId());
            return "redirect:/DevelopmentTools";
        }
        return "redirect:/";
    }

    @PostMapping("/registerUser")
    public String registerUser(User user){
        if(userRepo.findByEmail(user.getEmail()) != null){
            return "redirect:/";
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encryptedPass =encoder.encode(user.getPassword());
        user.setPassword(encryptedPass);
        //Roles role = new Roles();
        //role.setName("USER");
        userRepo.save(user);
        UserService.setLoggedIn(user.getUserId());
        return "redirect:/DevelopmentTools";
    }

    @GetMapping(value = "/player/search", params = {"searchType", "searchFilter"})
    @ResponseBody
    public List<Player> searchPlayer(@RequestParam(name = "searchType") String type,
                                     @RequestParam(name = "searchFilter") String filter) {
        ArrayList<Player> players = new ArrayList<Player>();
        
        System.out.printf("%s  |  %s", type, filter);

        if (type.equals("pname"))
        {
            System.out.println("Test");
            players.addAll(playerService.findPlayersByFirstName(filter));
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
    
    @PostMapping(value = "/player/create", params = {"addfname","addlname","addssname","addeadd","addpnum"})
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

    @PatchMapping(value = "/player/update", params = {"playerId", "skipperName", "rank"})
    @ResponseBody
    public ResponseEntity<String> updatePlayer(@RequestParam(name = "playerId") long playerId,
                                               @RequestParam(name = "skipperName") String skipperName,
                                               @RequestParam(name = "rank") int rank) {
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
                                                   @RequestParam(name = "rulesetId") long rulesetId,
                                                   @RequestParam(name = "playerIds") ArrayList<Long> playerIds) {
        Tournament tournament = new Tournament();
        tournament.setName(name);
        tournament.setLocation(location);
        tournament.setDate(date);

        Ruleset ruleset = rulesetService.findRulesetById(rulesetId);

        if (ruleset == null) {
            return ResponseEntity.badRequest().body("Ruleset not found");
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

    @PatchMapping(value = "/match/update", params = {"matchId", "playerOneId", "playerTwoId", "tournamentId", "playerOneScore", "playerTwoScore"})
    @ResponseBody
    public ResponseEntity<String> updateMatch(@RequestParam(name = "matchId") long matchId,
                                              @RequestParam(name = "playerOneId") long playerOneId,
                                              @RequestParam(name = "playerTwoId") long playerTwoId,
                                              @RequestParam(name = "tournamentId") long tournamentId,
                                              @RequestParam(name = "playerOneScore") int playerOneScore,
                                              @RequestParam(name = "playerTwoScore") int playerTwoScore) {
        Match match = new Match();

        Player playerOne = playerService.findPlayerById(playerOneId);

        if (playerOne == null) {
            return ResponseEntity.badRequest().body("Player one not found");
        }

        Player playerTwo = playerService.findPlayerById(playerTwoId);

        if (playerTwo == null) {
            return ResponseEntity.badRequest().body("Player two not found");
        }

        Tournament tournament = tournamentService.findTournamentById(tournamentId);

        if (tournament == null) {
            return ResponseEntity.badRequest().body("Tournament not found");
        }

        match.setPlayerOne(playerOne);
        match.setPlayerTwo(playerTwo);
        match.setTournament(tournament);
        match.setPlayerOneScore(playerOneScore);
        match.setPlayerTwoScore(playerTwoScore);

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


    // General utility function to replace the frequently repeated lines above
    // Needs further implementation
    private ResponseEntity<String> responseGenerator(HttpStatus status, String body) {
        return ResponseEntity.status(status).body(body);
    }

    private <T> String JsonMapper(T object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
