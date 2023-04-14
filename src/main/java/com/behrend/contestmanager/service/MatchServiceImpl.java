package com.behrend.contestmanager.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.behrend.contestmanager.repository.MatchRepository;
import com.behrend.contestmanager.models.Match;
import com.behrend.contestmanager.models.Player;
import com.behrend.contestmanager.models.Tournament;

import java.util.List;
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
    public List<Match> getAllMatches() {
        return (List<Match>) matchRepository.findAll();
    }

    @Override
    public List<Match> getMatchesByPlayer(Player player) {
        
        ArrayList<Match> matchList = new ArrayList<>();
        matchList.addAll(matchRepository.findAllByDefenderId(player.getPlayerId()));
        matchList.addAll(matchRepository.findAllByChallengerId(player.getPlayerId()));
        return matchList;
    }

    @Override
    public List<Match> getMatchesByTournament(Tournament tournament) {
        return matchRepository.findAllByTournamentId(tournament.getTournamentId());
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
