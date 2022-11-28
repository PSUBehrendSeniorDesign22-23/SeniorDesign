package com.behrend.contestmanager.repository;

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
@Sql(scripts = "create-match-data.sql")
@Sql(scripts = "cleanup-match-data.sql")
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class MatchRepositoryTests {
    
    @Autowired
    private MatchRepository matchRepository;

    @Test
    void findAllMatches() {
        List<Match> matches = (List<Match>) matchRepository.findAll();
        Assertions.assertEquals(3, matches.size());
    }

    @Test
    void findMatchById() {

    }

    @Test
    void findMatchesByPlayerOne() {

    }

    @Test
    void findMatchesByPlayerTwo() {

    }

    @Test
    void findAllByMatchesTournament() {

    }
}
