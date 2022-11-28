package com.behrend.contestmanager.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import com.behrend.contestmanager.models.Player;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@Sql(scripts = "/create-player-data.sql")
@Sql(scripts = "/cleanup-player-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class PlayerRepositoryTests {
    
    @Autowired
    private PlayerRepository playerRepository;

    @Test
    void findAllPlayers()
    {
        ArrayList<Player> players = (ArrayList<Player>) playerRepository.findAll();
        for (Player player : players)
        {
            System.out.printf("%d | %s | %s | %s | %d | %s | %s\n", player.getPlayerId(), player.getFirstName(), player.getLastName(), player.getSkipperName(), player.getRank(), player.getEmail(), player.getPhoneNum());
        }
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
    void findPlayersByFirstName()
    {
        List<Player> players = playerRepository.findAllByFirstName("John");
        Assertions.assertEquals(players.size(), 2);
    }

    @Test
    void findPlayersByLastName()
    {
        List<Player> players = playerRepository.findAllByLastName("Doe");
        Assertions.assertEquals(players.size(), 2);
    }

    @Test
    void findPlayerBySkipperName()
    {
        Player player = playerRepository.findBySkipperName("SkipperMan");
        Assertions.assertNotNull(player);
    }

    @Test
    void findPlayerByEmail()
    {
        Player player = playerRepository.findByEmail("john.doe@example.com");
        Assertions.assertNotNull(player);
    }


    @Test
    void addPlayers()
    {
        ArrayList<Player> players = new ArrayList<>();

        Player playerOne = new Player();
        playerOne.setFirstName("FirstNameOne");
        playerOne.setLastName("LastNameOne");
        playerOne.setSkipperName("OneOne");
        playerOne.setEmail("one.one@example.com");
        playerOne.setRank(1000);
        playerOne.setPhoneNum("09998887777");

        Player playerTwo = new Player();
        playerTwo.setFirstName("FirstNameTwo");
        playerTwo.setLastName("LastNameTwo");
        playerTwo.setSkipperName("TwoTwo");
        playerTwo.setEmail("two.two@example.com");
        playerTwo.setRank(1000);
        playerTwo.setPhoneNum("08887776666");

        players.add(playerOne);
        players.add(playerTwo);

        playerRepository.saveAll(players);

        ArrayList<Player> playersInDb = (ArrayList<Player>) playerRepository.findAll();

        boolean playerOneFound = false;
        boolean playerTwoFound = false;

        for (Player player : playersInDb)
        {
            if (playerOne.getEmail() == player.getEmail())
            {
                playerOneFound = true;
            }
            if (playerTwo.getEmail() == player.getEmail())
            {
                playerTwoFound = true;
            }
        }

        Assertions.assertTrue(playerOneFound);
        Assertions.assertTrue(playerTwoFound);
    }
}
