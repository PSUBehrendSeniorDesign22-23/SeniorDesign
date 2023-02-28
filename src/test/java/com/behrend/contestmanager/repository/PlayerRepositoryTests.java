package com.behrend.contestmanager.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import com.behrend.contestmanager.models.Player;

import java.util.ArrayList;

@DataJpaTest
@Sql(scripts = "/create-player-data.sql")
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class PlayerRepositoryTests {
    
    @Autowired
    private PlayerRepository playerRepository;

    @Test
    void findAllPlayers()
    {
        ArrayList<Player> players = (ArrayList<Player>) playerRepository.findAll();
        Assertions.assertEquals(5, players.size());
    }

    @Test
    void findPlayerById()
    {
        long playerId = 12L;
        Player player = playerRepository.findById(playerId).orElse(null);
        
        Assertions.assertNotNull(player);
        Assertions.assertEquals(playerId, player.getPlayerId());
    }

    @Test
    void findPlayerThatDoesNotExist()
    {
        long playerId = 5L;
        Player player = playerRepository.findById(playerId).orElse(null);
        Assertions.assertNull(player);
    }

    @Test
    void findPlayerBySkipperName()
    {
        Player player = playerRepository.findAllBySkipperName("SkipperMan").get(0);
        Assertions.assertNotNull(player);
    }
}
