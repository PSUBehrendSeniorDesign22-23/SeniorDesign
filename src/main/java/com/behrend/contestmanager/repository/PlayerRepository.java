package com.behrend.contestmanager.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.behrend.contestmanager.models.Player;
import java.util.List;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {

    List<Player> findAllBySkipperName(String skipperName);
}