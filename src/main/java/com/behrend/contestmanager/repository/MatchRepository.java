package com.behrend.contestmanager.repository;

import org.springframework.data.repository.CrudRepository;

import com.behrend.contestmanager.models.Match;

import java.util.List;

public interface MatchRepository extends CrudRepository<Match, Long> {

    List<Match> findAllByDefenderId(long playerId);

    List<Match> findAllByChallengerId(long playerId);

    List<Match> findAllByTournamentId(long tournamentId);
    
}