package com.behrend.contestmanager.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.behrend.contestmanager.models.Match;

@Repository
public interface MatchRepository extends CrudRepository<Match, Long> {

}