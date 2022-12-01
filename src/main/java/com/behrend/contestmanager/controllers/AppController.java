package com.behrend.contestmanager.controllers;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.behrend.contestmanager.models.Player;
import com.behrend.contestmanager.models.Ruleset;
import com.behrend.contestmanager.models.Tournament;
import com.behrend.contestmanager.repository.PlayerRepository;
import com.behrend.contestmanager.repository.RulesetRepository;
import com.behrend.contestmanager.repository.TournamentRepository;

@Controller
public class AppController {
    @Autowired
    PlayerRepository playerRepo;

    @Autowired
    TournamentRepository tourRepo;

    @Autowired
    RulesetRepository ruleSetRepo;
    
    @PostMapping(value = "/", params = {"addfname","addlname","addssname","addeadd","addpnum"})
    public String handleData(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String skipperName, @RequestParam String email, @RequestParam String phoneNum, Player player){
        if(firstName != null){
            player.setFirstName(firstName);
        }else{
            //do something
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

        return "redirect:/";
    }

    @PostMapping(value = "/", params = {"addtname","addtloc","addtdate","addtrule"})
    public String handleData(@RequestParam String name, @RequestParam String location, @RequestParam Date date, @RequestParam String ruleSetName, Tournament tournament){
        if(name != null){
            tournament.setName(ruleSetName);
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
                //throw error
            }else{
                tournament.setRuleset(ruleSet);
            }
        }
        tourRepo.save(tournament);

        return "redirect:/";
    }

    @PostMapping(value = "/", params = {"addrname","addrorigin"})
    public String handleData(@RequestParam String RuleSetName, @RequestParam String origin, Ruleset ruleset){

        if(RuleSetName != null){
            ruleset.setName(RuleSetName);
        }
        if(origin != null){
            ruleset.setOrigin(origin);
        }
        ruleSetRepo.save(ruleset);

        return "redirect:/";
    }

}
