package com.behrend.contestmanager.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.behrend.contestmanager.models.Match;
import com.behrend.contestmanager.models.Tournament;
import com.behrend.contestmanager.models.Player;

import java.util.List;

@Repository
public interface MatchRepository extends CrudRepository<Match, Long> {

    List<Match> findAllByPlayerOne(Player playerOne);

    List<Match> findAllByPlayerTwo(Player playerTwo);

    List<Match> findAllByTournament(Tournament tournament);
    
}