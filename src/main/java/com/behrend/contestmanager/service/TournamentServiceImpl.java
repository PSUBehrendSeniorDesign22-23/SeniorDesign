package com.behrend.contestmanager.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.behrend.contestmanager.models.Tournament;
import com.behrend.contestmanager.models.Ruleset;
import com.behrend.contestmanager.repository.TournamentRepository;

import java.util.ArrayList;
import java.sql.Date;

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
        return tournamentRepository.findById(tournamentId).get();
    }

    @Override
    public ArrayList<Tournament> findAllTournaments() {
        return (ArrayList<Tournament>) tournamentRepository.findAll();
    }

    @Override
    public ArrayList<Tournament> findTournamentsByName(String name) {
        return (ArrayList<Tournament>) tournamentRepository.findTournamentsByName(name);
    }

    @Override
    public ArrayList<Tournament> findTournamentsByLocation(String location) {
        return (ArrayList<Tournament>) tournamentRepository.findTournamentsByLocation(location);
    }

    @Override
    public ArrayList<Tournament> findTournamentsByDate(Date date) {
        return (ArrayList<Tournament>) tournamentRepository.findTournamentsByDate(date);
    }

    @Override
    public ArrayList<Tournament> findTournamentsByRuleset(Ruleset ruleset) {
        return (ArrayList<Tournament>) tournamentRepository.findTournamentsByRulesetId(ruleset.getRulesetId());
    }

    // Update
    public Tournament updateTournament(Tournament tournament, long tournamentId) {
        Tournament currentTournament = tournamentRepository.findById(tournament.getTournamentId()).get();

        return currentTournament;
    }

    // Delete
    public void deleteTournamentById(long tournamentId) {
        tournamentRepository.deleteById(tournamentId);
    }
}
