package com.behrend.contestmanager.service;

import com.behrend.contestmanager.models.Tournament;
import com.behrend.contestmanager.models.Ruleset;

import java.util.List;
import java.sql.Date;

public interface TournamentService {
    // Create
    Tournament saveTournament(Tournament tournament);

    // Read
    Tournament findTournamentById(long tournamentId);

    List<Tournament> findAllTournaments();

    List<Tournament> findTournamentsByName(String name);

    List<Tournament> findTournamentsByLocation(String location);

    List<Tournament> findTournamentsByDate(Date date);

    List<Tournament> findTournamentsByRuleset(Ruleset ruleset);

    // Update
    Tournament updateTournament(Tournament tournament, long tournamentId);

    // Delete
    void deleteTournamentById(long tournamentId);
}
