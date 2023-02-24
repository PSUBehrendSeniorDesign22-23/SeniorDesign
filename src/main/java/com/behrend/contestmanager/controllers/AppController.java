package com.behrend.contestmanager.controllers;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.behrend.contestmanager.models.*;
import com.behrend.contestmanager.repository.UserRepository;
import com.behrend.contestmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.behrend.contestmanager.repository.PlayerRepository;
import com.behrend.contestmanager.repository.RulesetRepository;
import com.behrend.contestmanager.repository.TournamentRepository;

import javax.management.relation.Role;

@Controller
public class AppController {
    @Autowired
    PlayerRepository playerRepo;

    @Autowired
    TournamentRepository tourRepo;

    @Autowired
    RulesetRepository ruleSetRepo;

    @Autowired
    UserRepository userRepo;

    @GetMapping(value = "/")
    public String index(Model model) {
        return "Landing";
    }

    @PostMapping(value = "/login", params = {"email", "psw"})
    public String UserLogin(@RequestParam(name = "email") String email,
                            @RequestParam(name = "psw") String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User acc = userRepo.findByEmail(email);
        if(acc==null){return "redirect:/LoginFailed";}
        if(encoder.matches(password,acc.getPassword())){
            UserService.setLoggedIn(acc.getId());
            return "redirect:/DevelopmentTools";
        }
        return "redirect:/";
    }

    @PostMapping(value="/registerUser", params = {"firstName", "lastName", "emailSU","phonrNum","address","pswSU"})
    @ResponseBody
    public String registerUser(@RequestParam(name = "firstName") String firstName,
                               @RequestParam(name = "lastName") String lastName,
                               @RequestParam(name = "emailSU") String email,
                               @RequestParam(name = "phonrNum") String phoneNum,
                               @RequestParam(name = "address") String address,
                               @RequestParam(name = "pswSU") String password){
        User user = new User();
        if(userRepo.findByEmail(email) != null){
            return "redirect:/";
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encryptedPass =encoder.encode(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhoneNum(phoneNum);
        user.setAddress(address);
        user.setPassword(encryptedPass);
        //Roles role = new Roles();
        //role.setName("USER");
        userRepo.save(user);
        UserService.setLoggedIn(user.getId());
        return "redirect:/DevelopmentTools";
    }

    @GetMapping(value = "/players/search", params = {"searchType", "searchFilter"})
    @ResponseBody
    public List<Player> searchPlayer(@RequestParam(name = "searchType") String type, @RequestParam(name = "searchFilter") String filter) {
        ArrayList<Player> players = new ArrayList<Player>();
        
        System.out.printf("%s  |  %s", type, filter);

        if (type.equals("pname"))
        {
            System.out.println("Test");
            players.addAll(playerRepo.findAllByFirstName(filter));
        }
        if (type.equals("pssname"))
        {
            players.add(playerRepo.findBySkipperName(filter));
        }

        return players;
    }

    @GetMapping(value = "/tournament/search", params = {"searchType", "searchFilter"})
    @ResponseBody
    public List<Tournament> searchTournament(@RequestParam(name = "searchType") String type, @RequestParam(name = "searchFilter") String filter) {
        ArrayList<Tournament> tournaments = new ArrayList<Tournament>();
        
        if (type.equals("tname"))
        {
            tournaments.add(tourRepo.findTournamentByName(filter));
        }
        if (type.equals("tlocation"))
        {
            tournaments.addAll(tourRepo.findTournamentsByLocation(filter));
        }
        if (type.equals("tdate"))
        {
            try
            {
                Date inputDate = Date.valueOf(filter);
                tournaments.addAll(tourRepo.findTournamentsByDate(inputDate));
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
    public List<Ruleset> searchRuleset(@RequestParam(name = "searchType") String type, @RequestParam(name = "searchFilter") String filter) {
        ArrayList<Ruleset> rulesets = new ArrayList<Ruleset>();
        
        if (type.equals("rname"))
        {
            rulesets.add(ruleSetRepo.findRulesetByName(filter));
        }
        if (type.equals("rorigin"))
        {
            rulesets.addAll(ruleSetRepo.findRulesetsByOrigin(filter));
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
        
        playerRepo.save(player);

        return ResponseEntity.ok("{\"operation\": \"success\"}");
    }

    @PostMapping(value = "/tournament/create", params = {"addtname","addtloc","addtdate","addtrule"})
    @ResponseBody
    public ResponseEntity<String> handleData(@RequestParam(name = "addtname") String name, @RequestParam(name = "addtloc") String location, @RequestParam(name = "addtdate") Date date, @RequestParam(name = "addtrule") String ruleSetName){

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
            Ruleset ruleSet = ruleSetRepo.findRulesetByName(ruleSetName);
            if( ruleSet == null){
                return ResponseEntity.status(HttpStatus.valueOf(400)).body("{\"operation\": \"failure\", \"message\": \"Ruleset not found\"}");
            }else{
                tournament.setRuleset(ruleSet);
            }
        }
        tourRepo.save(tournament);

        return ResponseEntity.ok("{\"operation\": \"success\"}");
    }

    @PostMapping(value = "/ruleset/create", params = {"addrname","addrorigin"})
    @ResponseBody
    public ResponseEntity<String> handleData(@RequestParam(name = "addrname") String RuleSetName, @RequestParam(name = "addrorigin") String origin){

        Ruleset ruleset = new Ruleset();

        if(RuleSetName != null){
            ruleset.setName(RuleSetName);
        }
        if(origin != null){
            ruleset.setOrigin(origin);
        }
        ruleSetRepo.save(ruleset);

        return ResponseEntity.ok("{\"operation\": \"success\"}");
    }

}
