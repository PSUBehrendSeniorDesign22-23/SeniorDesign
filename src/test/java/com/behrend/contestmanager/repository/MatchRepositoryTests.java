package com.behrend.contestmanager.repository;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import com.behrend.contestmanager.models.Match;

@DataJpaTest
@Sql(scripts = "/create-match-data.sql")
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class MatchRepositoryTests {
    
    @Autowired
    private MatchRepository matchRepository;

    @Test
    void findAllMatches() {
        ArrayList<Match> matches = (ArrayList<Match>) matchRepository.findAll();
        Assertions.assertEquals(4, matches.size());
    }

    @Test
    void findMatchById() {
        ArrayList<Long> ids = new ArrayList<>();
        ids.add(11L);
        Iterable<Match> matches = matchRepository.findAllById(ids);
        for (Match match : matches)
        {
            Assertions.assertEquals(match.getMatchId(), ids.get(0));
        }
    }

    @Test
    void findMatchesByPlayerId() {
        long playerId = 11L;
        List<Match> matchesOne = matchRepository.findAllByPlayerOneId(playerId);
        List<Match> matchesTwo = matchRepository.findAllByPlayerTwoId(playerId);
        
        boolean matchPlayerOneFound = false;
        boolean matchPlayerTwoFound = false;

        for (Match match : matchesOne)
        {
            if (match.getPlayerOne().getPlayerId() == playerId)
            {
                matchPlayerOneFound = true;
            }
            
        }
        
        Assertions.assertTrue(matchPlayerOneFound);

        for (Match match : matchesTwo)
        {
            if (match.getPlayerTwo().getPlayerId() == playerId) 
            {
                matchPlayerTwoFound = true;
            }
        }
        
        Assertions.assertTrue(matchPlayerTwoFound);
    }

    @Test
    void findAllByMatchesByTournamentId() {
        long tournamentId = 11L;
        List<Match> matches = matchRepository.findAllByTournamentId(tournamentId);
        
        boolean matchTournamentOneFound = false;
        boolean matchTournamentTwoFound = false;

        for (Match match : matches)
        {
            if (match.getTournament().getTournamentId() == tournamentId)
            {
                matchTournamentOneFound = true;
            }
            if (match.getTournament().getTournamentId() == tournamentId) 
            {
                matchTournamentTwoFound = true;
            }
        }

        Assertions.assertTrue(matchTournamentOneFound);
        Assertions.assertTrue(matchTournamentTwoFound);
    }
}
