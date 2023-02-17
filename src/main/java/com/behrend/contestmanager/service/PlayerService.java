package com.behrend.contestmanager.service;

import com.behrend.contestmanager.models.Player;

import java.util.List;

public interface PlayerService {
    
    // Create
    Player savePlayer(Player player);

    // Read
    List<Player> getAllPlayers();

    List<Player> findPlayerByFirstName(String firstName);

    List<Player> findPlayerByLastName(String lastName);

    Player findPlayerBySkipperName(String skipperName);

    // Update
    Player updatePlayer(Player player, long playerId);

    // Delete
    void deletePlayerById(long playerId);
}
