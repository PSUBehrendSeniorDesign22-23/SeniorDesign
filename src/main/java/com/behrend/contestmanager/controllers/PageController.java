package com.behrend.contestmanager.controllers;

import com.behrend.contestmanager.models.Role;
import com.behrend.contestmanager.models.User;
import com.behrend.contestmanager.repository.RoleRepository;
import com.behrend.contestmanager.repository.UserRepository;
import com.behrend.contestmanager.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(value = "/login", params = {"email", "psw"})
    public String UserLogin(@RequestParam(name = "email") String email,
                            @RequestParam(name = "psw") String password) {
        //create endpoints for redirects
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User acc = userRepo.findByEmail(email);
        if(acc==null){return "redirect:/LoginFailed";}
        if(encoder.matches(password,acc.getPassword())){
            UserService.setLoggedIn(acc.getUserId());
            return "redirect:PublicBrowse";
        }
        return "redirect:/";
    }

    @PostMapping(value="/registerUser", params = {"firstName", "lastName", "emailSU","phoneNum","address","pswSU","psw-repeat", "remember"})
    @ResponseBody
    public ResponseEntity<String> registerUser(@RequestParam(name = "firstName") String firstName,
                                       @RequestParam(name = "lastName") String lastName,
                                       @RequestParam(name = "emailSU") String email,
                                       @RequestParam(name = "phoneNum") String phoneNum,
                                       @RequestParam(name = "address") String address,
                                       @RequestParam(name = "pswSU") String password,
                                       @RequestParam(name = "psw-repeat") String pswRep,
                                       @RequestParam(name = "remember") String rem){
        User user = new User();
        if(userRepo.findByEmail(email) != null){
            return new ResponseEntity<>("An account with this email already exists", HttpStatus.BAD_REQUEST);
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encryptedPass =encoder.encode(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhoneNum(phoneNum);
        user.setAddress(address);
        user.setPassword(encryptedPass);
        UserService.setLoggedIn(user.getUserId());
        user.setPassword(encryptedPass);
        user.roles.add(roleRepo.findRoleByName("USER"));
        userRepo.save(user);
        UserService.setLoggedIn(user.getUserId());
        return ResponseEntity.ok("{\"operation\": \"success\"}");
    }

    @GetMapping(value="/PublicBrowse", produces = "application/json")
    public String publicBrowse(Model model){return "PublicBrowse";}

    @GetMapping(value = "/LoginFaled", produces = "application/json")
    public String loginFailed(){return "LoginFailed";}

    @GetMapping(value = "/CoordinatorTools", produces = "application/json")
    public String coordinatorTools(){return "Coordinator";}


}
