package com.behrend.contestmanager.repository;

import org.springframework.data.repository.CrudRepository;

import com.behrend.contestmanager.models.Match;

import java.util.List;

public interface MatchRepository extends CrudRepository<Match, Long> {

    List<Match> findAllByPlayerOneId(long playerId);

    List<Match> findAllByPlayerTwoId(long playerId);

    List<Match> findAllByTournamentId(long tournamentId);
    
}