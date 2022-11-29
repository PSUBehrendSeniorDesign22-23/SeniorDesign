package com.behrend.contestmanager.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.behrend.contestmanager.models.Match;

import java.util.List;

@Repository
public interface MatchRepository extends CrudRepository<Match, Long> {

    @EntityGraph(value = "match.playerOne", type = EntityGraphType.FETCH)
    List<Match> findAllByPlayerOneId(long playerId);

    @EntityGraph(value = "match.playerTwo", type = EntityGraphType.FETCH)
    List<Match> findAllByPlayerTwoId(long playerId);

    @EntityGraph(value = "match.tournament", type = EntityGraphType.FETCH)
    List<Match> findAllByTournamentId(long tournamentId);
    
}