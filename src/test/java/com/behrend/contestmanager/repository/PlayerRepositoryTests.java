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
        ArrayList<Long> ids = new ArrayList<>();
        ids.add(12L);
        Iterable<Player> players = playerRepository.findAllById(ids);
        for (Player player : players)
        {
            Assertions.assertEquals(player.getPlayerId(), ids.get(0));
        }
    }

    @Test
    void findPlayerThatDoesNotExist()
    {
         ArrayList<Long> ids = new ArrayList<>();
        ids.add(5L);
        ArrayList<Player> players = (ArrayList<Player>) playerRepository.findAllById(ids);
        Assertions.assertTrue(players.size() == 0);
    }

    @Test
    void findPlayerBySkipperName()
    {
        Player player = playerRepository.findAllBySkipperName("SkipperMan").get(0);
        Assertions.assertNotNull(player);
    }

    @Test
    void addPlayers()
    {
        ArrayList<Player> players = new ArrayList<>();

        Player playerOne = new Player();
        playerOne.setSkipperName("OneOne");
        playerOne.setRank(1000);

        Player playerTwo = new Player();
        playerTwo.setSkipperName("TwoTwo");
        playerTwo.setRank(1000);

        players.add(playerOne);
        players.add(playerTwo);

        playerRepository.saveAll(players);

        ArrayList<Player> playersInDb = (ArrayList<Player>) playerRepository.findAll();

        boolean playerOneFound = false;
        boolean playerTwoFound = false;

        for (Player player : playersInDb)
        {
            if (playerOne.getPlayerId() == player.getPlayerId())
            {
                playerOneFound = true;
            }
            if (playerTwo.getPlayerId() == player.getPlayerId())
            {
                playerTwoFound = true;
            }
        }

        Assertions.assertTrue(playerOneFound);
        Assertions.assertTrue(playerTwoFound);
    }
}
