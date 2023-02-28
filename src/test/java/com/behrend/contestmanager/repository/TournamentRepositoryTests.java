package com.behrend.contestmanager.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import com.behrend.contestmanager.models.Ruleset;
import com.behrend.contestmanager.models.Tournament;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@Sql(scripts = "/create-tournament-data.sql")
@Sql(scripts = "/cleanup-tournament-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class TournamentRepositoryTests {
    
    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private RulesetRepository rulesetRepository;

    @Test
    void findAllTournaments() {
        ArrayList<Tournament> tournaments = (ArrayList<Tournament>) tournamentRepository.findAll();
        Assertions.assertEquals(2, tournaments.size());
    }

    @Test
    void findTournamentById() {
        ArrayList<Long> ids = new ArrayList<>();
        ids.add(11L);
        Iterable<Tournament> tournaments = tournamentRepository.findAllById(ids);
        for (Tournament tournament : tournaments)
        {
            Assertions.assertEquals(tournament.getTournamentId(), ids.get(0));
        }
    }

    @Test
    void findTournamentByName() {
        String name = "TournamentOne";
        Tournament tournament = tournamentRepository.findTournamentsByName(name).get(0);
        Assertions.assertEquals(name, tournament.getName());
    }

    @Test 
    void findTournamentsByLocation()
    {
        String location = "North America";
        List<Tournament> tournaments = tournamentRepository.findTournamentsByLocation(location);
        for (Tournament tournament : tournaments)
        {
            Assertions.assertEquals(location, tournament.getLocation());
        }
    }

    @Test
    void findTournamentsByDate()
    {
        Date date = Date.valueOf(LocalDate.of(1980, 12, 7));
        List<Tournament> tournaments = tournamentRepository.findTournamentsByDate(date);
        for (Tournament tournament : tournaments)
        {
            Assertions.assertEquals(date, tournament.getDate());
        }
    }

    @Test
    void findTournamentsByRulesetId()
    {
        
    }

    @Test
    void createTournament() {

        Tournament tournament = new Tournament();
        Ruleset ruleset = new Ruleset();

        ruleset.setName("TestRuleset");
        ruleset.setOrigin("North America");

        ruleset = rulesetRepository.save(ruleset);

        tournament.setName("TestTournament");
        tournament.setLocation("North America");
        tournament.setDate(Date.valueOf(LocalDate.of(2012, 12, 12)));
        tournament.setRuleset(ruleset);

        tournament = tournamentRepository.save(tournament);

        List<Tournament> tournamentsInDb = (List<Tournament>) tournamentRepository.findAll();

        boolean tournamentFound = false;

        for (Tournament t : tournamentsInDb)
        {
            if (t.getTournamentId() == tournament.getTournamentId())
            {
                tournamentFound = true;
            }
        }

        Assertions.assertTrue(tournamentFound);
    }
}
