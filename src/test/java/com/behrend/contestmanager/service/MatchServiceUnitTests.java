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

import java.util.List;
import java.util.Optional;

import com.behrend.contestmanager.repository.MatchRepository;
import com.behrend.contestmanager.models.Match;
import com.behrend.contestmanager.models.Tournament;
import com.behrend.contestmanager.models.Player;

@ExtendWith(MockitoExtension.class)
public class MatchServiceUnitTests {
    
    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private MatchServiceImpl matchService;

    private Match match;

    private Player playerOne;
    private Player playerTwo;
    private Tournament tournament;

    @BeforeEach
    public void setup() {
        playerOne = new Player();
        playerTwo = new Player();

        tournament = new Tournament();

        match = new Match();
        match.setPlayerOne(playerOne);
        match.setPlayerTwo(playerTwo);
        match.setTournament(tournament);
    }

    // Save test
    @Test
    public void givenMatchObject_whenSaveMatch_thenReturnMatchObject(){
        // given
        given(matchRepository.save(match)).willReturn(match);

        // when
        Match savedMatch = matchService.saveMatch(match);

        // then
        Assertions.assertNotNull(savedMatch);
    }

    // Get All test
    @Test
    public void givenMatchs_whenGetAllMatchs_ReturnMatchList() {

        Match match2 = new Match();

        // given
        given(matchRepository.findAll()).willReturn(List.of(match, match2));

        // when
        List<Match> matchList = matchService.getAllMatches();

        // then
        Assertions.assertNotNull(matchList);
        Assertions.assertEquals(2, matchList.size());
    }

    // Get by tournament
    @Test
    public void givenMatchs_whenGetByTournament_thenReturnMatchsFromTournament() {
        
        Match match2 = new Match();
        match2.setTournament(tournament);

        // given
        given(matchRepository.findAllByTournamentId(tournament.getTournamentId())).willReturn(List.of(match, match2));

        // when
        List<Match> matchList = matchService.getMatchesByTournament(tournament);

        // then
        Assertions.assertNotNull(matchList);
        Assertions.assertEquals(2, matchList.size());
    }

    @Test
    public void givenPlayerObject_whenGetMatchByPlayer_thenReturnMatchesWithPlayer() {
        
        Player playerThree = new Player();

        Match match2 = new Match();
        match2.setPlayerOne(playerTwo);
        match2.setPlayerTwo(playerThree);

        Match match3 = new Match();
        match3.setPlayerOne(playerThree);
        match3.setPlayerTwo(playerOne);

        // given
        given(matchRepository.findAllByPlayerOneId(playerOne.getPlayerId())).willReturn(List.of(match));
        given(matchRepository.findAllByPlayerTwoId(playerOne.getPlayerId())).willReturn(List.of(match3));

        given(matchRepository.findAllByPlayerOneId(playerTwo.getPlayerId())).willReturn(List.of(match2));
        given(matchRepository.findAllByPlayerTwoId(playerTwo.getPlayerId())).willReturn(List.of(match));

        given(matchRepository.findAllByPlayerOneId(playerThree.getPlayerId())).willReturn(List.of(match3));
        given(matchRepository.findAllByPlayerTwoId(playerThree.getPlayerId())).willReturn(List.of(match2));

        // when
        List<Match> playerOneMatchList = matchService.getMatchesByPlayer(playerOne);
        List<Match> playerTwoMatchList = matchService.getMatchesByPlayer(playerTwo);
        List<Match> playerThreeMatchList = matchService.getMatchesByPlayer(playerThree);

        // then
        Assertions.assertNotNull(playerOneMatchList);
        Assertions.assertEquals(2, playerOneMatchList.size());

        Assertions.assertNotNull(playerTwoMatchList);
        Assertions.assertEquals(2, playerTwoMatchList.size());

        Assertions.assertNotNull(playerThreeMatchList);
        Assertions.assertEquals(2, playerTwoMatchList.size());
    }

    // Update test
    @Test
    public void givenMatchObject_whenUpdateMatch_thenReturnUpdatedMatch() {
       
        Match matchUpdate = new Match();
        matchUpdate.setPlayerOne(playerTwo);
        matchUpdate.setPlayerTwo(playerOne);
        matchUpdate.setPlayerOneScore(2);
        matchUpdate.setPlayerTwoScore(1);

        // given
        given(matchRepository.findById(match.getMatchId())).willReturn(Optional.of(match));
        given(matchRepository.save(match)).willReturn(match);

        // when
        Match updatedMatch = matchService.updateMatch(matchUpdate, match.getMatchId());

        // Then
        Assertions.assertNotNull(updatedMatch);
        Assertions.assertEquals(match.getMatchId(), updatedMatch.getMatchId());
        Assertions.assertEquals(match.getPlayerOne(), updatedMatch.getPlayerOne());
        Assertions.assertEquals(match.getPlayerTwo(), updatedMatch.getPlayerTwo());
        Assertions.assertEquals(2, updatedMatch.getPlayerOneScore());
        Assertions.assertEquals(1, updatedMatch.getPlayerTwoScore());
    }


    // Delete test
    @Test
    public void givenMatchId_whenDeleteMatch_thenMatchIsDeleted() {
         
        // given
        long matchId = 1L;

        willDoNothing().given(matchRepository).deleteById(matchId);

        // when
        matchService.deleteMatchById(matchId);

        // then
        verify(matchRepository, times(1)).deleteById(matchId);
    }
}
