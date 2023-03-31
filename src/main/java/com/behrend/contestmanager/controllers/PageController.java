package com.behrend.contestmanager.controllers;

import com.behrend.contestmanager.models.Role;
import com.behrend.contestmanager.models.User;
import com.behrend.contestmanager.repository.RoleRepository;
import com.behrend.contestmanager.repository.UserRepository;
import com.behrend.contestmanager.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PageController {

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

    @Autowired
    RoleRepository roleRepo;

    @GetMapping(value = "/")
    public String index(Model model) {
        return "Landing";
    }

    @PostMapping("/login")
    public String UserLogin(User user) {
        //create endpoints for redirects
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User acc = userRepo.findByEmail(user.getEmail());
        if(acc==null){return "redirect:/";}
        if(encoder.matches(user.getPassword(),acc.getPassword())){
            UserService.setLoggedIn(acc.getUserId());
            return "redirect:/PublicBrowse";
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
        user.roles.add(roleRepo.findRoleByName("USER"));
        userRepo.save(user);
        UserService.setLoggedIn(user.getUserId());
        return "redirect:/PublicBrowse";
    }

    @GetMapping(value="/PublicBrowse", produces = "application/json")
    public String publicBrowse(Model model){return "PublicBrowse";}

    @GetMapping(value = "/LoginFaled", produces = "application/json")
    public String loginFailed(){return "LoginFailed";}

    @GetMapping(value = "/CoordinatorTools", produces = "application/json")
    public String coordinatorTools(){return "Coordinator";}


}
