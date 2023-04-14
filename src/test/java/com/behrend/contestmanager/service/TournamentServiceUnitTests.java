package com.behrend.contestmanager.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import com.behrend.contestmanager.repository.TournamentRepository;
import com.behrend.contestmanager.models.Rule;
import com.behrend.contestmanager.models.Ruleset;
import com.behrend.contestmanager.models.Tournament;

@ExtendWith(MockitoExtension.class)
public class TournamentServiceUnitTests {
    
    @Mock
    private TournamentRepository tournamentRepository;

    @InjectMocks
    private TournamentServiceImpl tournamentService;

    private Tournament tournament;

    private Ruleset ruleset;
    private ArrayList<Rule> rules;

    @BeforeEach
    public void setup() {
        Rule ruleOne = new Rule();
        ruleOne.setName("RuleOne");
        ruleOne.setAttribute("RuleAttributeOne");

        Rule ruleTwo = new Rule();
        ruleTwo.setName("RuleTwo");
        ruleTwo.setAttribute("RuleAttributeTwo");

        
        rules = new ArrayList<>();
        rules.add(ruleOne);
        rules.add(ruleTwo);
        
        ruleset = new Ruleset();
        ruleset.setName("RulesetOne");
        ruleset.setOrigin("RulesetOrigin");
        ruleset.setRules(rules);

        tournament = new Tournament();
        tournament.setName("TournamentOne");
        tournament.setDate(new Date(0));
        tournament.setLocation("LocationOne");
        tournament.setRuleset(ruleset);
    }

    // Save test
    @Test
    public void givenTournamentObject_whenSaveTournament_thenReturnTournamentObject(){
        // given
        given(tournamentRepository.save(tournament)).willReturn(tournament);

        // when
        Tournament savedTournament = tournamentService.saveTournament(tournament);

        // then
        Assertions.assertNotNull(savedTournament);
    }

    // Get All test
    @Test
    public void givenTournaments_whenGetAllTournaments_ReturnTournamentList() {

        Tournament tournament2 = new Tournament();

        // given
        given(tournamentRepository.findAll()).willReturn(List.of(tournament, tournament2));

        // when
        List<Tournament> tournamentList = tournamentService.findAllTournaments();

        // then
        Assertions.assertNotNull(tournamentList);
        Assertions.assertEquals(2, tournamentList.size());
    }

    // Get by name
    @Test
    public void givenString_whenGetByName_thenReturnTournamentWithName() {
        
        String name = "TournamentOne";
        // given
        given(tournamentRepository.findTournamentsByName(name)).willReturn(List.of(tournament));

        // when
        List<Tournament> foundTournaments = tournamentService.findTournamentsByName(name);

        // then
        Assertions.assertNotNull(foundTournaments);
        for (Tournament r : foundTournaments) {
            Assertions.assertEquals(name, r.getName());
        }
    }

    // Get by Id
    @Test
    public void givenTournamentId_whenGetTournamentById_thenReturnTournamentWithId() {
        
        // given
        given(tournamentRepository.findById(tournament.getTournamentId())).willReturn(Optional.of(tournament));
        // when
        Tournament foundTournament = tournamentService.findTournamentById(tournament.getTournamentId());
        // then
        Assertions.assertNotNull(foundTournament);
        Assertions.assertEquals(tournament.getTournamentId(), foundTournament.getTournamentId());
    }

    // Get by location
    @Test
    public void givenLocation_whenGetTournamentByLocation_thenReturnTournamentWithLocation() {
        
        String location = "LocationOne";

        Tournament tournament2 = new Tournament();
        tournament2.setName("TournamentTwo");
        tournament2.setLocation("LocationOne");
        tournament2.setRuleset(ruleset);
        tournament2.setDate(new Date(0));

        // given
        given(tournamentRepository.findTournamentsByLocation(location)).willReturn(List.of(tournament, tournament2));

        // when
        List<Tournament> tournaments = tournamentService.findTournamentsByLocation(location);

        // then
        Assertions.assertNotNull(tournaments);
        for (Tournament foundTournament : tournaments) {
            Assertions.assertEquals(location, foundTournament.getLocation());
        }
    }

    // Get by date
    @Test
    public void givenDate_whenFindTournamentByDate_thenReturnTournamnentWithDate() {
        Date date = new Date(0);
        Date date2 = new Date(100000);

        Tournament tournament2 = new Tournament();
        tournament2.setName("TournamentTwo");
        tournament2.setLocation("LocationOne");
        tournament2.setRuleset(ruleset);
        tournament2.setDate(date2);

        // given
        given(tournamentRepository.findTournamentsByDate(date)).willReturn(List.of(tournament));
        given(tournamentRepository.findTournamentsByDate(date2)).willReturn(List.of(tournament2));

        // when
        List<Tournament> tournamentsWithDate = tournamentService.findTournamentsByDate(date);
        List<Tournament> tournamentsWithDate2 = tournamentService.findTournamentsByDate(date2);

        // then
        Assertions.assertNotNull(tournamentsWithDate);
        Assertions.assertNotNull(tournamentsWithDate2);
        for (Tournament foundTournament : tournamentsWithDate) {
            Assertions.assertEquals(date, foundTournament.getDate());
        }
        for (Tournament foundTournament : tournamentsWithDate2) {
            Assertions.assertEquals(date2, foundTournament.getDate());
        }
    }

    // Get by ruleset
    @Test
    public void givenRulesetId_whenFindTournamentByRuleset_thenReturnTournamentWithRuleset() {
        // given
        given(tournamentRepository.findTournamentsByRulesetId(ruleset.getRulesetId())).willReturn(List.of(tournament));
        
        // when
        List<Tournament> tournaments = tournamentService.findTournamentsByRuleset(ruleset);

        // then
        Assertions.assertNotNull(tournaments);
        for (Tournament foundTournament : tournaments) {
            Assertions.assertEquals(ruleset.getRulesetId(), foundTournament.getRuleset().getRulesetId());
        }
    }

    // Update test
    @Test
    public void givenTournamentObject_whenUpdateTournament_thenReturnUpdatedTournament() {
       
        Date updatedDate = new Date(1000000);

        Tournament tournamentUpdate = new Tournament();
        tournamentUpdate.setName("UpdatedTournament");
        tournamentUpdate.setLocation("UpdatedLocation");
        tournamentUpdate.setDate(updatedDate);

        // given
        given(tournamentRepository.findById(tournament.getTournamentId())).willReturn(Optional.of(tournament));
        given(tournamentRepository.save(tournament)).willReturn(tournament);

        // when
        Tournament updatedTournament = tournamentService.updateTournament(tournamentUpdate, tournament.getTournamentId());

        // Then
        Assertions.assertNotNull(updatedTournament);
        Assertions.assertEquals(tournamentUpdate.getName(), updatedTournament.getName());
        
        // The toString calls below are for ensuring the date value is the same (we do not care if the object itself is the same object)
        Assertions.assertEquals(tournamentUpdate.getDate().toString(), updatedTournament.getDate().toString());
        Assertions.assertEquals(tournamentUpdate.getLocation(), updatedTournament.getLocation());
    }


    // Delete test
    @Test
    public void givenTournamentId_whenDeleteTournament_thenTournamentIsDeleted() {
         
        // given
        long tournamentId = 1L;

        willDoNothing().given(tournamentRepository).deleteById(tournamentId);

        // when
        tournamentService.deleteTournamentById(tournamentId);

        // then
        verify(tournamentRepository, times(1)).deleteById(tournamentId);
    }
}
