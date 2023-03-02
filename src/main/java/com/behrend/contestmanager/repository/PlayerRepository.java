package com.behrend.contestmanager.repository;

import org.springframework.data.repository.CrudRepository;

import com.behrend.contestmanager.models.Player;
import java.util.List;

public interface PlayerRepository extends CrudRepository<Player, Long> {

    List<Player> findAllBySkipperName(String skipperName);
}