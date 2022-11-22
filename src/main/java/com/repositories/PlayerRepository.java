package com.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.behrend.contestmanager.models.Player;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {
    
}
