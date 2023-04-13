package com.behrend.contestmanager.controllers;

import com.behrend.contestmanager.models.Role;
import com.behrend.contestmanager.models.User;
import com.behrend.contestmanager.repository.UserRepository;
import com.behrend.contestmanager.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.behrend.contestmanager.models.Role.USER;

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

   

    @PostMapping(value="/registerUser")
    public String registerUser( @RequestParam(name = "firstName") String firstName,
                                @RequestParam(name = "lastName") String lastName,
                                @RequestParam(name = "emailSU") String email,
                                @RequestParam(name = "phoneNum") String phoneNum,
                                @RequestParam(name = "address") String address,
                                @RequestParam(name = "pswSU") String password,
                                @RequestParam(name = "psw-repeat") String pswRep) {
        User user = new User();

        if(userRepo.findByEmail(email) != null){
            //new ResponseEntity<>("An account with this email already exists", HttpStatus.BAD_REQUEST)
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
        UserService.setLoggedIn(user.getUserId());
        user.setPassword(encryptedPass);

        ArrayList<Role> roles = new ArrayList<>();
        roles.add(USER);
        user.setRoles(roles);
        userRepo.save(user);
        UserService.setLoggedIn(user.getUserId());
        return "redirect:/PublicBrowse";
    }

    @GetMapping(value="/PublicBrowse", produces = "application/json")
    public String publicBrowse(Model model){return "PublicBrowse";}

    @GetMapping(value = "/LoginFailed", produces = "application/json")
    public String loginFailed(){return "LoginFailed";}

    @GetMapping(value = "/CoordinatorTools", produces = "application/json")
    public String coordinatorTools(){return "Coordinator";}


}
