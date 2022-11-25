package com.controllers;

import org.springframework.stereotype.Controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.behrend.contestmanager.models.Tournament;
import com.repositories.TournamentRepository;

@Controller
@RequestMapping("/tournament")
public class TournamentController {

    @Autowired
    TournamentRepository tournamentRepo;

    @GetMapping("/create")
    public String createTournament(Tournament tournament){
        return "processesTournament";
    }

    
}
