package com.behrend.contestmanager.service;

import com.behrend.contestmanager.models.Match;
import com.behrend.contestmanager.models.Player;
import com.behrend.contestmanager.models.Tournament;

import java.util.List;

public interface MatchService {
    // Create
    Match saveMatch(Match match);

    // Read
    List<Match> getAllMatches();

    List<Match> getMatchesByTournament(Tournament tournament);

    List<Match> getMatchesByPlayer(Player player);

    // Update
    Match updateMatch(Match match, long matchId);

    // Delete
    void deleteMatchById(long matchId);
}
