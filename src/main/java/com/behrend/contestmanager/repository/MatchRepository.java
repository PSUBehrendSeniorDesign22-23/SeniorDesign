package com.behrend.contestmanager.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.behrend.contestmanager.models.Match;

import java.util.List;

@Repository
public interface MatchRepository extends CrudRepository<Match, Long> {

    List<Match> findAllByPlayerOneId(long playerId);

    List<Match> findAllByPlayerTwoId(long playerId);

    List<Match> findAllByTournamentId(long tournamentId);
    
}