package com.behrend.contestmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.behrend.contestmanager.models.Tournament;
import com.behrend.contestmanager.models.Ruleset;
import com.behrend.contestmanager.models.Player;
import com.behrend.contestmanager.repository.TournamentRepository;

import java.util.List;
import java.util.ArrayList;
import java.sql.Date;

@Service
public class TournamentServiceImpl implements TournamentService {
    
    @Autowired
    private TournamentRepository tournamentRepository;

    // Create
    @Override
    public Tournament saveTournament(Tournament tournament) {
        return tournamentRepository.save(tournament);
    }

    // Read
    @Override
    public Tournament findTournamentById(long tournamentId) {
        return tournamentRepository.findById(tournamentId).orElse(null);
    }

    @Override
    public List<Tournament> findAllTournaments() {
        return (List<Tournament>) tournamentRepository.findAll();
    }

    @Override
    public List<Tournament> findTournamentsByName(String name) {
        return (List<Tournament>) tournamentRepository.findTournamentsByName(name);
    }

    @Override
    public List<Tournament> findTournamentsByLocation(String location) {
        return (List<Tournament>) tournamentRepository.findTournamentsByLocation(location);
    }

    @Override
    public List<Tournament> findTournamentsByDate(Date date) {
        return (List<Tournament>) tournamentRepository.findTournamentsByDate(date);
    }

    @Override
    public List<Tournament> findTournamentsByRuleset(Ruleset ruleset) {
        return (List<Tournament>) tournamentRepository.findTournamentsByRulesetId(ruleset.getRulesetId());
    }

    // Update
    public Tournament updateTournament(Tournament tournament, long tournamentId) {
        Tournament currentTournament = tournamentRepository.findById(tournamentId).orElse(null);

        if (currentTournament == null)
        {
            return null;
        }

        if (!"".equalsIgnoreCase(tournament.getName())) {
            currentTournament.setName(tournament.getName());
        }

        if (!"".equalsIgnoreCase(tournament.getLocation())) {
            currentTournament.setLocation(tournament.getLocation());
        }

        if (!tournament.getDate().toString().equals(currentTournament.getDate().toString())) {
            currentTournament.setDate(tournament.getDate());
        }

        if (tournament.getRuleset() != null) {
            currentTournament.setRuleset(tournament.getRuleset());
        }

        if (currentTournament.getPlayers() == null) {
            currentTournament.setPlayers(tournament.getPlayers());
        }
        else if (!tournament.getPlayers().equals(currentTournament.getPlayers())) {
            currentTournament.setPlayers(tournament.getPlayers());
        }

        return tournamentRepository.save(currentTournament);
    }

    public Tournament addPlayerToTournament(Tournament tournament, Player player) {
        
        Tournament updatedTournament = tournamentRepository.findById(tournament.getTournamentId()).get();

        if (!updatedTournament.getPlayers().contains(player)) {
            ArrayList<Player> playerList = (ArrayList<Player>) updatedTournament.getPlayers();
            playerList.add(player);
            updatedTournament.setPlayers(playerList);
        }

        return tournamentRepository.save(updatedTournament);
    }

    // Delete
    public void deleteTournamentById(long tournamentId) {
        tournamentRepository.deleteById(tournamentId);
    }
}
