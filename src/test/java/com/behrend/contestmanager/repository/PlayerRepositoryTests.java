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

import java.util.List;
import java.util.Optional;

@DataJpaTest
@Sql(scripts = "/create-player-data.sql")
@Sql(scripts = "/cleanup-player-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class PlayerRepositoryTests {
    
    @Autowired
    private PlayerRepository playerRepository;

    @Test
    void findPlayerByID()
    {
        long id = 4;
        Optional<Player> player = playerRepository.findById(id);
        Assertions.assertEquals(player.get().getPlayerID(), id);
    }

    @Test
    void findPlayerThatDoesNotExist()
    {
        long id = 10;
        Optional<Player> player = playerRepository.findById(id);
        Assertions.assertFalse(player.isPresent());
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

}
