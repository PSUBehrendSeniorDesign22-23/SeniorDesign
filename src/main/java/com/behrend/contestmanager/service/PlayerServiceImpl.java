package com.behrend.contestmanager.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.behrend.contestmanager.repository.PlayerRepository;
import com.behrend.contestmanager.models.Player;

import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {
    
    @Autowired
    private PlayerRepository playerRepository;

    // Create
    @Override
    public Player savePlayer(Player player) {

        if (player.getRank() == null) {
            int defaultRank = 1000;
            player.setRank(defaultRank);
        }

        return playerRepository.save(player);
    }

    // Read
    @Override
    public List<Player> findAllPlayers() {
        return (List<Player>) playerRepository.findAll();
    }

    @Override
    public List<Player> findPlayerByFirstName(String firstName) {
        return playerRepository.findAllByFirstName(firstName);
    }

    @Override
    public List<Player> findPlayerByLastName(String lastName) {
        return playerRepository.findAllByLastName(lastName);
    }

    @Override
    public Player findPlayerBySkipperName(String skipperName) {
        return playerRepository.findBySkipperName(skipperName);
    }

    // Update
    @Override
    public Player updatePlayer(Player player, long playerId) {
        Player currentPlayer = playerRepository.findById(playerId).get();

        if (player.getFirstName() != null && !"".equalsIgnoreCase(player.getFirstName())) {
            currentPlayer.setFirstName(player.getFirstName());
        }

        if (player.getLastName() != null && !"".equalsIgnoreCase(player.getLastName())) {
            currentPlayer.setLastName(player.getLastName());
        }

        if (player.getSkipperName() != null && !"".equalsIgnoreCase(player.getSkipperName())) {
            currentPlayer.setSkipperName(player.getSkipperName());
        }
        
        if (currentPlayer.getRank() != null) {
            currentPlayer.setRank(player.getRank());
        }

        return playerRepository.save(currentPlayer);
    }

    // Delete
    @Override
    public void deletePlayerById(long playerId) {
        playerRepository.deleteById(playerId);
    }
}
