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

import com.behrend.contestmanager.repository.PlayerRepository;
import com.behrend.contestmanager.models.Player;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceUnitTests {
    
    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerServiceImpl playerService;

    private Player player;

    @BeforeEach
    public void setup() {
        player = new Player();
        player.setRank(1000);
        player.setSkipperName("SkipOne");
    }

    // Save test
    @Test
    public void givenPlayerObject_whenSavePlayer_thenReturnPlayerObject(){
        // given
        given(playerRepository.save(player)).willReturn(player);

        // when
        Player savedPlayer = playerService.savePlayer(player);

        // then
        Assertions.assertNotNull(savedPlayer);
    }

    // Get All test
    @Test
    public void givenPlayers_whenGetAllPlayers_ReturnPlayerList() {
        
        // given
        Player player2 = new Player();
        player2.setRank(1000);
        player2.setSkipperName("SkipTwo");

        given(playerRepository.findAll()).willReturn(List.of(player, player2));

        // when
        List<Player> playerList = playerService.findAllPlayers();

        // then
        Assertions.assertNotNull(playerList);
        Assertions.assertEquals(2, playerList.size());
    }

    // Get by skipper name
    @Test
    public void givenPlayers_whenGetBySkipperName_thenReturnPlayersWithSkipperName() {

        // Given
        String testSkipperName = "SkipOne";

        Player player2 = new Player();
        player2.setRank(1000);
        player2.setSkipperName("SkipOne");

        given(playerRepository.findAllBySkipperName(testSkipperName)).willReturn(List.of(player, player2));

        // When
        List<Player> playerList = playerService.findPlayersBySkipperName(testSkipperName);

        Assertions.assertNotNull(playerList);
        Assertions.assertEquals(2, playerList.size());
        Assertions.assertEquals(testSkipperName, playerList.get(0).getSkipperName());
        Assertions.assertEquals(testSkipperName, playerList.get(1).getSkipperName());
    }

    // Update test
    @Test
    public void givenPlayerObject_whenUpdatePlayer_thenReturnUpdatedPlayer() {
        
        // Given
        Player playerUpdate = new Player();
        playerUpdate.setRank(1500);
        playerUpdate.setSkipperName("updateSkipper");

        given(playerRepository.findById(player.getPlayerId())).willReturn(Optional.of(player));
        given(playerRepository.save(player)).willReturn(player);

        // When
        Player updatedPlayer = playerService.updatePlayer(playerUpdate, player.getPlayerId());

        // Then
        Assertions.assertNotNull(updatedPlayer);
        Assertions.assertEquals(player.getPlayerId(), updatedPlayer.getPlayerId());
        Assertions.assertEquals(playerUpdate.getRank(), updatedPlayer.getRank());
        Assertions.assertEquals(playerUpdate.getSkipperName(), updatedPlayer.getSkipperName());
    }

    // Delete test
    @Test
    public void givenPlayerId_whenDeletePlayer_thenPlayerIsDeleted() {
        
        // given
        long playerId = 1L;

        willDoNothing().given(playerRepository).deleteById(playerId);

        // when
        playerService.deletePlayerById(playerId);

        // then
        verify(playerRepository, times(1)).deleteById(playerId);
    }
}
