package com.behrend.contestmanager.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.behrend.contestmanager.repository.MatchRepository;
import com.behrend.contestmanager.models.Match;
import com.behrend.contestmanager.models.Player;
import com.behrend.contestmanager.models.Tournament;

import java.util.ArrayList;

@Service
public class MatchServiceImpl implements MatchService{
    
    @Autowired
    private MatchRepository matchRepository;

    // Create
    @Override
    public Match saveMatch(Match match) {

        if (match.getDefenderScore() < 0)  {
            match.setDefenderScore(0);
        }

        if (match.getChallengerScore() < 0) {
            match.setChallengerScore(0);
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
       
        Match currentMatch = matchRepository.findById(matchId).orElse(null);

        if (currentMatch == null) {
            return null;
        }

        if (match.getDefender() != null) {
            currentMatch.setDefender(match.getDefender());
        }

        if (match.getChallenger() != null) {
            currentMatch.setChallenger(match.getChallenger());
        }

        if (match.getDefenderScore() != -1) {
            currentMatch.setDefenderScore(match.getDefenderScore());
        }

        if (match.getChallengerScore() != -1) {
            currentMatch.setChallengerScore(match.getChallengerScore());
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
