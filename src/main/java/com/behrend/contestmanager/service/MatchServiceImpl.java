package com.behrend.contestmanager.service;


import org.springframework.beans.factory.annotation.Autowired;

import com.behrend.contestmanager.repository.MatchRepository;
import com.behrend.contestmanager.models.Match;
import com.behrend.contestmanager.models.Player;
import com.behrend.contestmanager.models.Tournament;

import java.util.ArrayList;


public class MatchServiceImpl implements MatchService{
    
    @Autowired
    private MatchRepository matchRepository;

    // Create
    @Override
    public Match saveMatch(Match match) {

        if (match.getPlayerOneScore() < 0)  {
            match.setPlayerOneScore(0);
        }

        if (match.getPlayerTwoScore() < 0) {
            match.setPlayerTwoScore(0);
        }

        return matchRepository.save(match);
    }

    // Read
    @Override
    public ArrayList<Match> getAllMatches() {
        return (ArrayList<Match>) matchRepository.findAll();
    }

    @Override
    public ArrayList<Match> getMatchesByPlayer(Player player) {
        
        ArrayList<Match> playerList = new ArrayList<>();
        playerList.addAll(matchRepository.findAllByPlayerOneId(player.getPlayerId()));
        playerList.addAll(matchRepository.findAllByPlayerTwoId(player.getPlayerId()));
        return playerList;
    }

    @Override
    public ArrayList<Match> getMatchesByTournament(Tournament tournament) {
        return (ArrayList<Match>) matchRepository.findAllByTournamentId(tournament.getTournamentId());
    }

    // Update
    @Override
    public Match updateMatch(Match match, long matchId) {
       
        Match currentMatch = matchRepository.findById(matchId).get();

        if (match.getPlayerOne() != null) {
            currentMatch.setPlayerOne(match.getPlayerOne());
        }

        if (match.getPlayerTwo() != null) {
            currentMatch.setPlayerTwo(match.getPlayerTwo());
        }

        if (match.getPlayerOneScore() != -1) {
            currentMatch.setPlayerOneScore(match.getPlayerOneScore());
        }

        if (match.getPlayerTwoScore() != -1) {
            currentMatch.setPlayerTwoScore(match.getPlayerTwoScore());
        }

        if (match.getTournament() != null) {
            currentMatch.setTournament(match.getTournament());
        }

        return matchRepository.save(currentMatch);
    }

    // Delete
    @Override
    public void deleteMatchById(long matchId) {
        matchRepository.deleteById(matchId);
    }
}
