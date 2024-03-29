package com.behrend.contestmanager.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.behrend.contestmanager.repository.PlayerRepository;
import com.behrend.contestmanager.repository.UserRepository;
import com.behrend.contestmanager.models.Player;
import com.behrend.contestmanager.models.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {
    
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private UserRepository userRepository;

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
    public Player findPlayerById(long playerId) {
        return playerRepository.findById(playerId).orElse(null);
    }

    @Override
    public List<Player> findAllPlayers() {
        return (List<Player>) playerRepository.findAll();
    }

    @Override
    public List<Player> findPlayersByName(String name) {
        ArrayList<Player> players = new ArrayList<>();

        ArrayList<User> users = new ArrayList<>();
        
        users.addAll(userRepository.findByFirstName(name));
        users.addAll(userRepository.findByLastName(name));

        for (User user : users) {
            Player player = findPlayerByUserId(user.getUserId());
            if (player != null) {
                players.add(player);
            }
        }

        return players;
    }

    @Override
    public List<Player> findPlayersBySkipperName(String skipperName) {
        return playerRepository.findAllBySkipperName(skipperName);
    }

    @Override
    public Player findPlayerByUserId(long userId) {
        return playerRepository.findByUserId(userId);
    }

    // Update
    @Override
    public Player updatePlayer(Player player, long playerId) {
        Player currentPlayer = playerRepository.findById(playerId).orElse(null);

        if (currentPlayer == null) {
            return null;
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
